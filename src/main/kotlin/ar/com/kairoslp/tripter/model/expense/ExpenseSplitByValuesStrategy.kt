package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
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
}