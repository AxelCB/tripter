package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.model.account.UserAccountForTrip

interface UserAccountForTripRepository {
    fun findByUserIdAndTripId(userId: Long, tripId: Long): UserAccountForTrip?
}