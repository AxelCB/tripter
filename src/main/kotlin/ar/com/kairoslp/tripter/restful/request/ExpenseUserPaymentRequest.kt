package ar.com.kairoslp.tripter.restful.request

import java.math.BigDecimal

class ExpenseUserPaymentRequest(val userId: Long, val amount: BigDecimal)