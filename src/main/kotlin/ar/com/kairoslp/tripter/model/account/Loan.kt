package ar.com.kairoslp.tripter.model.account

import java.math.BigDecimal

class Loan(amount: BigDecimal, origin: UserAccountForTrip, destination: UserAccountForTrip): Movement(amount, origin, destination) {
}