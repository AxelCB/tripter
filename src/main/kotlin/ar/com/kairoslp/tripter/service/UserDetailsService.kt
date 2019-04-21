package ar.com.kairoslp.tripter.service

import ar.com.kairoslp.tripter.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class UserDetailsService(@Autowired private val userRepository: UserRepository): UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)

        val grantedAuthorities = HashSet<SimpleGrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority("TRAVELER"))

        return org.springframework.security.core.userdetails.User(user.email, user.password, grantedAuthorities)
    }
}