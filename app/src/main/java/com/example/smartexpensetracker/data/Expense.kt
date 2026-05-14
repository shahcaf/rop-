package com.example.smartexpensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long,
    val note: String = "",
    val isIncome: Boolean = false
)

enum class ExpenseCategory(val icon: String) {
    FOOD("🍔"),
    TRANSPORT("🚗"),
    SHOPPING("🛍️"),
    BILLS("📄"),
    ENTERTAINMENT("🎬"),
    HEALTH("💊"),
    OTHER("📦")
}
