package com.example.notetaking

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notetaking.DataProcess.AfterBackgroundProcess
import com.example.notetaking.DataProcess.NoteInformationDataProcess
import com.example.notetaking.Recycler.Adapter.NoteAdapter
import com.example.notetaking.Recycler.Adapter.PassDataForProcess
import com.example.notetaking.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), PassDataForProcess, AfterBackgroundProcess {

    lateinit var mainBinding: ActivityMainBinding

    lateinit var noteAdapter: NoteAdapter

    private val noteInformationDataProcess = NoteInformationDataProcess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.textSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })


        mainBinding.textSearchView.addTextChangedListener {


            val searchItem = noteInformationDataProcess.searchInData(
                this@MainActivity, mainBinding.textSearchView.text.toString()
            )

            if (searchItem.isEmpty()) {

                //noteAdapter.arrayListData.clear()

                noteAdapter.notifyDataSetChanged()

            } else {

                //noteAdapter.arrayListData.clear()

                noteAdapter.arrayListData.addAll(
                    noteInformationDataProcess.searchInData(
                        this@MainActivity,
                        mainBinding.textSearchView.text.toString()
                    )
                )
                noteAdapter.notifyDataSetChanged()

            }

        }


        mainBinding.buttonAdd.setOnClickListener {

            startActivity(Intent(this, AddNote::class.java))

        }


        noteAdapter = NoteAdapter(this, this)

        mainBinding.recyclerView.layoutManager = GridLayoutManager(
            this@MainActivity, 2, GridLayoutManager.VERTICAL, false
        )


        mainBinding.recyclerView.adapter = noteAdapter


    }

    override fun onResume() {
        super.onResume()

        noteInformationDataProcess.setupAdapterData(
            this@MainActivity,
            this@MainActivity
        )

        noteAdapter.notifyDataSetChanged()

    }

    override fun deleteData(specificDataKey: Int, specificDataPosition: Int) {

        mainBinding.deleteItemView.visibility = View.VISIBLE

        mainBinding.editItemView.visibility = View.VISIBLE

        Toast.makeText(this,specificDataPosition.toString(),Toast.LENGTH_SHORT).show()
        mainBinding.deleteItemView.setOnClickListener {

            val dialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

            dialog.apply {

                setTitle("Delete")

                setMessage("Are You Sure For Delete ?")

                setIcon(android.R.drawable.ic_delete)

                setCancelable(false)

                setPositiveButton("Yes") { _, _ ->


                    for (i in 0 .. noteAdapter.deleteAndEditArrayList.size-1) {

                        val positionId = noteAdapter.deleteAndEditArrayList[i].position

                        val keyId = noteAdapter.deleteAndEditArrayList[i].key.toString()

                        noteAdapter.arrayListData.removeAt(positionId)

                        noteInformationDataProcess.deleteItemData(context, keyId)
                    }

                    noteAdapter.deleteAndEditArrayList.clear()
                    noteAdapter.notifyDataSetChanged()

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

            val intent: Intent = Intent(this@MainActivity, AddNote::class.java)

            intent.putExtra("idExtra", noteAdapter.deleteAndEditArrayList[0].toString())

            noteAdapter.deleteAndEditArrayList.clear()

            mainBinding.editItemView.visibility = View.INVISIBLE

            mainBinding.deleteItemView.visibility = View.INVISIBLE

            startActivity(intent)
        }


    }

    override fun notifyUserInterfaceForData() {

        //Foreground Process
        runOnUiThread {

            noteAdapter.notifyDataSetChanged()

        }

    }

}