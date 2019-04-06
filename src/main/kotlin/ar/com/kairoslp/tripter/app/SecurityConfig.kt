package ar.com.kairoslp.tripter.app

import ar.com.kairoslp.tripter.service.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
class SecurityConfig(@Autowired val userDetailsService: UserDetailsService): WebSecurityConfigurerAdapter() {

    @Throws
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/index", "/user/register", "/**/*swagger*/**", "/v2/api-docs").permitAll()
                .antMatchers("/traveler/**").hasRole("USER")
                .antMatchers("/**").authenticated()
                .and().formLogin().successForwardUrl("/user/me")
                .and().csrf().disable()

    }

    @Autowired
    @Throws
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
