package ar.com.kairoslp.tripter.restful.controller

import ar.com.kairoslp.tripter.model.Trip
import ar.com.kairoslp.tripter.model.User
import ar.com.kairoslp.tripter.service.TripService
import ar.com.kairoslp.tripter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/trip")
class TripController(@Autowired val userService: UserService, @Autowired val tripService: TripService) {

    @PostMapping
    fun organizeTrip(@PathVariable userId: Long, @RequestParam title: String,
                     @RequestParam startDate: Date, @RequestParam endDate: Date?): Trip {
        return this.userService.organizeTrip(userId, title, startDate, endDate)
    }

    @PostMapping("/{tripId}/traveler")
    fun addTravelerToTrip(@PathVariable tripId: Long, @RequestParam traveler: User) {
        return this.tripService.addTravelerToTrip(this.userService.getLoggedInUser().id!!, tripId, traveler)
    }
}