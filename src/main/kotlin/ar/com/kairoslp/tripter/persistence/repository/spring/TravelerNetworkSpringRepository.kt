package ar.com.kairoslp.tripter.persistence.repository.spring

import ar.com.kairoslp.tripter.model.TravelerNetwork
import org.springframework.data.jpa.repository.JpaRepository

interface TravelerNetworkSpringRepository: JpaRepository<TravelerNetwork, Long>