package ar.com.kairoslp.tripter.persistence.repository

import ar.com.kairoslp.tripter.model.User

interface UserRepository {
    fun findById(id: Long): User
    fun findByUsername(username: String): User?
}