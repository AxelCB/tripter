package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import java.math.BigDecimal

class ExpenseEquallySplitStrategy: ExpenseSplitStrategy() {
    var users: List<User> = ArrayList()

    override fun validate(expense: Expense): Boolean {
        return true
    }

    override fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal {
        return cost.divide(BigDecimal(users.size), BigDecimal.ROUND_HALF_EVEN)
    }
    /*
    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        var debtsPerUser: Map<UserAccountForTrip, BigDecimal> = expense.payments.map { it.destination!! to it.amount }.toMap()

        users.forEach {
            val userAccount = it.getAccountFor(expense.trip)
            var expenseDebtList: List<ExpenseDebt> = createDebtExpensesFor(expense.cost.divide(BigDecimal(users.size), BigDecimal.ROUND_HALF_EVEN), userAccount, expense, debtsPerUser)
            for (expenseDebt in expenseDebtList) {
                expense.addExpenseDebt(expenseDebt)
                userAccount.addMovement(expenseDebt)
            }
        }
    }
    */
}