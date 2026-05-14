package com.example.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartexpensetracker.data.Expense
import com.example.smartexpensetracker.data.ExpenseDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {

    val allExpenses: StateFlow<List<Expense>> = dao.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _budget = MutableStateFlow(0.0) // Start balance at 0
    val budget: StateFlow<Double> = _budget.asStateFlow()

    val totalBalance: StateFlow<Double> = allExpenses.map { list ->
        list.sumOf { if (it.isIncome) it.amount else -it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addExpense(amount: Double, category: String, note: String, date: Long, isIncome: Boolean = false) {
        viewModelScope.launch {
            dao.insertExpense(Expense(amount = amount, category = category, note = note, date = date, isIncome = isIncome))
        }
    }

    fun setBudget(newBudget: Double) {
        _budget.value = newBudget
    }

    private fun getStartOfMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }
    
    fun getCategoryBreakdown(expenses: List<Expense>): Map<String, Double> {
        return expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    fun exportExpensesToCsv(expenses: List<Expense>): String {
        val builder = StringBuilder()
        builder.append("Date,Category,Amount,Note\n")
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        expenses.forEach {
            builder.append("${sdf.format(java.util.Date(it.date))},${it.category},${it.amount},\"${it.note}\"\n")
        }
        return builder.toString()
    }
}
