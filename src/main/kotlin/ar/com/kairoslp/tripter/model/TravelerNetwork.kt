package ar.com.kairoslp.tripter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.ArrayList
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class TravelerNetwork(@Id @GeneratedValue val id: Long? = null,
                      @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true) var users: MutableList<User> = ArrayList()): Serializable {

    fun addUser(user: User) {
        this.users.add(user)
    }

    fun getUserById(userId: Long): User {
        return this.users.filter {user -> user.id == userId  }.first()
    }
}