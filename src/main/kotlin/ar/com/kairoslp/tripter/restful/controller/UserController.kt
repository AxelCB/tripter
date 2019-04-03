package ar.com.kairoslp.tripter.restful.controller

import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.service.TravelerNetworkService
import ar.com.kairoslp.tripter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(@Autowired val userService: UserService, @Autowired val travelerNetworkService: TravelerNetworkService) {

    @PostMapping("/register")
    fun register(@RequestParam firstName: String, @RequestParam lastName: String, @RequestParam email: String, @RequestParam password: String): User {
        return userService.register(firstName, lastName, email, password)
    }

    @GetMapping("/me")
    fun getUser(): User {
        return userService.getLoggedInUser()
    }
}