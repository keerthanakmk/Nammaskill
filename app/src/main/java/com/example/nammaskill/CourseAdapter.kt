package com.example.nammaskill

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class CourseAdapter(
    private val courseList: List<Course>
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val courseImage: ImageView =
            itemView.findViewById(R.id.courseImage)

        val courseName: TextView =
            itemView.findViewById(R.id.courseName)

        val courseCategory: TextView =
            itemView.findViewById(R.id.courseCategory)

        val courseLocation: TextView =
            itemView.findViewById(R.id.courseLocation)

        val applyBtn: Button =
            itemView.findViewById(R.id.applyBtn)

        val btnInterested: Button =
            itemView.findViewById(R.id.btnInterested)

        val btnLocation: Button =
            itemView.findViewById(R.id.btnLocation)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)

        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        position: Int
    ) {

        val course = courseList[position]

        holder.courseName.text = course.name
        holder.courseCategory.text = course.category
        holder.courseLocation.text = course.location

        // SET COURSE IMAGE

        if (course.name.contains("Android", true)
            || course.category.contains("Coding", true)
        ) {

            holder.courseImage.setImageResource(
                R.drawable.coding
            )

        } else if (
            course.name.contains("Electrician", true)
            || course.category.contains("Technical", true)
        ) {

            holder.courseImage.setImageResource(
                R.drawable.electrician
            )

        } else if (
            course.name.contains("Tailoring", true)
            || course.category.contains("Skill", true)
        ) {

            holder.courseImage.setImageResource(
                R.drawable.tailoring
            )

        } else {

            holder.courseImage.setImageResource(
                R.drawable.coding
            )
        }

        // OPEN COURSE DETAILS

        holder.itemView.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                CourseDetailsActivity::class.java
            )

            intent.putExtra(
                "courseName",
                course.name
            )

            intent.putExtra(
                "courseCategory",
                course.category
            )

            intent.putExtra(
                "courseLocation",
                course.location
            )

            holder.itemView.context
                .startActivity(intent)
        }

        // APPLY BUTTON

        holder.applyBtn.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                ApplyActivity::class.java
            )

            intent.putExtra(
                "courseName",
                course.name
            )

            holder.itemView.context
                .startActivity(intent)
        }

        // INTERESTED BUTTON

        holder.btnInterested.setOnClickListener {

            val database = FirebaseDatabase
                .getInstance()
                .getReference("InterestedUsers")

            val interestId = database.push().key!!

            val interestData = HashMap<String, String>()

            interestData["courseName"] = course.name
            interestData["status"] = "Interested"

            database.child(interestId)
                .setValue(interestData)

            Toast.makeText(
                holder.itemView.context,
                "Trainer will contact you soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        // LOCATION BUTTON

        holder.btnLocation.setOnClickListener {

            val gmmIntentUri = Uri.parse(
                "geo:0,0?q=${course.location}"
            )

            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                gmmIntentUri
            )

            mapIntent.setPackage(
                "com.google.android.apps.maps"
            )

            holder.itemView.context
                .startActivity(mapIntent)
        }
    }

    override fun getItemCount(): Int {

        return courseList.size
    }
}