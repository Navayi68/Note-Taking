package com.example.notetaking.Setting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notetaking.R
import com.example.notetaking.databinding.SettingLayoutBinding

class Setting : AppCompatActivity() {

    lateinit var settingLayoutBinding: SettingLayoutBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingLayoutBinding = SettingLayoutBinding.inflate(layoutInflater)
        setContentView(settingLayoutBinding.root)

        setLanguage()

        val groups = arrayOf(
            "English",
            "Persian"
        )

        val arrayAdapter = ArrayAdapter(this@Setting, R.layout.support_simple_spinner_dropdown_item,groups)

        settingLayoutBinding.spinnerLanguageView.adapter = arrayAdapter

        val sharedPreferences = getSharedPreferences("SettingFile", Context.MODE_PRIVATE)


        settingLayoutBinding.saveButton.setOnClickListener {

            val language = settingLayoutBinding.spinnerLanguageView.selectedItem.toString()

            var putString = "Fn"

            if (language=="English") {
                putString = "En"
            }

            sharedPreferences.edit().putString("language",putString).apply()

            setLanguage()

            Toast.makeText(this, "Save Setting", Toast.LENGTH_SHORT).show()
        }


        settingLayoutBinding.backActionView.setOnClickListener {

            finish()

        }

    }

    private fun setLanguage() {

        val sharedPreferences = getSharedPreferences("SettingFile", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("language",null)

        if (language=="Fn"){
            settingLayoutBinding.saveButton.setText(R.string.save_button_fn)
            settingLayoutBinding.textActionBar.setText(R.string.setting_fn)
        }else{
            settingLayoutBinding.saveButton.setText(R.string.save_button)
            settingLayoutBinding.textActionBar.setText(R.string.setting)
        }

    }


}