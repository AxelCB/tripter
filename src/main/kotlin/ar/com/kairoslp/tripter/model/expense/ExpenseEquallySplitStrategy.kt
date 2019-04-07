package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal

class ExpenseEquallySplitStrategy: ExpenseSplitStrategy() {
    var users: List<User> = ArrayList()

    override fun validate(expense: Expense): Boolean {
        return true
    }

    override fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal {
        return cost.divide(BigDecimal(users.size), BigDecimal.ROUND_HALF_EVEN)
    }
}