package com.creativeinstitute.mytaskmanagerapp.CardWork

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.room.Room
import com.creativeinstitute.mytaskmanagerapp.Database.DataObject
import com.creativeinstitute.mytaskmanagerapp.Database.Entity
import com.creativeinstitute.mytaskmanagerapp.MainActivity
import com.creativeinstitute.mytaskmanagerapp.databinding.ActivityCreateCard2Binding
import com.creativeinstitute.mytaskmanagerapp.Database.myDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class create_card : AppCompatActivity() {
    lateinit var binding: ActivityCreateCard2Binding

    private lateinit var btnDatePicker: ImageButton

    //Database
    private lateinit var database: myDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCreateCard2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnDatePicker = binding.calendarButton
        val myCalendar = Calendar.getInstance()
        val datepicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        binding.calendarButton.setOnClickListener {
            DatePickerDialog(this, datepicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        //Database
        database = Room.databaseBuilder(
            applicationContext, myDatabase::class.java, name = "To_Do"
        ).build()

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
    }

    private fun saveTask() {
        val title = binding.createTitle.text.toString().trim()
        val description = binding.createDescription.text.toString().trim()
        val priority = binding.createPriority.text.toString().trim()

        val validPriorities = listOf("Completed", "Soon", "Pending")

        if (title.isNotBlank() && description.isNotBlank() && priority.isNotBlank() && priority in validPriorities) {
            // Fields are not empty, and priority is one of the valid values
            DataObject.setData(title, description, priority)

            GlobalScope.launch {
                database.dao().insertTask(Entity(0, title, description, priority))
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // Display a Toast message indicating that fields cannot be empty or priority is invalid
            Toast.makeText(
                this,
                "Invalid or empty fields. Please check your input.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
