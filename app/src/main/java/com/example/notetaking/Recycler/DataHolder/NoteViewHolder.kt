package com.example.notetaking.Recycler.DataHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.data_holder_view_uncategorized.view.textCategorizedView
import kotlinx.android.synthetic.main.data_holder_view_uncategorized.view.textCurrentDateView
import kotlinx.android.synthetic.main.data_holder_view_uncategorized.view.tickItemView
import kotlinx.android.synthetic.main.data_holder_view_work.view.*

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val rootItem: ConstraintLayout = view.rootItemView

    val imageNote:ImageView = view.imageNote

    val titleView: TextView = view.textTitleView

    val messageView: TextView = view.textMessageView

    val tickItem: ImageView = view.tickItemView

    val currentDateItem: TextView = view.textCurrentDateView

    val categorizedView: TextView = view.textCategorizedView

}