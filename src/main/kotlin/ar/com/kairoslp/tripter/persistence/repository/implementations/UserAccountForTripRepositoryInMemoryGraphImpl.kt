package ar.com.kairoslp.tripter.persistence.repository.implementations

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import ar.com.kairoslp.tripter.persistence.repository.UserAccountForTripRepository
import ar.com.kairoslp.tripter.service.TravelerNetworkService
import org.springframework.beans.factory.annotation.Autowired

class UserAccountForTripRepositoryInMemoryGraphImpl(@Autowired val travelerNetworkService: TravelerNetworkService): UserAccountForTripRepository {

    override fun findByUserIdAndTripId(userId: Long, tripId: Long): UserAccountForTrip? {
        val user: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = user.getTripById(tripId)
        return user.getAccountFor(trip)
    }
}