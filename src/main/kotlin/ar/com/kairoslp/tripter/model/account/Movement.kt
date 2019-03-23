package ar.com.kairoslp.tripter.model.account

import java.math.BigDecimal

abstract class Movement(var amount: BigDecimal, var origin: UserAccountForTrip? = null, var destination: UserAccountForTrip? = null) {

}