package ar.com.kairoslp.tripter.model.account

import java.math.BigDecimal

class ExpenseDebt(amount: BigDecimal, origin: UserAccountForTrip): Movement(amount, origin) {
}