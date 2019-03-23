package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import ar.com.kairoslp.tripter.model.account.ExpensePayment

class Expense(var cost: BigDecimal, var category: String, var payments: List<ExpensePayment>,//Non-persistent
              var strategy: ExpenseSplitStrategy, var trip: Trip, debts: List<ExpenseDebt>?) {

    var debts: List<ExpenseDebt> = debts ?: ArrayList()

    init {
        trip.addExpense(this)
    }

    fun splitBetween(users: List<User>) {
        this.strategy.splitExpenseBetween(this, users)
    }

    fun addExpenseDebt(expenseDebt: ExpenseDebt) {
        this.debts += expenseDebt
    }
}