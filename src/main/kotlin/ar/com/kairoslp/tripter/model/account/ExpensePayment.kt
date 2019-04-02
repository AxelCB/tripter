package ar.com.kairoslp.tripter.model.account

import ar.com.kairoslp.tripter.model.expense.Expense
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class ExpensePayment(amount: BigDecimal,
                     destination: UserAccountForTrip,
                     @ManyToOne var expense: Expense): Movement(amount, null, destination)