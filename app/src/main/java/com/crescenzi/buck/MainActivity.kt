package com.crescenzi.buck

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.crescenzi.buck.presentation.MainScreen
import com.crescenzi.buck.presentation.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // == ALLOW NETWORK ON MAIN THREAD == //
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
    }
}
