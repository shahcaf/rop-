package com.example.smartexpensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartexpensetracker.data.ExpenseCategory
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: ExpenseViewModel, onSaved: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategory.FOOD.name) }
    var expanded by remember { mutableStateOf(false) }
    var isIncome by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Transaction", style = MaterialTheme.typography.headlineMedium)

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = !isIncome,
                onClick = { isIncome = false; category = ExpenseCategory.FOOD.name },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                label = { Text("Expense") }
            )
            SegmentedButton(
                selected = isIncome,
                onClick = { isIncome = true; category = "Salary" },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                label = { Text("Income") }
            )
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
            label = { Text("Amount ($)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ExpenseCategory.values().forEach { cat ->
                    DropdownMenuItem(
                        text = { Text("${cat.icon} ${cat.name}") },
                        onClick = {
                            category = cat.name
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = if (datePickerState.selectedDateMillis != null) dateFormatter.format(Date(datePickerState.selectedDateMillis!!)) else "Select Date",
            onValueChange = {},
            label = { Text("Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && amt > 0) {
                    viewModel.addExpense(
                        amount = amt,
                        category = category,
                        note = note,
                        date = datePickerState.selectedDateMillis ?: System.currentTimeMillis(),
                        isIncome = isIncome
                    )
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = amount.isNotEmpty()
        ) {
            Text("Save Expense")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
