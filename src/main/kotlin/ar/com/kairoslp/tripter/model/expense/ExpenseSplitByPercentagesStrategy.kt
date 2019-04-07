package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import java.math.BigDecimal

class ExpenseSplitByPercentagesStrategy: ExpenseSplitStrategy {
    var percentagesByUser: List<PercentageByUser> = ArrayList()

    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        if (this.percentagesByUser.map { it.percentage }.reduce { x, y -> x.add(y) } != BigDecimal.ONE) return
        users.forEach {user ->
            val userAccount = user.getAccountFor(expense.trip)
            val percentageByUser = this.percentagesByUser.first { percentageByUser -> percentageByUser.user == user }
            val cost: BigDecimal = expense.cost.multiply(percentageByUser.percentage)
            val expenseDebt = ExpenseDebt(cost, userAccount, expense)
            expense.addExpenseDebt(expenseDebt)
            userAccount.addMovement(expenseDebt)
        }
    }
}