package ar.com.kairoslp.tripter.model

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.access.AccessDeniedException
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "TRIPTER_USER")
class User(var firstName: String,
           var lastName: String,
           @Column(unique = true) var email: String,
           @JsonIgnore var password: String,
           @Id @GeneratedValue val id: Long? = null,
           @Version val version: Long? = 0L,
           @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "user") var userAccountsForTrips: MutableList<UserAccountForTrip> = ArrayList(),
           @Column(unique = true) var username: String = email): Serializable {

    val fullName: String get() = "$firstName $lastName"

    fun getTrips(): List<Trip> {
        return this.userAccountsForTrips.map(UserAccountForTrip::trip)
    }

    fun organizeNewTrip(title: String, startDate: LocalDate, endDate: LocalDate? = null): Trip {
        val trip = Trip(title, startDate, this, endDate)
            trip.addTraveler(this)
        return trip
    }

    //A user cannot join a trip by himself, he must be added/invited by the organizer
    fun joinTrip(userAccountForTrip: UserAccountForTrip) {
        this.userAccountsForTrips.add(userAccountForTrip)
    }

    @Throws(AccessDeniedException::class)
    fun addTravelerToTrip(user: User, trip: Trip) {
        if (trip.organizer == this) {
            trip.addTraveler(user)
        } else {
            throw AccessDeniedException("User must be organizer of trip ${trip.id} to be able to add travelers.")
        }
    }

    @Throws
    fun getAccountFor(trip: Trip): UserAccountForTrip {
        //Might throw if user is not a traveler of the provided trip
        return this.userAccountsForTrips.single { userAccountForTrip -> userAccountForTrip.trip == trip }
    }

    fun getTripById(tripId: Long): Trip {
        return this.getTrips().first { trip -> trip.id == tripId }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (id === null) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}