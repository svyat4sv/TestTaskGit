package com.example.testtaskgit.intent

import androidx.compose.ui.graphics.Color

data class ClientState(
    val clientName: String = "",
    val clientColor: Color = Color.Black,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

)