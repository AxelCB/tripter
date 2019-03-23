package ar.com.kairoslp.tripter.model

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import java.util.*

class User(var firstName: String, var lastName: String, var email: String, var password: String) {

    var userAccountsForTrips: List<UserAccountForTrip> = ArrayList()

    fun getTrips(): List<Trip> {
        return this.userAccountsForTrips.map(UserAccountForTrip::trip)
    }

    fun organizeNewTrip(title: String, startDate: Date, endDate: Date? = null): Trip {
        val trip = Trip(title, startDate, this, endDate)
        trip.addTraveler(this)
        return trip
    }

    //A user cannot join a trip by himself, he must be added/invited by the organizer
    fun joinTrip(userAccountForTrip: UserAccountForTrip) {
        this.userAccountsForTrips+= userAccountForTrip
    }

    fun addTravelerToTrip(user: User, trip: Trip) {
        if (trip.organizer == this) {
            trip.addTraveler(user)
        }
    }

    @Throws
    fun getAccountFor(trip: Trip): UserAccountForTrip {
        //Might throw if user is not a traveler of the provided trip
        return this.userAccountsForTrips.filter { userAccountForTrip -> userAccountForTrip.trip.equals(trip) }.single()
    }


}