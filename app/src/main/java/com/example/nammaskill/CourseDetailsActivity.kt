package com.example.nammaskill

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CourseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        val detailsImage =
            findViewById<ImageView>(R.id.detailsImage)

        val detailsName =
            findViewById<TextView>(R.id.detailsName)

        val detailsCategory =
            findViewById<TextView>(R.id.detailsCategory)

        val detailsLocation =
            findViewById<TextView>(R.id.detailsLocation)

        val detailsDuration =
            findViewById<TextView>(R.id.detailsDuration)

        val detailsEligibility =
            findViewById<TextView>(R.id.detailsEligibility)

        val btnApplyNow =
            findViewById<Button>(R.id.btnApplyNow)

        // GET DATA

        val courseName =
            intent.getStringExtra("courseName")

        val category =
            intent.getStringExtra("courseCategory")

        val location =
            intent.getStringExtra("courseLocation")

        // SET TEXT

        detailsName.text = courseName
        detailsCategory.text = "Category: $category"
        detailsLocation.text = "Location: $location"

        // DYNAMIC DURATION & ELIGIBILITY

        if (category.equals("Coding", true)) {

            detailsDuration.text =
                "Duration: 6 Months"

            detailsEligibility.text =
                "Eligibility: PUC / Degree"

            detailsImage.setImageResource(
                R.drawable.coding
            )

        } else if (
            category.equals("Technical", true)
        ) {

            detailsDuration.text =
                "Duration: 4 Months"

            detailsEligibility.text =
                "Eligibility: SSLC"

            detailsImage.setImageResource(
                R.drawable.electrician
            )

        } else {

            detailsDuration.text =
                "Duration: 3 Months"

            detailsEligibility.text =
                "Eligibility: No Qualification Required"

            detailsImage.setImageResource(
                R.drawable.tailoring
            )
        }

        // APPLY BUTTON

        btnApplyNow.setOnClickListener {

            val intent = Intent(
                this,
                ApplyActivity::class.java
            )

            intent.putExtra(
                "courseName",
                courseName
            )

            startActivity(intent)
        }
    }
}