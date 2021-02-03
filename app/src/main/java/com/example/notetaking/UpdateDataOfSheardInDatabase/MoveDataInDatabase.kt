package com.example.notetaking.UpdateDataOfSheardInDatabase

import android.content.Context
import com.example.notetaking.RoomDataBase.DataBaseModel
import java.io.File

class MoveDataInDatabase {

    fun dataTransfer(context: Context): List<DataBaseModel> {

        val allList : ArrayList<DataBaseModel> = ArrayList<DataBaseModel>()

        val getData = context.getSharedPreferences("id", Context.MODE_PRIVATE)

        println(">>> Move")
        getData.all.keys.forEach { key ->
            val allData = getData.getString(key, null)

            allData?.let {

                val split = it.split("|")

                val id = split[0]
                val title = split[1]
                val message = split[2]
                val categorizedData = split[3]
                val currentDate = split[4]

                println(">>> MoveFor $title")

                allList.add(DataBaseModel(id,title,message,currentDate, categorizedData))
            }
        }

        println(">>> MoveSizeList ${allList.size}")
        return allList
    }

}