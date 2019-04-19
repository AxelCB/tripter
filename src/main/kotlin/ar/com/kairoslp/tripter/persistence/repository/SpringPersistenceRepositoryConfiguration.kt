package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.persistence.repository.spring.TripSpringRepository
import ar.com.kairoslp.tripter.persistence.repository.spring.UserSpringRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringPersistenceRepositoryConfiguration(@Autowired val userSpringRepository: UserSpringRepository, @Autowired val tripSpringRepository: TripSpringRepository) {

    @Bean
    fun userRepository(): UserRepository {
        return this.userSpringRepository
    }

    @Bean
    fun tripRepository(): TripRepository {
        return this.tripSpringRepository
    }
}