package com.example.testtaskgit.intent

sealed class ClientIntent {
    data object LoadClientData : ClientIntent()
    data object LoadLocation : ClientIntent()
}