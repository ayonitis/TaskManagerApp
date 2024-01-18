package com.creativeinstitute.mytaskmanagerapp.CardWork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.creativeinstitute.mytaskmanagerapp.Database.DataObject
import com.creativeinstitute.mytaskmanagerapp.MainActivity
import com.creativeinstitute.mytaskmanagerapp.databinding.ActivitySplashScreenBinding
import com.creativeinstitute.mytaskmanagerapp.Database.myDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    private lateinit var database: myDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Database
        database = Room.databaseBuilder(
            applicationContext, myDatabase::class.java, name = "To_Do"
        ).build()

        GlobalScope.launch {
            DataObject.listdata = database.dao().getTask() as MutableList<CardInfo>
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}