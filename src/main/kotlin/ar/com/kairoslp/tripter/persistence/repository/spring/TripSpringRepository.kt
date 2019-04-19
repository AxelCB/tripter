package ar.com.kairoslp.tripter.persistence.repository.spring

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.persistence.repository.TripRepository
import org.springframework.data.jpa.repository.JpaRepository

interface TripSpringRepository: JpaRepository<Trip, Long>, TripRepository