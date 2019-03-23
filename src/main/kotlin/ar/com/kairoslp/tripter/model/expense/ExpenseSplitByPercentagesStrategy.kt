package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User

class ExpenseSplitByPercentagesStrategy: ExpenseSplitStrategy {
    var percentagesByUser: List<PercentageByUser> = ArrayList()

    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}