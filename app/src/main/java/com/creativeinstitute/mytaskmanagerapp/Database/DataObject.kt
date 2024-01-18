package com.creativeinstitute.mytaskmanagerapp.Database

import com.creativeinstitute.mytaskmanagerapp.CardWork.CardInfo
import java.util.Calendar

object DataObject {
    var listdata = mutableListOf<CardInfo>()

    fun setData(title: String, description: String, priority: String){
        listdata.add(CardInfo(title,description,priority))
    }

    fun getAllData(): List<CardInfo> {
        return listdata
    }

    fun deleteAll(){
        listdata.clear()
    }

    fun getData(pos:Int): CardInfo {
        return listdata[pos]
    }

    fun deleteData(pos:Int){
        listdata.removeAt(pos)
    }

    fun updateData(
        pos: Int,
        title: String,
        description: String,
        priority: String,
    ) {
        listdata[pos].title = title
        listdata[pos].description = description
        listdata[pos].priority = priority
    }
}