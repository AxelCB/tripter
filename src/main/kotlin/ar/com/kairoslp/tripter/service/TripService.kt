package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TripService(@Autowired val travelerNetworkService: TravelerNetworkService) {

    @Transactional
    fun addTravelerToTrip(userId: Long, tripId: Long, traveler: User) {
        val user: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        val trip: Trip = user.getTripById(tripId)
        user.addTravelerToTrip(traveler, trip)
    }
}