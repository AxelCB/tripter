package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpensePayment
import ar.com.kairoslp.tripter.model.expense.Expense
import ar.com.kairoslp.tripter.restful.request.ExpenseRequest
import ar.com.kairoslp.tripter.restful.response.DebtResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
        //TODO if expense split strategy was not equally, must set ValueByUser's or PercentageByUser
        val involvedUsers = trip.getTravelers().filter { user -> expenseRequest.usersIds.contains(user.id) }
        if (involvedUsers.size != expenseRequest.usersIds.size) {
            //TODO Throw error! At least one of the given users is no a traveler of this trip
        }
        expense.splitBetween(involvedUsers)
    }

    @Transactional
    fun calculateDebts(tripId: Long, userId: Long): List<DebtResponse> {
        //TODO Calculate user's debts
        return ArrayList()
    }
}