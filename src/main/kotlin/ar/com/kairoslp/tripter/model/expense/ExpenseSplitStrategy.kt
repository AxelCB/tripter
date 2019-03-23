package ar.com.kairoslp.tripter.model.expense

import ar.com.kairoslp.tripter.model.User

interface ExpenseSplitStrategy {
    fun splitExpenseBetween(expense: Expense, users: List<User>)
}