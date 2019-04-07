package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import java.math.BigDecimal

class ExpenseSplitByValuesStrategy: ExpenseSplitStrategy() {
    var valuesByUser: List<ValueByUser> = ArrayList()

    override fun validate(expense: Expense): Boolean {
        return this.valuesByUser.map { it.value }.reduce { x, y -> x.add(y) } == expense.cost
    }

    override fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal {
        val valueByUser = this.valuesByUser.first { valueByUser -> valueByUser.user == user }
        return valueByUser.value
    }

/*
    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        if (this.valuesByUser.map { it.value }.reduce { x, y -> x.add(y) } != expense.cost) return

        var debtsPerUser: Map<UserAccountForTrip, BigDecimal> = expense.payments.map { it.destination!! to it.amount }.toMap()

        users.forEach {user ->
            val userAccount = user.getAccountFor(expense.trip)
            val valueByUser = this.valuesByUser.first { valueByUser -> valueByUser.user == user }
            val expenseDebt = ExpenseDebt(valueByUser.value, userAccount, expense)
            expense.addExpenseDebt(expenseDebt)
            userAccount.addMovement(expenseDebt)
        }
    }

 */
}