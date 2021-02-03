package com.example.notetaking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notetaking.DataProcess.CoroutinesProcess
import com.example.notetaking.databinding.AddNoteBinding
import java.text.SimpleDateFormat
import java.util.*


class AddNote : AppCompatActivity() {

    lateinit var addNoteBinding: AddNoteBinding

    var switchSave = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNoteBinding = AddNoteBinding.inflate(layoutInflater)
        setContentView(addNoteBinding.root)

        //val b=BuildConfig.VERSION_NAME

        Handler(Looper.getMainLooper()).postDelayed({

            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                addNoteBinding.titleViewItem,
                InputMethodManager.SHOW_IMPLICIT
            )

            addNoteBinding.titleViewItem.requestFocus()

        }, 500)

        setLanguage()

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

        val groupsFn = arrayOf(
            "دسته بندی نشده",
            "دفتر",
            "شخصی",
            "خانوادگی",
            "مطالعه",
            "خرید",
            "سلامتی",
            "کار"
        )


        val arraySpinner: ArrayAdapter<String>

        //Add The Spinner
        arraySpinner = if (addNoteBinding.textActionBar.text == "Note") {

            ArrayAdapter(
                this@AddNote, R.layout.support_simple_spinner_dropdown_item, groups
            )
        } else {

            ArrayAdapter(
                this@AddNote, R.layout.support_simple_spinner_dropdown_item, groupsFn
            )
        }


        addNoteBinding.spinnerView.adapter = arraySpinner

        val idExtra = intent.getStringExtra("idExtra").toString()

        if (intent.hasExtra("idExtra")) {

            val titleExtra = intent.getStringExtra("titleExtra")

            val messageExtra = intent.getStringExtra("messageExtra")

            val categorizedExtra = intent.getStringExtra("categorizedExtra")

            var groupId = 0
            when (categorizedExtra) {
                "Uncategorized", "دسته بندی نشده" -> groupId = 0
                "Office", "دفتر" -> groupId = 1
                "Personal", "شخصی" -> groupId = 2
                "Family affair", "خانوادگی" -> groupId = 3
                "Study", "مطالعه" -> groupId = 4
                "Shopping", "خرید" -> groupId = 5
                "Health", "سلامتی" -> groupId = 6
                "Work", "کار" -> groupId = 7
            }

            addNoteBinding.titleViewItem.setText(titleExtra)

            addNoteBinding.messageViewItem.setText(messageExtra)

            addNoteBinding.spinnerView.setSelection(groupId)

        }

        addNoteBinding.saveButtonAction.setOnClickListener {


            val coroutinesProcess = CoroutinesProcess()

            val title = addNoteBinding.titleViewItem.text.toString()

            val message = addNoteBinding.messageViewItem.text.toString()

            val dateTime = SimpleDateFormat("dd/M/yyyy\nhh:mm:ss")
            val currentDate = dateTime.format(Date()).toString()

            val categorized = addNoteBinding.spinnerView.selectedItem.toString()

            if (message.isNotEmpty() || title.isNotEmpty()) {

                if (intent.hasExtra("idExtra")) {

                    coroutinesProcess.updateNoteToDataBase(
                        applicationContext,
                        idExtra,
                        title,
                        message,
                        currentDate,
                        categorized
                    )

                } else {

                    coroutinesProcess.saveNoteToDataBase(
                        applicationContext,
                        title,
                        message,
                        currentDate,
                        categorized
                    )
                }

                Toast.makeText(applicationContext, "Saved...", Toast.LENGTH_SHORT).show()

                switchSave = true

                finish()

            } else {

                finish()

            }

        }

        addNoteBinding.backActionView.setOnClickListener {

            finish()
        }

    }

    private fun setLanguage() {

        val sharedPreferences = getSharedPreferences("SettingFile", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("language",null)

        if (language=="Fn"){
            addNoteBinding.textActionBar.setText(R.string.text_Action_Add_fn)
            addNoteBinding.categorizedGroup.setText(R.string.category_fn)
            addNoteBinding.titleViewItem.setHint(R.string.title_add_note_fn)
            addNoteBinding.messageViewItem.setHint(R.string.message_add_note_fn)
        }else{
            addNoteBinding.textActionBar.setText(R.string.text_Action_Add)
            addNoteBinding.categorizedGroup.setText(R.string.category)
            addNoteBinding.titleViewItem.setHint(R.string.title_add_note)
            addNoteBinding.messageViewItem.setHint(R.string.message_add_note)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onPause() {
        super.onPause()

        if (!switchSave) {

            val coroutinesProcess = CoroutinesProcess()

            val title = addNoteBinding.titleViewItem.text.toString()

            val message = addNoteBinding.messageViewItem.text.toString()

            val categorized = addNoteBinding.spinnerView.selectedItem.toString()

            val dateTime = SimpleDateFormat("dd/M/yyyy\nhh:mm:ss")

            val currentDate = dateTime.format(Date())

            val idExtra = intent.getStringExtra("idExtra").toString()

            if (message.isNotEmpty() || title.isNotEmpty()) {

                if (intent.hasExtra("idExtra")) {

                    coroutinesProcess.updateNoteToDataBase(
                        applicationContext,
                        idExtra,
                        title,
                        message,
                        currentDate,
                        categorized
                    )

                } else {

                    coroutinesProcess.saveNoteToDataBase(
                        applicationContext,
                        title,
                        message,
                        currentDate,
                        categorized
                    )
                }
            }
        }
    }

}