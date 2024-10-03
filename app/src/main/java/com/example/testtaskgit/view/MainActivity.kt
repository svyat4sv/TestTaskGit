package com.example.testtaskgit.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testtaskgit.intent.ClientIntent
import com.example.testtaskgit.ui.theme.TestTaskGitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTaskGitTheme {
                ClientScreen()
            }
        }
    }
}

@Composable
fun ClientScreen() {
    val viewModel: ClientViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Отправляем интент для загрузки данных при первом отображении
    viewModel.sendIntent(ClientIntent.LoadClientData)


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.errorMessage != null -> {
                Text(text = "Ошибка: ${state.errorMessage}")
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Отображение имени клиента
                    Text(
                        text = state.clientName,
                        color = state.clientColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
