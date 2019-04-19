package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.MissingServletRequestParameterException
import java.time.LocalDate
import javax.transaction.Transactional

@Service
class UserService(@Autowired val travelerNetworkService: TravelerNetworkService, @Autowired val passwordEncoder: PasswordEncoder, @Autowired val userRepository: UserRepository) {

    @Transactional
    fun register(firstName: String, lastName: String, email: String, password: String): User {
        val user = User(firstName, lastName, email, password)
        user.password = passwordEncoder.encode(password)
        travelerNetworkService.findTravelerNetwork().addUser(user)
        return user
    }

    @Transactional
    fun organizeTrip(userId: Long, title: String,
                     startDate: LocalDate, endDate: LocalDate?): Trip {
        val user: User = travelerNetworkService.findTravelerNetwork().getUserById(userId)
        return user.organizeNewTrip(title, startDate, endDate)
    }

    @Transactional
    @Throws
    fun getLoggedInUser(): User {
        val userDetails = SecurityContextHolder.getContext().authentication.principal
        if (userDetails is UserDetails) {
            val loggedInUser: User? = this.userRepository.findByUsername(userDetails.username)
            if (loggedInUser != null && loggedInUser.id == null) {
                throw MissingServletRequestParameterException("Logged in user ID", "Long")
            }
            if (loggedInUser != null)
                return loggedInUser
        }
        throw AccessDeniedException("User is not logged in!")
    }
}