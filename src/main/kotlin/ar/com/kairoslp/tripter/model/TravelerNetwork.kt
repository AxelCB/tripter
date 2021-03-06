package ar.com.kairoslp.tripter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class TravelerNetwork(@Id @GeneratedValue val id: Long? = null,
                      @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
                      @JoinColumn(name = "traveler_network_id")
                      var users: MutableList<User> = ArrayList(),
                      @Version val version: Long? = 0L): Serializable {

    fun addUser(user: User) {
        this.users.add(user)
    }

    fun getUserById(userId: Long): User {
        return this.users.first { user -> user.id == userId }
    }
}