package ar.com.kairoslp.tripter.restful.controller

import ar.com.kairoslp.tripter.restful.request.ExpenseRequest
import ar.com.kairoslp.tripter.restful.request.UserAmountRequest
import ar.com.kairoslp.tripter.restful.response.DebtResponse
import ar.com.kairoslp.tripter.service.TripService
import ar.com.kairoslp.tripter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/trip")
class TripController(@Autowired val userService: UserService, @Autowired val tripService: TripService) {

    @PostMapping
    fun organizeTrip(@RequestParam title: String, @RequestParam startDate: LocalDate, @RequestParam(required = false) endDate: LocalDate?): Long? {
        return this.userService.organizeTrip(this.userService.getLoggedInUser().id!!, title, startDate, endDate).id
    }

    @PostMapping("/{tripId}/traveler")
    fun addTraveler(@PathVariable tripId: Long, @RequestParam travelerId: Long) {
        return this.tripService.addTravelerToTrip(this.userService.getLoggedInUser().id!!, tripId, travelerId)
    }

    @DeleteMapping("/{tripId}/traveler/{travelerId}")
    fun removeTraveler(@PathVariable tripId: Long, @PathVariable travelerId: Long) {
        return this.tripService.removeTravelerFromTrip(this.userService.getLoggedInUser().id!!, tripId, travelerId)
    }

    @PostMapping("/{tripId}/{version}/expense")
    fun addExpense(@PathVariable tripId: Long, @PathVariable version: Long, @RequestBody expenseRequest: ExpenseRequest) {
        return this.tripService.addExpense(tripId, version, expenseRequest, this.userService.getLoggedInUser().id!!)
    }

    @GetMapping("/{tripId}/debt")
    fun getDebts(@PathVariable tripId: Long): List<DebtResponse> {
        return this.tripService.calculateDebts(tripId, this.userService.getLoggedInUser().id!!)
    }

    @PostMapping("/{tripId}/loan")
    fun addLoan(@PathVariable tripId: Long, @RequestBody loanRequest: UserAmountRequest) {
        return this.tripService.addLoan(tripId, loanRequest, this.userService.getLoggedInUser().id!!)
    }

    @PostMapping("/{tripId}/debtPayment")
    fun addDebtPayment(@PathVariable tripId: Long, @RequestBody loanRequest: UserAmountRequest) {
        return this.tripService.addDebtPayment(tripId, loanRequest, this.userService.getLoggedInUser().id!!)
    }
}