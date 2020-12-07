package com.example.notetaking.DataProcess

import android.content.Context
import com.example.notetaking.DataHolder.NoteInformationDataClass
import com.example.notetaking.MainActivity

//Use Interface For Background And Foreground In Thread
interface AfterBackgroundProcess {

    fun notifyUserInterfaceForData()

}


class NoteInformationDataProcess {

    //Save Information Data In ShearedPreferences
    fun saveInformationData(
        context: Context,
        id: Int,
        title: String,
        message: String,
        currentDate: String,
        categorizedData: String
    ) {

        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

        sharedPreferences.edit().let {

            it.putString(
                id.toString(),
                "${id}|${title}|${message}|${currentDate}|${categorizedData}"
            )

            it.apply()

        }

    }


    //Read Information Data From List For Process
    fun loadInformationData(context: Context): List<NoteInformationDataClass> {

        val allListData: ArrayList<NoteInformationDataClass> = ArrayList<NoteInformationDataClass>()

        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

        sharedPreferences.all.keys.forEach { key ->

            val allData = sharedPreferences.getString(key, null)

            allData?.let {

                val splittedData = it.split("|")

                val id = splittedData[0].toInt()

                val title = splittedData[1]

                val message = splittedData[2]

                val currentDate = splittedData[3]

                val categorizedData = splittedData[4]

                allListData.add(
                    NoteInformationDataClass(
                        id, title, message, currentDate, categorizedData
                    )
                )

            }

        }

        return allListData.sortedByDescending { it.id }

    }

    //Search Item In Data List By Title
    fun searchInData(context: Context, searchTitle: String): List<NoteInformationDataClass> {

        val allData = loadInformationData(context)

        val searchResult = ArrayList<NoteInformationDataClass>()

        allData.forEach {

            if (it.title.contains(searchTitle))
                searchResult.add(it)

        }

        return searchResult
    }

    //Search Item In Data List By Id
    fun searchById(context: Context,searchId:Int): List<NoteInformationDataClass>{
        val allData = loadInformationData(context)

        val searchResult = ArrayList<NoteInformationDataClass>()

        allData.forEach {

            if (it.id==searchId)
                searchResult.add(it)

        }

        return searchResult
    }

    //Delete Item From List
    fun deleteItemData(context: Context, keyRemovePosition: String) {

        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

        sharedPreferences.edit().remove(keyRemovePosition).apply()

    }

    //Prepare Data In Background To Add To The List
    fun setupAdapterData(mainActivity: MainActivity,afterBackgroundProcess: AfterBackgroundProcess) {

        //Background Process
        val loadProcess = Thread(Runnable {

            mainActivity.noteAdapter.arrayListData.clear()

            mainActivity.noteAdapter.arrayListData.addAll(
                loadInformationData(mainActivity)
            )

           afterBackgroundProcess.notifyUserInterfaceForData()

        })

        if (!loadProcess.isAlive) {

            loadProcess.start()

        }
    }

}
