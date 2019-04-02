package ar.com.kairoslp.tripter.model.account

import java.math.BigDecimal
import javax.persistence.Entity

@Entity
class DebtPayment(amount: BigDecimal,
                  origin: UserAccountForTrip,
                  destination: UserAccountForTrip): Movement(amount, origin, destination)