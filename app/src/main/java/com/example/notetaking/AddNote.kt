package com.example.notetaking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notetaking.DataProcess.NoteInformationDataProcess
import com.example.notetaking.databinding.AddNoteBinding

class AddNote : AppCompatActivity() {
    lateinit var addNoteBinding: AddNoteBinding
    lateinit var noteInformationDataProcess: NoteInformationDataProcess
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNoteBinding = AddNoteBinding.inflate(layoutInflater)
        setContentView(addNoteBinding.root)

        val idExtra: String = intent.getStringExtra("id").toString()
        val titleExtra: String = intent.getStringExtra("title").toString()
        val messageExtra: String = intent.getStringExtra("message").toString()

        if (intent.hasExtra("id")) {
            addNoteBinding.titleViewItem.setText(titleExtra)
            addNoteBinding.messageViewItem.setText(messageExtra)
        }
        addNoteBinding.buttonSave.setOnClickListener {

            noteInformationDataProcess = NoteInformationDataProcess()
            val sharedPreferences =
                application.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

            var title = addNoteBinding.titleViewItem.text.toString()
            var message = addNoteBinding.messageViewItem.text.toString()
            var id = sharedPreferences.all.size

            if (title.isEmpty()) {
                addNoteBinding.titleViewItem.error = "Please Enter Title"
                addNoteBinding.titleViewItem.requestFocus()
            } else {
                if (intent.hasExtra("id"))
                    noteInformationDataProcess.saveInformationData(
                        applicationContext,
                        idExtra.toInt(),
                        title,
                        message
                    )
                else
                    noteInformationDataProcess.saveInformationData(
                        applicationContext,
                        id,
                        title,
                        message
                    )
                Toast.makeText(applicationContext, "Saved...", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}