package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}