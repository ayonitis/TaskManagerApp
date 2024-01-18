package com.creativeinstitute.mytaskmanagerapp.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.creativeinstitute.mytaskmanagerapp.Database.DAO
import com.creativeinstitute.mytaskmanagerapp.Database.Entity

@Database(entities = [Entity::class], version =1 )
abstract class myDatabase: RoomDatabase() {
    abstract fun dao(): DAO
}