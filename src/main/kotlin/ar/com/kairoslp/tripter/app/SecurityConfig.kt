package ar.com.kairoslp.tripter.app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Throws
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/index", "/register").permitAll()
                .antMatchers("/traveler/**").permitAll()
//                .antMatchers("/traveler/**").hasRole("USER")
                .and()
                .formLogin()
//        .loginPage("/login").failureUrl("/login-error")

    }

    @Autowired
    @Throws
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("123456").roles("USER")
    }
}
