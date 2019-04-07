package ar.com.kairoslp.tripter.model.account

import ar.com.kairoslp.tripter.model.expense.Expense
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class ExpenseDebt(amount: BigDecimal,
                  origin: UserAccountForTrip, destination: UserAccountForTrip,
                  @ManyToOne var expense: Expense): Movement(amount, origin, destination)