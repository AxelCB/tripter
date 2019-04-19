package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.model.Trip

interface TripRepository {
    fun findById(id: Long): Trip
}