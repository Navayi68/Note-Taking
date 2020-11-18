package com.example.notetaking.Recycler.DataHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.data_holder_view_item.view.*

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rootItem:ConstraintLayout=view.rootItemView
    val deleteItem:ImageView=view.deleteItemView
    val titleView:TextView=view.textTitleView
    val messageView:TextView=view.textMessageView
}