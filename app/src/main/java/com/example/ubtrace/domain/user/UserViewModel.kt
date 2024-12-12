package com.example.ubtrace.domain.user;

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.ubtrace.data.user.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class UserViewModel : ViewModel(){

    fun getUserbyId(
        context : Context,
        userUid: String,
        data: (User) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("users")
            .document(userUid)

        try {
            fireStoreRef.get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val user = it.toObject<User>()!!
                        data(user)

                    } else{
                        Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getNamebyId(
        context : Context,
        userUid: String,
        data: (String) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("users")
            .document(userUid)

        try {
            fireStoreRef.get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val name = it.getString("name").toString()
                        data(name)

                    } else{
                        Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveUser(
        user: User,
        context : Context,
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(user.userUid)

        try {
            fireStoreRef.set(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getNoTelpbyId(
        context : Context,
        userUid : String,
        data: (String) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("users")
            .document(userUid)

        try {
            fireStoreRef.get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val noTelp = it.getString("noTelp").toString()
                        data(noTelp)

                    } else{
                        Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
