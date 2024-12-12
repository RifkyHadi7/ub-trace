package com.example.ubtrace.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String?>
    suspend fun register(email: String, password: String, username: String, noTelp: String): Result<String?>
}
