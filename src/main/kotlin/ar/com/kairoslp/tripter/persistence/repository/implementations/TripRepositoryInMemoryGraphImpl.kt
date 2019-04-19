package ar.com.kairoslp.tripter.persistence.repository.implementations

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.persistence.repository.TripRepository
import ar.com.kairoslp.tripter.service.TravelerNetworkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TripRepositoryInMemoryGraphImpl(@Autowired val travelerNetworkService: TravelerNetworkService): TripRepository {

    override fun findById(id: Long): Trip {
        //TODO Missing user id! Can't get a trip without knowing one of it's travelers
        val invalidId = -1L
        return this.travelerNetworkService.findTravelerNetwork().getUserById(invalidId).getTripById(id)
    }

}