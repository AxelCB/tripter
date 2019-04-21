package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.DebtPayment
import ar.com.kairoslp.tripter.model.account.ExpensePayment
import ar.com.kairoslp.tripter.model.account.Loan
import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import ar.com.kairoslp.tripter.model.expense.*
import ar.com.kairoslp.tripter.persistence.repository.TripRepository
import ar.com.kairoslp.tripter.persistence.repository.UserAccountForTripRepository
import ar.com.kairoslp.tripter.persistence.repository.UserRepository
import ar.com.kairoslp.tripter.restful.request.ExpenseRequest
import ar.com.kairoslp.tripter.restful.request.UserAmountRequest
import ar.com.kairoslp.tripter.restful.response.DebtResponse
import ar.com.kairoslp.tripter.restful.response.UserNotTravelerOfTripException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.MissingServletRequestParameterException
import javax.transaction.Transactional

@Service
class TripService(@Autowired val travelerNetworkService: TravelerNetworkService,
                  @Autowired val userRepository: UserRepository,
                  @Autowired val tripRepository: TripRepository,
                  @Autowired val userAccountForTripRepository: UserAccountForTripRepository) {

    @Transactional
    fun addTravelerToTrip(userId: Long, tripId: Long, travelerId: Long) {
        val loggedInUser: User =  userRepository.findById(userId)
        val trip: Trip = tripRepository.findById(tripId)
        val traveler = userRepository.findById(travelerId)
        loggedInUser.addTravelerToTrip(traveler, trip)
    }

    @Throws
    @Transactional
    fun addExpense(tripId: Long, expenseRequest: ExpenseRequest, userId: Long) {
        val trip: Trip = tripRepository.findById(tripId)
        val expense = Expense(expenseRequest, trip)

        for (expenseUserPayment in expenseRequest.payments) {
            val userAccount = userAccountForTripRepository.findByUserIdAndTripId(expenseUserPayment.userId, tripId)!!
            expense.payments.add(ExpensePayment(expenseUserPayment.amount, userAccount, expense))
        }
        val involvedUsers = trip.getTravelers().filter { user -> expenseRequest.usersIds.contains(user.id) }
        if (involvedUsers.size < expenseRequest.usersIds.size) {
            val notTravelers: List<Long> = (involvedUsers.map { it.id } + expenseRequest.usersIds)
                    .groupBy { it }
                    .filter { it.value.size == 1 }
                    .flatMap { it.value }
                    .requireNoNulls()
            if (notTravelers.size > 1) {
                throw UserNotTravelerOfTripException(notTravelers.first(),tripId)
            } else {
                throw UserNotTravelerOfTripException(notTravelers, tripId)
            }
        }

        when (expense.strategy) {
            is ExpenseEquallySplitStrategy -> (expense.strategy as ExpenseEquallySplitStrategy).users = involvedUsers
            is ExpenseSplitByValuesStrategy -> {
                if (expenseRequest.amountPerUser != null) {
                    val expenseStrategy = (expense.strategy as ExpenseSplitByValuesStrategy)
                    expenseStrategy.valuesByUser = expenseRequest.amountPerUser!!.map { ValueByUser(it.amount, trip.getTravelerAccountByUserId(it.userId).user) }
                } else {
                    throw MissingServletRequestParameterException("amountPerUser","UserAmountRequest(userId, amount)")
                }
            }
            is ExpenseSplitByPercentagesStrategy -> {
                if (expenseRequest.amountPerUser != null) {
                    val expenseStrategy = (expense.strategy as ExpenseSplitByPercentagesStrategy)
                    expenseStrategy.percentagesByUser = expenseRequest.amountPerUser!!.map { PercentageByUser(it.amount, trip.getTravelerAccountByUserId(it.userId).user) }
                } else {
                    throw MissingServletRequestParameterException("amountPerUser","UserAmountRequest(userId, amount)")
                }
            }
        }
        expense.splitBetween(involvedUsers)
    }

    @Transactional
    fun calculateDebts(tripId: Long, userId: Long): List<DebtResponse> {
        val loggedInUser: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = loggedInUser.getTripById(tripId)
        val userAccount = loggedInUser.getAccountFor(trip)
        val debtResponseList = ArrayList<DebtResponse>()

        val outgoingMovementsByDestination = userAccount.outgoingMovements.filter { it.destination != it.origin } .groupBy { it.destination }
        val incomingMovementsByOrigin = userAccount.incomingMovements.filter { it !is ExpensePayment && it.destination != it.origin }.groupBy { it.origin }.toMutableMap()

        outgoingMovementsByDestination.forEach {
            var debtAmount = it.value.map { it.amount }.reduce { acc, movementAmount -> acc.add(movementAmount) }
            if(incomingMovementsByOrigin[it.key] != null && incomingMovementsByOrigin.getValue(it.key).isNotEmpty()) {
                debtAmount = debtAmount.subtract(
                    incomingMovementsByOrigin.getValue(it.key)
                            .map { movement -> movement.amount }
                            .reduce { acc, movementAmount -> acc.add(movementAmount) }
                )
                incomingMovementsByOrigin.remove(it.key)
            }
            debtResponseList.add(DebtResponse(it.key!!.user.id!!, it.key!!.user.fullName, debtAmount))
        }
        incomingMovementsByOrigin.forEach{
            val debtAmount = incomingMovementsByOrigin.getValue(it.key)
                    .map { movement -> movement.amount }
                    .reduce { acc, movementAmount -> acc.add(movementAmount) }.negate()
            debtResponseList.add(DebtResponse(it.key!!.user.id!!, it.key!!.user.fullName, debtAmount))
        }
        return debtResponseList
    }

    @Transactional
    fun addLoan(tripId: Long, loanRequest: UserAmountRequest, userId: Long) {
        val loggedInUserAccount: UserAccountForTrip = userAccountForTripRepository.findByUserIdAndTripId(userId, tripId)!!
        loggedInUserAccount.addMovement(Loan(loanRequest.amount, loggedInUserAccount, userAccountForTripRepository.findByUserIdAndTripId(loanRequest.userId, tripId)!!))
    }

    @Transactional
    fun addDebtPayment(tripId: Long, debtPaymentRequest: UserAmountRequest, userId: Long) {
        val loggedInUserAccount: UserAccountForTrip = userAccountForTripRepository.findByUserIdAndTripId(userId, tripId)!!
        loggedInUserAccount.addMovement(DebtPayment(debtPaymentRequest.amount, loggedInUserAccount, userAccountForTripRepository.findByUserIdAndTripId(debtPaymentRequest.userId, tripId)!!))
    }
}