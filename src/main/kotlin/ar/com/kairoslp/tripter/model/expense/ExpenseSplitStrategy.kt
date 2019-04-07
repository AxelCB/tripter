package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.ExpenseDebt
import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import java.math.BigDecimal

abstract class ExpenseSplitStrategy {

    fun splitExpenseBetween(expense: Expense, users: List<User>) {
        val creditAmountPerUser: MutableMap<UserAccountForTrip, BigDecimal> = expense.payments.map { it.destination!! to it.amount }.toMap().toMutableMap()

        users.forEach {
            val userAccount = it.getAccountFor(expense.trip)
            val expenseDebtsAndCreditAmountPerUser = createDebtExpensesFor(this.calculateDebtAmountFor(expense.cost, userAccount.user), userAccount, expense, creditAmountPerUser)
            val expenseDebtList: List<ExpenseDebt> = expenseDebtsAndCreditAmountPerUser
            for (expenseDebt in expenseDebtList) {
                expense.addExpenseDebt(expenseDebt)
                userAccount.addMovement(expenseDebt)
            }
        }
    }

    private fun createDebtExpensesFor(amount: BigDecimal, userAccount: UserAccountForTrip, expense: Expense, creditAmountPerUser: MutableMap<UserAccountForTrip, BigDecimal>): List<ExpenseDebt> { //: ExpenseDebtsAndCreditAmountPerUser {
        var pendingCreditor = creditAmountPerUser.entries.first { it.value > BigDecimal.ZERO }
        if (amount <= pendingCreditor.value) {
            creditAmountPerUser[pendingCreditor.key] = pendingCreditor.value.subtract(amount)
            return arrayListOf(ExpenseDebt(amount, userAccount, pendingCreditor.key, expense))
        } else {
            var pendingAmount = amount
            val expenseDebts: MutableList<ExpenseDebt> = ArrayList()
            while (pendingAmount > BigDecimal.ZERO) {
                if (pendingAmount < pendingCreditor.value) {
                    creditAmountPerUser[pendingCreditor.key] = pendingCreditor.value.subtract(amount)
                    expenseDebts.add(ExpenseDebt(pendingAmount, userAccount, pendingCreditor.key, expense))
                    pendingAmount = BigDecimal.ZERO
                } else {
                    pendingAmount = pendingAmount.subtract(pendingCreditor.value)
                    expenseDebts.add(ExpenseDebt(pendingCreditor.value, userAccount, pendingCreditor.key, expense))
                    creditAmountPerUser[pendingCreditor.key] = BigDecimal.ZERO
                    pendingCreditor = creditAmountPerUser.entries.first { it.value > BigDecimal.ZERO }
                }
            }
            return expenseDebts
        }
    }

    abstract fun validate(expense: Expense): Boolean

    abstract fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal
}