package com.example.notetaking.DataProcess

import android.content.Context
import androidx.room.Room
import com.example.notetaking.MainActivity
import com.example.notetaking.RoomDataBase.DataBaseInterFace
import com.example.notetaking.RoomDataBase.DataBaseModel
import com.example.notetaking.RoomDataBase.DataBaseName
import kotlinx.coroutines.*


class CoroutinesProcess() {

    var listDatabase: List<DataBaseModel> = ArrayList<DataBaseModel>()

    suspend fun getAllData(context: Context): List<DataBaseModel> {

        val widgetDataInterface = Room.databaseBuilder(
            context,
            DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        val getAllList = widgetDataInterface.initializeDataAccessObject().getAllNoteDataSuspend()

        widgetDataInterface.close()

        return getAllList
    }


    //private val mainActivity = MainActivity()
    fun loadDataNote(context: MainActivity) = CoroutineScope(Dispatchers.IO).launch {

        val widgetDataInterface = Room.databaseBuilder(
            context,
            DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        listDatabase = widgetDataInterface.initializeDataAccessObject().getAllNoteDataSuspend()

        context.noteAdapter.arrayListData.clear()
        context.noteAdapter.arrayListData.addAll(getAllData(context))

        withContext(Dispatchers.Main) {
            context.noteAdapter.notifyDataSetChanged()
        }

        widgetDataInterface.close()
    }

    suspend fun searchInData(
        context: MainActivity,
        title: String,
        message: String
    ): List<DataBaseModel> {

        val widgetDataInterface = Room.databaseBuilder(
            context,
            DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        listDatabase = widgetDataInterface.initializeDataAccessObject()
            .searchNoteByTitleOrMessageSuspend(title, message)

        val searchResult = ArrayList<DataBaseModel>()

        getAllData(context).forEach {
            if (it.title.contains(title) || it.message.contains(message)) {
                searchResult.add(it)
            }
        }

        withContext(Dispatchers.Main) {

            context.noteAdapter.notifyDataSetChanged()
        }

        widgetDataInterface.close()

        return searchResult

    }


    fun saveNoteToDataBase(
        context: Context,
        title: String,
        message: String,
        currentDate: String,
        categorizedData: String
    ) = CoroutineScope(Dispatchers.IO).launch {

        val id = System.currentTimeMillis().toString()

        val dataBaseModel = DataBaseModel(id, title, message, currentDate, categorizedData)

        val widgetDataInterface = Room.databaseBuilder(
            context, DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        widgetDataInterface.initializeDataAccessObject().insertNewNoteSuspend(dataBaseModel)

        widgetDataInterface.close()
    }


    fun updateNoteToDataBase(
        context: Context,
        id: String,
        title: String,
        message: String,
        currentDate: String,
        categorizedData: String
    ) = CoroutineScope(Dispatchers.IO).launch {

        val widgetDataInterface = Room.databaseBuilder(
            context, DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        val dataBaseModel = DataBaseModel(id, title, message, currentDate, categorizedData)

        widgetDataInterface.initializeDataAccessObject().updateNoteSuspend(dataBaseModel)

        widgetDataInterface.close()
    }

    fun deleteNoteDataBase(
        context: Context,
        id: String
    ) = CoroutineScope(Dispatchers.IO).launch {

        val widgetDataInterface = Room.databaseBuilder(
            context,
            DataBaseInterFace::class.java,
            DataBaseName
        ).build()

        val dataBaseModel = DataBaseModel(id, "", "", "", "", false)

        widgetDataInterface.initializeDataAccessObject().deleteNoteSuspend(dataBaseModel)

        widgetDataInterface.close()
    }

}