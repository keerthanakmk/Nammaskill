package com.example.nammaskill

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSuccess: Button
    private lateinit var etSearch: EditText

    private lateinit var adapter: CourseAdapter

    private val courseList = ArrayList<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnSuccess = findViewById(R.id.btnSuccess)
        etSearch = findViewById(R.id.etSearch)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CourseAdapter(courseList)

        recyclerView.adapter = adapter

        // SUCCESS STORIES BUTTON

        btnSuccess.setOnClickListener {

            val intent = Intent(
                this,
                SuccessActivity::class.java
            )

            startActivity(intent)
        }

        // SEARCH FEATURE

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val searchText = s.toString().trim()

                val filteredList = courseList.filter {

                    it.name.contains(
                        searchText,
                        ignoreCase = true
                    ) ||

                            it.category.contains(
                                searchText,
                                ignoreCase = true
                            ) ||

                            it.location.contains(
                                searchText,
                                ignoreCase = true
                            )
                }

                recyclerView.adapter =
                    CourseAdapter(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // FIREBASE DATABASE

        val database = FirebaseDatabase.getInstance()
            .getReference("courses")

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                courseList.clear()

                for (data in snapshot.children) {

                    val course = data.getValue(Course::class.java)

                    if (course != null) {

                        courseList.add(course)
                    }
                }

                Log.d(
                    "FIREBASE_DATA",
                    "Size: ${courseList.size}"
                )

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

                Log.e(
                    "FIREBASE_ERROR",
                    error.message
                )
            }
        })
    }
}