package com.example.notetaking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.notetaking.DataProcess.NoteInformationDataProcess
import com.example.notetaking.databinding.AddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    lateinit var addNoteBinding: AddNoteBinding

    private val noteInformationDataProcess: NoteInformationDataProcess =
        NoteInformationDataProcess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNoteBinding = AddNoteBinding.inflate(layoutInflater)
        setContentView(addNoteBinding.root)

        //List For Add The Spinner
        val groups = arrayOf(
            "Uncategorized",
            "Office",
            "Personal",
            "Family affair",
            "Study",
            "Shopping",
            "Health",
            "Work"
        )

        //Add The Spinner
        val arraySpinner = ArrayAdapter(
            this@AddNote, android.R.layout.simple_spinner_dropdown_item, groups
        )

        addNoteBinding.spinnerView.adapter = arraySpinner

        val idExtra = intent.getStringExtra("idExtra")

        if (intent.hasExtra("idExtra")) {

            val readDataForExtra =noteInformationDataProcess.searchById(this@AddNote,idExtra.toString())

            val titleExtra = readDataForExtra[0].title

            val messageExtra = readDataForExtra[0].message

            val categorizedExtra = readDataForExtra[0].categorizedData

            var groupId: Int = 0
            when (categorizedExtra) {
                "Uncategorized" -> groupId = 0
                "Office" -> groupId = 1
                "Personal" -> groupId = 2
                "Family affair" -> groupId = 3
                "Study" -> groupId = 4
                "Shopping" -> groupId = 5
                "Health" -> groupId = 6
                "Work" -> groupId = 7
            }

            addNoteBinding.titleViewItem.setText(titleExtra)

            addNoteBinding.messageViewItem.setText(messageExtra)

            addNoteBinding.spinnerView.setSelection(groupId)

        }

        addNoteBinding.buttonSave.setOnClickListener {

            val sharedPreferences =
                application.getSharedPreferences("AllNote", Context.MODE_PRIVATE)

            val title = addNoteBinding.titleViewItem.text.toString()

            val message = addNoteBinding.messageViewItem.text.toString()

            val categorized = addNoteBinding.spinnerView.selectedItem.toString()

            val dateTime = SimpleDateFormat("dd/M/yyyy\nhh:mm:ss")
            val currentDate = dateTime.format(Date())

            val id=System.currentTimeMillis().toString()

            if (title.isEmpty()) {

                addNoteBinding.titleViewItem.error = "Please Enter Title"

                addNoteBinding.titleViewItem.requestFocus()

            } else {

                if (intent.hasExtra("idExtra"))

                    noteInformationDataProcess.saveInformationData(
                        applicationContext,
                        idExtra!!,
                        title,
                        message,
                        currentDate,
                        categorized
                    )
                else

                    noteInformationDataProcess.saveInformationData(
                        applicationContext,
                        id,
                        title,
                        message,
                        currentDate,
                        categorized
                    )

                Toast.makeText(applicationContext, "Saved...", Toast.LENGTH_SHORT).show()

                finish()
            }

        }

    }

}