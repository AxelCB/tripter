package ar.com.kairoslp.tripter.model.expense

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