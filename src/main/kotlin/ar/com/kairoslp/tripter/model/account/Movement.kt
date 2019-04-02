package ar.com.kairoslp.tripter.model.account

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Movement(var amount: BigDecimal,
                        @ManyToOne var origin: UserAccountForTrip? = null,
                        @ManyToOne var destination: UserAccountForTrip? = null,
                        @Id @GeneratedValue val id: Long? = null)