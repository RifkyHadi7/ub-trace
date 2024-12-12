package com.example.ubtrace.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun login(email: String, password: String): Result<String?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, username: String, noTelp: String): Result<String?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")

            // Simpan user ke Firestore
            val userMap = mapOf(
                "userId" to userId,
                "username" to username,
                "email" to email,
                "noTelp" to noTelp
            )
            firestore.collection("users").document(userId).set(userMap).await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
