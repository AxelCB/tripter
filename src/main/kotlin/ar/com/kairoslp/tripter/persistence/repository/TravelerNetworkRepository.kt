package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.model.TravelerNetwork
import org.springframework.data.jpa.repository.JpaRepository

interface TravelerNetworkRepository: JpaRepository<TravelerNetwork, Long>