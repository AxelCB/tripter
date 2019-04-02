package ar.com.kairoslp.tripter.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["ar.com.kairoslp.tripter.model"])
@EnableJpaRepositories(basePackages = ["ar.com.kairoslp.tripter.persistence.repository"])
@SpringBootApplication(scanBasePackages = ["ar.com.kairoslp.tripter"])
class TripterApplication

fun main(args: Array<String>) {
    runApplication<TripterApplication>(*args)
}
