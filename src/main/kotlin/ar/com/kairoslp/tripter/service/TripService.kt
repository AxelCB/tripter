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
import org.springframework.security.access.AccessDeniedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.web.bind.MissingServletRequestParameterException
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
class TripService(@Autowired val userRepository: UserRepository,
                  @Autowired val tripRepository: TripRepository,
                  @Autowired val userAccountForTripRepository: UserAccountForTripRepository,
                  @PersistenceContext val em: EntityManager) {

    @Throws(AccessDeniedException::class)
    @Transactional
    fun addTravelerToTrip(userId: Long, tripId: Long, travelerId: Long) {
        val loggedInUser: User =  userRepository.findById(userId)
        val trip: Trip = tripRepository.findById(tripId)
        val traveler = userRepository.findById(travelerId)
        em.lock(trip, LockModeType.OPTIMISTIC_FORCE_INCREMENT)
        loggedInUser.addTravelerToTrip(traveler, trip)
    }

    @Throws(EntityNotLatestVersionException::class, UserNotTravelerOfTripException::class, MissingServletRequestParameterException::class)
    @Transactional
    fun addExpense(tripId: Long, tripVersion: Long, expenseRequest: ExpenseRequest, userId: Long) {
        val trip: Trip = tripRepository.findById(tripId)
        em.lock(trip, LockModeType.OPTIMISTIC_FORCE_INCREMENT)
        val expense = Expense(expenseRequest, trip)

        if (tripVersion < trip.version!!) {
            throw EntityNotLatestVersionException("trip")
        }
        
        try {
            trip.getTravelerAccountByUserId(userId)
        } catch (e: Exception) {
            throw UserNotTravelerOfTripException(userId, tripId)
        }
        
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
        val userAccount = userAccountForTripRepository.findByUserIdAndTripId(userId, tripId)!!
        val debtResponseList = ArrayList<DebtResponse>()

        val outgoingMovementsByDestination = userAccount.outgoingMovements.filter { it.destination != it.origin } .groupBy { it.destination }
        val incomingMovementsByOrigin = userAccount.incomingMovements.filter { it !is ExpensePayment && it.destination != it.origin }.groupBy { it.origin }.toMutableMap()

        outgoingMovementsByDestination.forEach {
            var debtAmount = it.value.map { movement -> movement.amount }.reduce { acc, movementAmount -> acc.add(movementAmount) }
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

    @Throws(AccessDeniedException::class, UserNotTravelerOfTripException::class, EntityNotFoundException::class, UserInvoledInMovementsException::class)
    @Transactional
    fun removeTravelerFromTrip(userId: Long, tripId: Long, travelerId: Long) {
        val loggedInUser: User =  userRepository.findById(userId)
        val trip: Trip
        val traveler: User
        try {
            trip = tripRepository.findById(tripId)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException(tripId, Trip::class.simpleName!!)
        }
        try {
            traveler = userRepository.findById(travelerId)
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException(travelerId, User::class.simpleName!!)
        }
        val travelerAccount: UserAccountForTrip
        try {
            travelerAccount = trip.getTravelerAccountByUserId(travelerId)
        } catch (e: NoSuchElementException) {
            throw UserNotTravelerOfTripException(userId, tripId) 
        }
        
        if (trip.organizer != loggedInUser) {
            throw AccessDeniedException("User must be organizer of trip ${trip.id} to be able to remove travelers.")
        }
        if (travelerAccount.incomingMovements.isNotEmpty() || travelerAccount.outgoingMovements.isNotEmpty()) {
            throw UserInvoledInMovementsException(userId, tripId)
        }
        trip.removeTraveler(traveler)
    }
}


class EntityNotLatestVersionException(entityName: String): Exception("The $entityName you are trying to update is not on it's latest version.")

class EntityNotFoundException(id: Long, entityName: String): Exception("No $entityName found with id $id .")

class UserInvoledInMovementsException(userId: Long, tripId: Long): Exception("User with $userId already involed in movements in trip with id $tripId.")
