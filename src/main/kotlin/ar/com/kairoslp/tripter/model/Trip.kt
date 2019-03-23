package ar.com.kairoslp.tripter.model

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import ar.com.kairoslp.tripter.model.expense.Expense
import java.util.*

class Trip(var title: String, var startDate: Date, var organizer: User, var endDate: Date? = null) {

    var userAccountsForTrip: List<UserAccountForTrip> = ArrayList()
    var expenses: List<Expense> = ArrayList()


    fun getTravelers(): List<User> {
        return this.userAccountsForTrip.map(UserAccountForTrip::user)
    }

    fun addTraveler(user: User) {
        val userAccountForTrip = UserAccountForTrip(user,this)
        this.userAccountsForTrip += userAccountForTrip
        user.joinTrip(userAccountForTrip)
    }

    fun addExpense(expense: Expense) {
        this.expenses += expense
    }
}