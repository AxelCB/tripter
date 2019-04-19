package ar.com.kairoslp.tripter.persistence.repository.spring

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip
import ar.com.kairoslp.tripter.persistence.repository.UserAccountForTripRepository
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountForTripSpringRepository: JpaRepository<UserAccountForTrip, Long>, UserAccountForTripRepository {
    override fun findByUserIdAndTripId(userId: Long, tripId: Long): UserAccountForTrip?
}