package ar.com.kairoslp.tripter.restful.request

import java.io.Serializable
import java.math.BigDecimal

class ExpenseRequest(var cost: BigDecimal, var category: String,
                     var payments: MutableList<UserAmountRequest>,
                     var strategy: ExpenseSplitStrategyEnum,
                     var usersIds: List<Long>): Serializable