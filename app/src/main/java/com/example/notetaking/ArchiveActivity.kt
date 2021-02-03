package com.example.notetaking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notetaking.databinding.LayoutArchiveBinding

class ArchiveActivity : AppCompatActivity() {

    lateinit var archiveBinding: LayoutArchiveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        archiveBinding = LayoutArchiveBinding.inflate(layoutInflater)
        setContentView(archiveBinding.root)




    }
}