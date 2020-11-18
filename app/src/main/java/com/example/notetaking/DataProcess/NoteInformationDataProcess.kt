package com.example.notetaking.DataProcess

import android.content.Context
import com.example.notetaking.DataHolder.NoteInformationDataClass

class NoteInformationDataProcess {

    fun saveInformationData(context: Context, id: Int, title: String, message: String) {

        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

        sharedPreferences.edit().let {
            it.putString(id.toString(), "${id}|${title}|${message}")
            it.apply()
        }
    }

    fun loadInformationData(context: Context): ArrayList<NoteInformationDataClass> {

        val allListData: ArrayList<NoteInformationDataClass> = ArrayList<NoteInformationDataClass>()
        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

        sharedPreferences.all.keys.forEach { key ->
            val allData = sharedPreferences.getString(key, null)

            allData?.let {
                val splittedData = it.split("|")

                val id = splittedData[0].toInt()
                val title = splittedData[1]
                val message = splittedData[2]

                allListData.add(NoteInformationDataClass(id, title, message))
            }

        }

        return allListData
    }

    fun deleteItemData(context: Context, keyRemovePosition:String){
        val sharedPreferences = context.getSharedPreferences("AllNote", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(keyRemovePosition).apply()
    }

}
