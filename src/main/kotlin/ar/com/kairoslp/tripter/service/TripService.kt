package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpensePayment
import ar.com.kairoslp.tripter.model.account.Movement
import ar.com.kairoslp.tripter.model.expense.*
import ar.com.kairoslp.tripter.restful.request.ExpenseRequest
import ar.com.kairoslp.tripter.restful.response.DebtResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
class TripService(@Autowired val travelerNetworkService: TravelerNetworkService) {

    @Transactional
    fun addTravelerToTrip(userId: Long, tripId: Long, travelerId: Long) {
        val loggedInUser: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = loggedInUser.getTripById(tripId)
        val traveler = travelerNetworkService.findTravelerNetwork().getUserById(travelerId)
        loggedInUser.addTravelerToTrip(traveler, trip)
    }

    @Transactional
    fun addExpense(tripId: Long, expenseRequest: ExpenseRequest, userId: Long) {
        val loggedInUser: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = loggedInUser.getTripById(tripId)
        val expense = Expense(expenseRequest, trip)

        for (expenseUserPayment in expenseRequest.payments) {
            val userAccount = trip.userAccountsForTrip.single { userAccountForTrip ->
                userAccountForTrip.user.id == expenseUserPayment.userId
            }
            expense.payments.add(ExpensePayment(expenseUserPayment.amount, userAccount, expense))
        }
        val involvedUsers = trip.getTravelers().filter { user -> expenseRequest.usersIds.contains(user.id) }
        if (involvedUsers.size != expenseRequest.usersIds.size) {
            //TODO Throw error! At least one of the given users is no a traveler of this trip
            return
        }

        when (expense.strategy) {
            is ExpenseEquallySplitStrategy -> (expense.strategy as ExpenseEquallySplitStrategy).users = involvedUsers
            //TODO set proper input
            is ExpenseSplitByValuesStrategy -> (expense.strategy as ExpenseSplitByValuesStrategy).valuesByUser = ArrayList()
            //TODO set proper input
            is ExpenseSplitByPercentagesStrategy -> (expense.strategy as ExpenseSplitByPercentagesStrategy).percentagesByUser = ArrayList()
        }
        expense.splitBetween(involvedUsers)
    }

    @Transactional
    fun calculateDebts(tripId: Long, userId: Long): List<DebtResponse> {
        val loggedInUser: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = loggedInUser.getTripById(tripId)
        val userAccount = loggedInUser.getAccountFor(trip)
        val debtResponseList = ArrayList<DebtResponse>()

        val outgoingMovementsByDestination = userAccount.outgoingMovements.groupBy { it.destination }
        val incomingMovementsByOrigin = userAccount.incomingMovements.filter { it !is ExpensePayment }.groupBy { it.origin }.toMutableMap()

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
}