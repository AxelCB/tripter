package ar.com.kairoslp.tripter.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ar.com.kairoslp.tripter"])
class TripterApplication

fun main(args: Array<String>) {
    runApplication<TripterApplication>(*args)
}
