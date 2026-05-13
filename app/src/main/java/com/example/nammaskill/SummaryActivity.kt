package com.example.nammaskill

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val txtName = findViewById<TextView>(R.id.txtName)
        val txtCourse = findViewById<TextView>(R.id.txtCourse)
        val txtQualification = findViewById<TextView>(R.id.txtQualification)

        val name = intent.getStringExtra("name")
        val course = intent.getStringExtra("course")
        val qualification = intent.getStringExtra("qualification")

        txtName.text = "Name: $name"
        txtCourse.text = "Course: $course"
        txtQualification.text = "Qualification: $qualification"
    }
}