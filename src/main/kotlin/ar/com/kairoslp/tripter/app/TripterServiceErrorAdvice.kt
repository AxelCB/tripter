package ar.com.kairoslp.tripter.app

import ar.com.kairoslp.tripter.service.EntityNotLatestVersionException
import ar.com.kairoslp.tripter.service.ExistingUserWithEmailException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class TripterServiceErrorAdvice {

    @ExceptionHandler(EntityNotLatestVersionException::class, ExistingUserWithEmailException::class)
    fun handle(req: HttpServletRequest, httpResponse: HttpServletResponse, e: Exception) {
        httpResponse.sendError(HttpStatus.CONFLICT.value(), e.message)
    }

}