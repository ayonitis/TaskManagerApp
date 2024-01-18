package com.creativeinstitute.mytaskmanagerapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.creativeinstitute.mytaskmanagerapp.CardWork.Adapter
import com.creativeinstitute.mytaskmanagerapp.CardWork.CardInfo
import com.creativeinstitute.mytaskmanagerapp.CardWork.create_card
import com.creativeinstitute.mytaskmanagerapp.Database.DataObject
import com.creativeinstitute.mytaskmanagerapp.Database.myDatabase
import com.creativeinstitute.mytaskmanagerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var adapter: Adapter
    private lateinit var database: myDatabase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchView = binding.searchView

        // Database initialization...
        database = Room.databaseBuilder(
            applicationContext, myDatabase::class.java, name = "To_Do"
        ).build()

        binding.add.setOnClickListener {
            val intent = Intent(this, create_card::class.java)
            startActivity(intent)
        }

        binding.deleteAll.setOnClickListener {
            DataObject.deleteAll()
            GlobalScope.launch {
                database.dao().deleteAll()
            }
            setRecycler()
        }

        setRecycler()

        // SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        // Ensure that the SearchView is clickable and focusable
        searchView.isClickable = true
        searchView.isFocusable = true
        searchView.isFocusableInTouchMode = true
        searchView.setIconifiedByDefault(false)
    }

    private fun setRecycler() {
        adapter = Adapter(DataObject.getAllData())
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }
}
