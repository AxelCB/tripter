package ar.com.kairoslp.tripter.model

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import ar.com.kairoslp.tripter.model.expense.Expense
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Trip(var title: String,
           var startDate: LocalDate,
           @ManyToOne var organizer: User,
           var endDate: LocalDate? = null,
           @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "trip") var userAccountsForTrip: MutableList<UserAccountForTrip> = ArrayList(),
           @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "trip") var expenses: MutableList<Expense> = ArrayList(),
           @Id @GeneratedValue val id: Long? = null) {

    fun getTravelers(): List<User> {
        return this.userAccountsForTrip.map(UserAccountForTrip::user)
    }

    fun addTraveler(user: User) {
        val userAccountForTrip = UserAccountForTrip(user,this)
        this.userAccountsForTrip.add(userAccountForTrip)
        user.joinTrip(userAccountForTrip)
    }

    fun addExpense(expense: Expense) {
        this.expenses.add(expense)
    }

    fun getTravelerAccountByUserId(userId: Long): UserAccountForTrip {
        return userAccountsForTrip.single { userAccountForTrip ->
            userAccountForTrip.user.id == userId
        }
    }

}