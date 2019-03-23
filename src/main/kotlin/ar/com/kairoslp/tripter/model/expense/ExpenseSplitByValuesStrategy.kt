package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User

class ExpenseSplitByValuesStrategy: ExpenseSplitStrategy {
    var valuesByUser: List<ValueByUser> = ArrayList()

    override fun splitExpenseBetween(expense: Expense, users: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}