package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.TravelerNetwork
import ar.com.kairoslp.tripter.persistence.repository.TravelerNetworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Scope
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Scope("singleton")
class TravelerNetworkService(@Autowired var travelerNetworkRepository: TravelerNetworkRepository) {

    var travelerNetwork: TravelerNetwork? = null
    var travelerNetworkId: Long? = null

    /**
     * One time initialization of first root object instance.
     * Could be done by a script to avoid this code, but was easier through code
     * specially since no specific persistence solution has been chosen yet.
     */
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initializeTravelerNetwork() {
        val travelerNetworkAmount = this.travelerNetworkRepository.count()
        if (travelerNetworkAmount == 1L) {
            travelerNetwork = this.travelerNetworkRepository.findAll().get(0)
        } else if (travelerNetworkAmount == 0L) {
            travelerNetwork = this.travelerNetworkRepository.save(TravelerNetwork())
        } else {
            System.out.println("There should be only one traveler network!!")
            //TODO throw error
        }
        travelerNetworkId = travelerNetwork?.id
    }

    @Transactional
    fun findTravelerNetwork(): TravelerNetwork {
        return travelerNetworkRepository.getOne(travelerNetworkId!!)
    }
}