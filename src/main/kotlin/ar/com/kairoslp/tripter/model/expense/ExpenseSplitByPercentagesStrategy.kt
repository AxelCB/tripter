package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal

class ExpenseSplitByPercentagesStrategy: ExpenseSplitStrategy() {
    var percentagesByUser: List<PercentageByUser> = ArrayList()

    override fun validate(expense: Expense): Boolean {
        return this.percentagesByUser.map { it.percentage }.reduce { x, y -> x.add(y) } == BigDecimal.ONE
    }

    override fun calculateDebtAmountFor(cost: BigDecimal, user: User): BigDecimal {
        val percentageByUser = this.percentagesByUser.first { percentageByUser -> percentageByUser.user == user }
        return cost.multiply(percentageByUser.percentage)
    }
}