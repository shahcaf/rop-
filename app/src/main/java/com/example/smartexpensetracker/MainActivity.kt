package com.example.smartexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.smartexpensetracker.data.AppDatabase
import com.example.smartexpensetracker.ui.MainApp
import com.example.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.example.smartexpensetracker.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Install Splash Screen
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this)

        // Initialize Database and ViewModel (Simple DI for this example)
        val database = AppDatabase.getDatabase(this)
        val viewModel = ExpenseViewModel(database.expenseDao())

        setContent {
            SmartExpenseTrackerTheme {
                Surface {
                    MainApp(viewModel)
                }
            }
        }
    }
}
