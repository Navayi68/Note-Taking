package com.example.notetaking.DataProcess

import android.content.Context
import com.example.notetaking.MainActivity


class NoteInformationDataProcess() {

    fun saveInformationSetting(context: Context, textSize: Int) {

        val sharedPreferences = context.getSharedPreferences("SettingFile", Context.MODE_PRIVATE)

        sharedPreferences.edit().apply {
            putString("Size", textSize.toString())
            apply()
        }
    }
}
