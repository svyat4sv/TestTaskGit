package com.example.testtaskgit.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
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
                    is ClientIntent.LoadLocation -> loadUserLocation() // Обработка нового интента
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

    // Получение координат пользователя с помощью GMS (или HMS)
    @SuppressLint("MissingPermission")
    private fun loadUserLocation() {
        viewModelScope.launch {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        _state.value = _state.value.copy(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = "Ошибка получения координат")
            }
        }
    }
}