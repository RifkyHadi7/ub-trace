package com.example.ubtrace.domain.report

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.ubtrace.data.report.Report
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReportViewModel: ViewModel() {

    fun addReport(
        report: Report,
        context: Context,
        noTelp: String,
        imageUri: Uri,
        onComplete: () -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            // Firebase Storage setup
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/${report.userUid}_${System.currentTimeMillis()}.jpg")

            // Upload image to Firebase Storage
            val uploadTask = storageRef.putFile(imageUri)

            // Get download URL after successful upload
            val downloadUrl = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                storageRef.downloadUrl
            }.await()

            // Set the image URL to the report object
            val updatedReport = report.copy(image = downloadUrl.toString(), noTelp = noTelp)

            // Firestore setup
            val firestore = FirebaseFirestore.getInstance()

            // Add the report to Firestore
            firestore.collection("reports")
                .add(updatedReport)
                .addOnSuccessListener { documentReference ->
                    val reportWithId = updatedReport.copy(id = documentReference.id)

                    // Update the report with the document ID if needed
                    firestore.collection("reports").document(documentReference.id)
                        .set(reportWithId)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Report successfully added with ID!", Toast.LENGTH_SHORT).show()
                            onComplete()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to update report with ID: ${e.message}", Toast.LENGTH_SHORT).show()
                            onComplete()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to add report: ${e.message}", Toast.LENGTH_SHORT).show()
                    onComplete()
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            onComplete()
        }
    }

    fun getReport(
        context: Context,
        data: (List<Report>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()
        try {
            // Fetch data from Firestore in IO context
            val reports = withContext(Dispatchers.IO) {
                val snapshot = firestore.collection("reports").get().await()
                snapshot.documents.mapNotNull { it.toObject<Report>() }
            }

            // Pass the data to the callback
            data(reports)

            // Show Toast on the main thread
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Loading Complete", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Show error Toast on the main thread
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}