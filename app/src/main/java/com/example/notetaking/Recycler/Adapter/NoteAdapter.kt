package com.example.notetaking.Recycler.Adapter

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.AddNote
import com.example.notetaking.DataHolder.NoteInformationDataClass
import com.example.notetaking.R
import com.example.notetaking.Recycler.DataHolder.NoteViewHolder

interface PassDataForDelete {
    fun deleteData(specificDataKey: Int, specificDataPosition: Int)
}

class NoteAdapter(
    private val context: AppCompatActivity,
    private val passDataForDelete: PassDataForDelete
) : RecyclerView.Adapter<NoteViewHolder>() {

    val arrayListData: ArrayList<NoteInformationDataClass> = ArrayList<NoteInformationDataClass>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.data_holder_view_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.titleView.text = arrayListData[position].title
        holder.messageView.text = arrayListData[position].message

        holder.rootItem.setOnClickListener {
            val intent: Intent = Intent(context, AddNote::class.java)

            intent.putExtra("id", arrayListData[position].id.toString())
            intent.putExtra("title",arrayListData[position].title)
            intent.putExtra("message",arrayListData[position].message)
            context.startActivity(intent)
        }

        holder.deleteItem.setOnClickListener {

            val alert: AlertDialog.Builder = AlertDialog.Builder(context)

            alert.apply {
                setTitle("Delete")
                setMessage("Are Your Sure For Delete ?")
                setCancelable(false)
                setPositiveButton("Yes") { _, _ ->
                    passDataForDelete.deleteData(arrayListData[position].id, position)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                }

                setNegativeButton("No") { _, _ -> }
                show()
            }

            true
        }

    }

    override fun getItemCount(): Int {
        return arrayListData.size
    }
}