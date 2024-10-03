package com.example.testtaskgit.view

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskgit.intent.ClientIntent
import com.example.testtaskgit.intent.ClientState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ClientViewModel(application: Application) : AndroidViewModel(application) {

    // Состояние экрана
    private val _state = MutableStateFlow(ClientState())
    val state: StateFlow<ClientState> = _state

    private val intentChannel = Channel<ClientIntent>(Channel.UNLIMITED)

    private val context: Context = application.applicationContext
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is ClientIntent.LoadClientData -> loadClientData()

                }
            }
        }
    }

    fun sendIntent(intent: ClientIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun loadClientData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val clientName = "OperatorA"
            val clientColor = Color(0xFFFF5733)

            _state.value = _state.value.copy(
                clientName = clientName,
                clientColor = clientColor,
                isLoading = false
            )
        }
    }
}