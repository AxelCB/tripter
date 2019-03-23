package ar.com.kairoslp.tripter.model.account

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal

class UserAccountForTrip(var user: User, var trip: Trip) {
    var balance: BigDecimal = BigDecimal.ZERO
    var movements: List<Movement> = ArrayList()

    fun addMovement(movement: Movement) {
        when {
            this == movement.destination -> this.balance += movement.amount
            this == movement.origin -> this.balance -= movement.amount
            else -> //TODO throw error! movement is not of this account
                return
        }
        this.movements += movement
    }
}