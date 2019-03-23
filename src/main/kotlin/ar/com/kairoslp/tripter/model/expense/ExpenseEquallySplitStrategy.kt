package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import java.math.BigDecimal

class ExpenseEquallySplitStrategy: ExpenseSplitStrategy {

    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        users.forEach {
            val userAccount = it.getAccountFor(expense.trip)
            val expenseDebt = ExpenseDebt(expense.cost.divide(BigDecimal(users.size), BigDecimal.ROUND_HALF_EVEN), userAccount)
            expense.addExpenseDebt(expenseDebt)
            userAccount.addMovement(expenseDebt)

        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}