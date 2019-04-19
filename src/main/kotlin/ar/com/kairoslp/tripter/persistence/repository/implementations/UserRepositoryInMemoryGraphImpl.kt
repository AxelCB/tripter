package ar.com.kairoslp.tripter.persistence.repository.implementations

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.persistence.repository.UserRepository
import ar.com.kairoslp.tripter.service.TravelerNetworkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserRepositoryInMemoryGraphImpl(@Autowired val travelerNetworkService: TravelerNetworkService): UserRepository {
    override fun findByUsername(username: String): User? {
        return this.travelerNetworkService.findTravelerNetwork().users.single { it.username == username }
    }

    override fun findById(id: Long): User {
        return this.travelerNetworkService.findTravelerNetwork().getUserById(id)
    }

}