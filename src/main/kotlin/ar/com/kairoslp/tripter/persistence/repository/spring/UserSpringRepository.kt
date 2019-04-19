package ar.com.kairoslp.tripter.persistence.repository.spring

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.persistence.repository.UserRepository
import org.springframework.data.jpa.repository.JpaRepository

interface UserSpringRepository: JpaRepository<User, Long>, UserRepository {
    override fun findByUsername(username: String): User?
}