package ar.com.kairoslp.tripter.model.account

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import java.math.BigDecimal
import javax.persistence.*

@Entity
class UserAccountForTrip(@ManyToOne var user: User,
                         @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH]) var trip: Trip,
                         var balance: BigDecimal = BigDecimal.ZERO,
                         @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "origin") var outgoingMovements: MutableList<Movement> = ArrayList(),
                         @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "destination") var incomingMovements: MutableList<Movement> = ArrayList(),
                         @Id @GeneratedValue val id: Long? = null,
                         @Version val version: Long? = 0L) {

    fun addMovement(movement: Movement) {
        when {
            this == movement.destination -> {
                this.balance += movement.amount
                this.incomingMovements.add(movement)
            }
            this == movement.origin -> {
                this.balance -= movement.amount
                this.outgoingMovements.add(movement)
            }
            else -> //TODO throw error! movement is not of this account
                return
        }
    }
}