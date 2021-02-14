package com.example.notetaking

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.notetaking.DataProcess.CoroutinesProcess
import com.example.notetaking.Recycler.Adapter.NoteAdapter
import com.example.notetaking.Recycler.Adapter.PassDataForProcess
import com.example.notetaking.RoomDataBase.DataBaseInterFace
import com.example.notetaking.RoomDataBase.DataBaseModel
import com.example.notetaking.RoomDataBase.DataBaseName
import com.example.notetaking.Setting.Setting
import com.example.notetaking.UI.Gesture.GestureConstants
import com.example.notetaking.UI.Gesture.GestureListenerConstants
import com.example.notetaking.UI.Gesture.GestureListenerInterface
import com.example.notetaking.UI.Gesture.SwipeGestureListener
import com.example.notetaking.UpdateDataOfSheardInDatabase.MoveDataInDatabase
import com.example.notetaking.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), PassDataForProcess,
    GestureListenerInterface {

    lateinit var mainBinding: ActivityMainBinding

    lateinit var noteAdapter: NoteAdapter

    private val coroutinesProcess: CoroutinesProcess = CoroutinesProcess()

    private val swipeGestureListener: SwipeGestureListener by lazy {
        SwipeGestureListener(applicationContext, this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        dataTransfer()

        setLanguage()

        mainBinding.settingActionView.setOnClickListener {
            startActivity(Intent(this@MainActivity, Setting::class.java))
        }

        mainBinding.searchIconActionView.setOnClickListener {

            val animationLeftIn =
                AnimationUtils.loadAnimation(applicationContext, android.R.anim.slide_in_left)

            mainBinding.textSearchView.startAnimation(animationLeftIn)
            mainBinding.backActionView.startAnimation(animationLeftIn)
            mainBinding.addActionView.visibility = View.INVISIBLE
            mainBinding.textActionBar.visibility = View.INVISIBLE
            mainBinding.settingActionView.visibility = View.INVISIBLE
            mainBinding.searchIconActionView.visibility = View.INVISIBLE
            mainBinding.textSearchView.visibility = View.VISIBLE
            mainBinding.backActionView.visibility = View.VISIBLE
            mainBinding.textSearchView.setText("")
            mainBinding.textSearchView.requestFocus()

            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                mainBinding.textSearchView,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        mainBinding.backActionView.setOnClickListener {
            val animationLeftOut = AnimationUtils.loadAnimation(applicationContext, R.anim.left_out)
            val animationFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

            mainBinding.backActionView.startAnimation(animationLeftOut)
            mainBinding.textSearchView.startAnimation(animationLeftOut)

            mainBinding.textSearchView.visibility = View.INVISIBLE
            mainBinding.backActionView.visibility = View.INVISIBLE

            mainBinding.textActionBar.startAnimation(animationFadeIn)
            mainBinding.addActionView.visibility = View.VISIBLE
            mainBinding.textActionBar.visibility = View.VISIBLE
            mainBinding.settingActionView.visibility = View.VISIBLE
            mainBinding.searchIconActionView.visibility = View.VISIBLE

            mainBinding.textSearchView.setText("")
            closeKeyBoard()
        }


        mainBinding.textSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val search = mainBinding.textSearchView.text.toString()

                if (search.isEmpty()) {

                    CoroutineScope(Dispatchers.IO).launch {

                        noteAdapter.arrayListData.clear()
                        noteAdapter.arrayListData.addAll(
                            coroutinesProcess.getAllData(
                                applicationContext
                            )
                        )

                        withContext(Dispatchers.Main) {
                            noteAdapter.notifyDataSetChanged()
                        }
                    }

                } else {

                    val title = mainBinding.textSearchView.text.toString()

                    CoroutineScope(Dispatchers.IO).launch {
                        noteAdapter.arrayListData.clear()
                        noteAdapter.arrayListData.addAll(
                            coroutinesProcess.searchInData(this@MainActivity, title, title)
                        )

                        withContext(Dispatchers.Main) {
                            noteAdapter.notifyDataSetChanged()
                        }
                    }

                }

                noteAdapter.deleteAndEditArrayList.clear()
                mainBinding.deleteItemView.visibility = View.INVISIBLE
                mainBinding.editItemView.visibility = View.INVISIBLE

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mainBinding.buttonAdd.setOnClickListener {

//            val getData = getSharedPreferences("AllNotes", Context.MODE_PRIVATE)
//
//            getData.edit().apply {
//
//                putString("312313", "123456|a1|aaaaaaa|date2|work")
//                putString("31231", "12345|a2|bbbbbbb|date3|Uncategorized")
//                putString("31233", "1234|a3|ccccccc|date4|Office")
//                putString("31213", "123|a4|ddddddd|date5|Personal")
//                putString("31313", "12|a5|eeeeeee|date6|Study")
//                apply()
//            }
            startActivity(
                Intent(this, AddNote::class.java)
            )
            mainBinding.textSearchView.setText("")

        }

        mainBinding.addActionView.setOnClickListener {

            startActivity(Intent(this, AddNote::class.java))

        }

        noteAdapter = NoteAdapter(this, this)

        mainBinding.recyclerView.layoutManager = GridLayoutManager(
            this@MainActivity, 2, GridLayoutManager.VERTICAL, false
        )

        mainBinding.recyclerView.adapter = noteAdapter

    }

    private fun setLanguage() {

        val sharedPreferences = getSharedPreferences("SettingFile", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("language", null)

        if (language == "Fn") {

            mainBinding.textActionBar.setText(R.string.text_Action_Main_fn)
            mainBinding.textSearchView.setHint(R.string.search_hint_fn)

        } else {

            mainBinding.textActionBar.setText(R.string.text_Action_Main)
            mainBinding.textSearchView.setHint(R.string.search_hint)

        }
    }

    private fun dataTransfer() {

        val moveDataInDatabase = MoveDataInDatabase()

        val getData = getSharedPreferences("id", Context.MODE_PRIVATE)

        val listMove = moveDataInDatabase.dataTransfer(applicationContext)

        println(">>> MainUnderMoveSizeList ${listMove.size}")
        if (getData.all.isNotEmpty()) {


            val listData = moveDataInDatabase.dataTransfer(this)

            val widgetDataInterface = Room.databaseBuilder(
                applicationContext, DataBaseInterFace::class.java,
                DataBaseName
            ).build()

            for (index in listData.indices) {

                val title = listData[index].title
                val message = listData[index].message
                val currentDate = listData[index].currentDate
                val categorizedData = listData[index].categorizedData
                val id = System.currentTimeMillis().toString()

                println(">>> Main $title")
                val dataBaseModel = DataBaseModel(id, title, message, currentDate, categorizedData)

                CoroutineScope(Dispatchers.IO).launch {
                    widgetDataInterface.initializeDataAccessObject()
                        .insertNewNoteSuspend(dataBaseModel)

                    println(">>> Into Coroutine")
                    withContext(Dispatchers.Main) {
                        noteAdapter.notifyDataSetChanged()
                    }
                }
            }

            widgetDataInterface.close()

            val dialog = AlertDialog.Builder(this)

            dialog.apply {
                setTitle("Update Data")
                setMessage("Information updated")
                setCancelable(false)
                setPositiveButton("Ok") { _, _ ->
                    noteAdapter.notifyDataSetChanged()
                }
                show()
            }
            //getData.edit().clear().apply()
        }

    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            coroutinesProcess.loadDataNote(this@MainActivity)

            withContext(Dispatchers.Main) {

                noteAdapter.deleteAndEditArrayList.clear()

                mainBinding.buttonAdd.visibility = View.VISIBLE

                mainBinding.deleteItemView.visibility = View.INVISIBLE

                mainBinding.editItemView.visibility = View.INVISIBLE

                noteAdapter.notifyDataSetChanged()

                mainBinding.textActionBar.visibility = View.VISIBLE
                mainBinding.searchIconActionView.visibility = View.VISIBLE
                mainBinding.addActionView.visibility = View.VISIBLE
                mainBinding.textSearchView.visibility = View.INVISIBLE
                mainBinding.backActionView.visibility = View.INVISIBLE
                mainBinding.settingActionView.visibility = View.VISIBLE

            }
        }

        Handler(Looper.getMainLooper()).postDelayed({ closeKeyBoard() }, 500)

    }

    override fun deleteData(
        specificDataKey: String, specificDataPosition: Int,
        imageView: ImageView,
        tickItem: ImageView
    ) {

        mainBinding.deleteItemView.visibility = View.VISIBLE

        mainBinding.editItemView.visibility = View.VISIBLE

        mainBinding.searchIconActionView.visibility = View.INVISIBLE

        mainBinding.addActionView.visibility = View.INVISIBLE

        mainBinding.deleteItemView.setOnClickListener {

            val dialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

            dialog.apply {

                setTitle("Delete")

                setMessage("Are You Sure For Delete ?")

                setIcon(android.R.drawable.ic_delete)

                setCancelable(false)

                setPositiveButton("Yes") { _, _ ->

                    noteAdapter.deleteAndEditArrayList.sortByDescending {
                        it.position
                    }

                    val coroutinesProcess = CoroutinesProcess()

                    for (i in 0 until noteAdapter.deleteAndEditArrayList.size) {

                        var positionId = noteAdapter.deleteAndEditArrayList[i].position

                        val keyId = noteAdapter.deleteAndEditArrayList[i].key

                        if (noteAdapter.arrayListData.size == 1)

                            when {

                                positionId == 0 -> noteAdapter.arrayListData.removeAt(positionId)

                                positionId >= 1 -> {

                                    positionId = 1

                                    noteAdapter.arrayListData.removeAt(positionId - 1)

                                }
                            }
                        else
                            noteAdapter.arrayListData.removeAt(positionId)

                        coroutinesProcess.deleteNoteDataBase(applicationContext, keyId)

                    }

                    noteAdapter.deleteAndEditArrayList.clear()

                    noteAdapter.notifyDataSetChanged()

                    imageView.colorFilter = null

                    tickItem.visibility = View.INVISIBLE

                    mainBinding.buttonAdd.visibility = View.VISIBLE

                    mainBinding.deleteItemView.visibility = View.INVISIBLE

                    mainBinding.editItemView.visibility = View.INVISIBLE

                    mainBinding.searchIconActionView.visibility = View.VISIBLE

                    mainBinding.addActionView.visibility = View.VISIBLE

                }

                setNegativeButton("No") { _, _ -> }

                show()
            }
        }

    }

    override fun editData(
        specificKeyPosition: Int,
        imageView: ImageView,
        tickItem: ImageView
    ) {

        mainBinding.editItemView.setOnClickListener {

            val intent = Intent(this@MainActivity, AddNote::class.java)

            intent.putExtra("idExtra", noteAdapter.deleteAndEditArrayList[0].key)
            intent.putExtra("titleExtra", noteAdapter.deleteAndEditArrayList[0].title)
            intent.putExtra("messageExtra", noteAdapter.deleteAndEditArrayList[0].message)
            intent.putExtra(
                "categorizedExtra",
                noteAdapter.deleteAndEditArrayList[0].categorizedData
            )

            noteAdapter.deleteAndEditArrayList.clear()

            mainBinding.editItemView.visibility = View.INVISIBLE

            mainBinding.deleteItemView.visibility = View.INVISIBLE


            imageView.colorFilter = null

            tickItem.visibility = View.INVISIBLE

            mainBinding.buttonAdd.visibility = View.VISIBLE

            startActivity(intent)
        }


    }

    override fun addToArchive(listDataForArchive: DataBaseModel) {

//        val archiveAdapter = ArchiveAdapter(this)
//
//        mainBinding.archiveItemView.setOnClickListener {
//
//            listDataForArchive.id = System.currentTimeMillis().toString()
//            for (index in 0 until noteAdapter.deleteAndEditArrayList.size){
//                archiveAdapter.arrayListDataArchive.add(listDataForArchive)
//            }
//        }
    }

    override fun onSwipeGesture(
        gestureConstants: GestureConstants,
        downMotionEvent: MotionEvent,
        moveMotionEvent: MotionEvent,
        initVelocityX: Float,
        initVelocityY: Float
    ) {
        super.onSwipeGesture(
            gestureConstants,
            downMotionEvent,
            moveMotionEvent,
            initVelocityX,
            initVelocityY
        )

        when (gestureConstants) {
            is GestureConstants.SwipeVertical -> {
                when (gestureConstants.verticalDirection) {
                    GestureListenerConstants.SWIPE_DOWN -> {
                        println("*** Swipe Down ***")
                    }
                    GestureListenerConstants.SWIPE_UP -> {
                        println("*** Swipe Up ***")
                    }
                }
            }
            is GestureConstants.SwipeHorizontal -> {
                when (gestureConstants.horizontalDirection) {
                    GestureListenerConstants.SWIPE_LEFT -> {
                        println("*** Swipe Left ***")
                        startActivity(Intent(this@MainActivity, AddNote::class.java))
                    }
                    GestureListenerConstants.SWIPE_RIGHT -> {
                        println("*** Swipe Right ***")

                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent?.let {
            swipeGestureListener.onTouchEvent(it)
        }

        return super.dispatchTouchEvent(motionEvent)
    }

    private fun closeKeyBoard() {

        val focusView = mainBinding.root

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)

    }

}