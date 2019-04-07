package ar.com.kairoslp.tripter.restful.request

import ar.com.kairoslp.tripter.model.expense.ExpenseEquallySplitStrategy
import ar.com.kairoslp.tripter.model.expense.ExpenseSplitByPercentagesStrategy
import ar.com.kairoslp.tripter.model.expense.ExpenseSplitByValuesStrategy
import ar.com.kairoslp.tripter.model.expense.ExpenseSplitStrategy

enum class ExpenseSplitStrategyEnum {
    EQUALLY {
        override fun createStrategy(): ExpenseSplitStrategy {
            return ExpenseEquallySplitStrategy()
        }
    },
    BY_PERCENTAGES {
        override fun createStrategy(): ExpenseSplitStrategy {
            return ExpenseSplitByPercentagesStrategy()
        }
    },
    BY_VALUES {
        override fun createStrategy(): ExpenseSplitStrategy {
            return ExpenseSplitByValuesStrategy()
        }
    };

    abstract fun createStrategy(): ExpenseSplitStrategy
}