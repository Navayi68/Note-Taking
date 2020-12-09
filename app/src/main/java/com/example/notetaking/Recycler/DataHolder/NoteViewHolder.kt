package com.example.notetaking.Recycler.DataHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.data_holder_view_uncategorized.view.*

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val rootItem: ConstraintLayout = view.rootViewItem

    val titleView: TextView = view.textTitleholder

    val messageView: TextView = view.textMessageHolder

    val tickItem: ImageView = view.imageTick

    val currentDateItem: TextView = view.textCurrentDate

    val categorizedView: TextView = view.textCategorizedView

}