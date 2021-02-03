package com.example.notetaking.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.R
import com.example.notetaking.Recycler.DataHolder.NoteViewHolder
import com.example.notetaking.RoomDataBase.DataBaseModel

class ArchiveAdapter(val context: Context) : RecyclerView.Adapter<NoteViewHolder>() {

    val arrayListDataArchive: ArrayList<DataBaseModel> = ArrayList<DataBaseModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return when (viewType) {

            ViewType.TypeUncategorized ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_uncategorized, parent, false)
                )

            ViewType.TypeOffice ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_office, parent, false)
                )

            ViewType.TypePersonal ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_personal, parent, false)
                )

            ViewType.TypeFamily ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_family, parent, false)
                )

            ViewType.TypeStudy ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_study, parent, false)
                )

            ViewType.TypeShopping ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_shopping, parent, false)
                )

            ViewType.TypeHealth ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_health, parent, false)
                )

            else ->
                NoteViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.data_holder_view_work, parent, false)
                )
        }

    }


    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return when (arrayListDataArchive[position].categorizedData) {

            "Uncategorized" -> ViewType.TypeUncategorized
            "Office" -> ViewType.TypeOffice
            "Personal" -> ViewType.TypePersonal
            "Family affair" -> ViewType.TypeFamily
            "Study" -> ViewType.TypeStudy
            "Shopping" -> ViewType.TypeShopping
            "Health" -> ViewType.TypeHealth
            else -> ViewType.TypeWork

        }

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.titleView.text = arrayListDataArchive[position].title

        holder.messageView.text = arrayListDataArchive[position].message

        holder.currentDateItem.text = arrayListDataArchive[position].currentDate

        holder.categorizedView.text = arrayListDataArchive[position].categorizedData

    }

    override fun getItemCount(): Int {
        return arrayListDataArchive.size
    }
}