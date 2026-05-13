package com.example.nammaskill

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.FirebaseDatabase

class ApplyActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etQualification: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)

        // NOTIFICATION PERMISSION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etQualification = findViewById(R.id.etQualification)
        btnSubmit = findViewById(R.id.btnSubmit)

        val courseName = intent.getStringExtra("courseName")

        btnSubmit.setOnClickListener {

            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val qualification = etQualification.text.toString().trim()

            // EMPTY FIELD VALIDATION

            if (
                name.isEmpty()
                || phone.isEmpty()
                || (
                        !courseName.equals("Tailoring", true)
                                && qualification.isEmpty()
                        )
            ) {

                Toast.makeText(
                    this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // ELIGIBILITY VALIDATION

                val isEligible = when {

                    // ANDROID DEVELOPMENT

                    courseName.equals(
                        "Android Development",
                        true
                    ) ->

                        qualification.equals("PUC", true)
                                || qualification.equals("Degree", true)

                    // ELECTRICIAN TRAINING

                    courseName.equals(
                        "Electrician Training",
                        true
                    ) ->

                        qualification.equals("SSLC", true)

                    // TAILORING

                    courseName.equals(
                        "Tailoring",
                        true
                    ) -> true

                    else -> true
                }

                // NOT ELIGIBLE

                if (!isEligible) {

                    Toast.makeText(
                        this,
                        "You are not eligible for this course",
                        Toast.LENGTH_LONG
                    ).show()

                    return@setOnClickListener
                }

                // FIREBASE DATABASE

                val database = FirebaseDatabase.getInstance()
                    .getReference("Applications")

                // RANDOM APPLICATION ID

                val randomId = (1000..9999).random()

                val applicationId = "APP_$randomId"

                // APPLICATION DATA

                val applicationData = HashMap<String, String>()

                applicationData["name"] = name
                applicationData["phone"] = phone
                applicationData["qualification"] = qualification
                applicationData["courseName"] = courseName.toString()

                database.child(applicationId)
                    .setValue(applicationData)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Application Submitted",
                            Toast.LENGTH_SHORT
                        ).show()

                        // NOTIFICATION

                        val channelId = "nammaskill_channel"

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            val channel = NotificationChannel(
                                channelId,
                                "NammaSkill Notifications",
                                NotificationManager.IMPORTANCE_DEFAULT
                            )

                            val manager =
                                getSystemService(NotificationManager::class.java)

                            manager.createNotificationChannel(channel)
                        }

                        val builder = NotificationCompat.Builder(
                            this,
                            channelId
                        )
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("NammaSkill")
                            .setContentText(
                                "Application submitted successfully"
                            )
                            .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                            )

                        if (
                            ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {

                            NotificationManagerCompat.from(this)
                                .notify(1, builder.build())
                        }

                        // OPEN SUMMARY SCREEN

                        val intent = Intent(
                            this,
                            SummaryActivity::class.java
                        )

                        intent.putExtra("name", name)
                        intent.putExtra("course", courseName)
                        intent.putExtra(
                            "qualification",
                            qualification
                        )

                        startActivity(intent)
                    }
            }
        }
    }
}