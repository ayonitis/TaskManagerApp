package com.creativeinstitute.mytaskmanagerapp.CardWork

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.creativeinstitute.mytaskmanagerapp.Database.DataObject
import com.creativeinstitute.mytaskmanagerapp.Database.Entity
import com.creativeinstitute.mytaskmanagerapp.MainActivity
import com.creativeinstitute.mytaskmanagerapp.databinding.ActivityUpdateCardBinding
import com.creativeinstitute.mytaskmanagerapp.Database.myDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone

class UpdateCard : AppCompatActivity() {
    lateinit var binding: ActivityUpdateCardBinding
    var pos: Int = -1

    private lateinit var btnDatePicker: ImageButton

    private lateinit var database: myDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateCardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Calendar
        btnDatePicker = binding.calendarButton
        val myCalendar = Calendar.getInstance()
        val datepicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        binding.calendarButton.setOnClickListener {
            DatePickerDialog(
                this,
                datepicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //Database
        database = Room.databaseBuilder(
            applicationContext, myDatabase::class.java, name = "To_Do"
        ).build()

        pos = intent.getIntExtra("id", -1)
        if (pos != -1) {
            val title = DataObject.getData(pos).title
            val description = DataObject.getData(pos).description
            val priority = DataObject.getData(pos).priority

            binding.createTitle.setText(title)
            binding.createDescription.setText(description)
            binding.createPriority.setText(priority)

            binding.deletebutton.setOnClickListener {
                DataObject.deleteData(pos)

                GlobalScope.launch {
                    database.dao().deleteTask(
                        Entity(
                            pos + 1, binding.createTitle.text.toString(),
                            binding.createDescription.text.toString(),
                            binding.createPriority.text.toString()
                        )
                    )
                }

                myIntent()
            }

            binding.updatebutton.setOnClickListener {

                GlobalScope.launch {
                    database.dao().updateTask(
                        Entity(
                            pos + 1, binding.createTitle.text.toString(),
                            binding.createDescription.text.toString(),
                            binding.createPriority.text.toString()
                        )
                    )
                }

                updateTask()
            }
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

    }

    private fun updateTask() {
        val updatedTitle = binding.createTitle.text.toString()
        val updatedDescription = binding.createDescription.text.toString()
        val updatedPriority = binding.createPriority.text.toString().trim()
        val updatedCalendar = binding.calendarButton.toString()

        val validPriorities = listOf("Completed", "Soon", "Pending")

        if (updatedTitle.isNotBlank() && updatedDescription.isNotBlank() && updatedPriority.isNotBlank() && updatedPriority in validPriorities) {
            // Fields are not empty, and updatedPriority is one of the valid values
            DataObject.updateData(pos, updatedTitle, updatedDescription, updatedPriority)
            myIntent()
        } else {
            // Display a Toast message indicating that updated fields cannot be empty or the priority is invalid
            Toast.makeText(
                this,
                "Invalid or empty fields. Please check your input.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Move myIntent() function outside of the updatebutton.setOnClickListener block
    private fun myIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}






