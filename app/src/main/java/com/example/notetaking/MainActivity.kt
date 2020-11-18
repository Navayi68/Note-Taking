package com.example.notetaking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetaking.DataProcess.NoteInformationDataProcess
import com.example.notetaking.Recycler.Adapter.NoteAdapter
import com.example.notetaking.Recycler.Adapter.PassDataForDelete
import com.example.notetaking.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), PassDataForDelete {
    lateinit var mainBinding: ActivityMainBinding
    lateinit var noteAdapter: NoteAdapter
    private val noteInformationDataProcess = NoteInformationDataProcess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java))
        }
        noteAdapter = NoteAdapter(this, this)

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )

    }

    override fun onResume() {
        super.onResume()
        noteAdapter.arrayListData.clear()
        noteAdapter.arrayListData.addAll(
            noteInformationDataProcess.loadInformationData(applicationContext)
        )
        mainBinding.recyclerView.adapter = noteAdapter

    }

    override fun deleteData(specificDataKey: Int, specificDataPosition: Int) {
        noteAdapter.arrayListData.removeAt(specificDataPosition)
        noteInformationDataProcess.deleteItemData(this, specificDataKey.toString())
        noteAdapter.notifyDataSetChanged()
    }

}