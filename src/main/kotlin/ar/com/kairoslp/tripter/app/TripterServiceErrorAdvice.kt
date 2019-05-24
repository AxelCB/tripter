package ar.com.kairoslp.tripter.app

import ar.com.kairoslp.tripter.service.ExistingUserWithEmailException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class TripterServiceErrorAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ExistingUserWithEmailException::class)
    fun handle(e: ExistingUserWithEmailException) {}
}