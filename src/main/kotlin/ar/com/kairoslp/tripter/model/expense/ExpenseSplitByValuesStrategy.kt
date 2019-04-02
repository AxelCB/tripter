package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt

class ExpenseSplitByValuesStrategy: ExpenseSplitStrategy {
    var valuesByUser: List<ValueByUser> = ArrayList()

    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        if (this.valuesByUser.map { it.value }.reduce { x, y -> x.add(y) } != expense.cost) return
        users.forEach {user ->
            val userAccount = user.getAccountFor(expense.trip)
            val valueByUser = this.valuesByUser.filter { valueByUser -> valueByUser.user == user }.first()
            val expenseDebt = ExpenseDebt(valueByUser.value, userAccount, expense)
            expense.addExpenseDebt(expenseDebt)
            userAccount.addMovement(expenseDebt)
        }
    }
}