package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal

class ExpenseSplitByPercentagesStrategy: ExpenseSplitStrategy() {
    var percentagesByUser: List<PercentageByUser> = ArrayList()

    /*
    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        var debtsPerUser: Map<UserAccountForTrip, BigDecimal> = expense.payments.map { it.destination!! to it.amount }.toMap()

        users.forEach {user ->
            val userAccount = user.getAccountFor(expense.trip)
            val percentageByUser = this.percentagesByUser.first { percentageByUser -> percentageByUser.user == user }
            val cost: BigDecimal = expense.cost.multiply(percentageByUser.percentage)
            var expenseDebtList: List<ExpenseDebt> = createDebtExpensesFor(cost, userAccount, expense, debtsPerUser)
            for (expenseDebt in expenseDebtList) {
                expense.addExpenseDebt(expenseDebt)
                userAccount.addMovement(expenseDebt)
            }
        }
    }

     */

    override fun validate(expense: Expense): Boolean {
        return this.percentagesByUser.map { it.percentage }.reduce { x, y -> x.add(y) } == BigDecimal.ONE
    }

    override fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal {
        val percentageByUser = this.percentagesByUser.first { percentageByUser -> percentageByUser.user == user }
        return cost.multiply(percentageByUser.percentage)
    }
}