package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import ar.com.kairoslp.tripter.model.account.ExpensePayment
import javax.persistence.*

@Entity
class Expense(var cost: BigDecimal, var category: String,
              @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "expense") var payments: MutableList<ExpensePayment>,
              //Non-persistent
              @Transient var strategy: ExpenseSplitStrategy,
              @ManyToOne var trip: Trip,
              @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "expense") var debts: MutableList<ExpenseDebt> = ArrayList(),
              @Id @GeneratedValue val id: Long? = null) {


    init {
        trip.addExpense(this)
    }

    fun splitBetween(users: List<User>) {
        this.strategy.splitExpenseBetween(this, users)
    }

    fun addExpenseDebt(expenseDebt: ExpenseDebt) {
        this.debts.add(expenseDebt)
    }
}