package com.example.notetaking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.notetaking.databinding.ActivitySettingBinding

class Setting : AppCompatActivity() {

    lateinit var settingBinding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)




        settingBinding.backActivity.setOnClickListener {

            startActivity(Intent(this@Setting,MainActivity::class.java))

            finish()
        }

        settingBinding.colorView.setOnClickListener {
            settingBinding.colorPicker.visibility= View.VISIBLE
        }

        settingBinding.colorPicker.setOnColorChangedListener {
            settingBinding.colorView.setBackgroundColor(it)
        }
        settingBinding.colorPicker.setOnClickListener {
            settingBinding.colorPicker.visibility= View.INVISIBLE
        }
    }
}