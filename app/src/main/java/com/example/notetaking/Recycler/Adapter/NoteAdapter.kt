package com.example.notetaking.Recycler.Adapter

import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.AddNote
import com.example.notetaking.DataHolder.InformationDataClassDelete
import com.example.notetaking.MainActivity
import com.example.notetaking.R
import com.example.notetaking.Recycler.DataHolder.NoteViewHolder
import com.example.notetaking.RoomDataBase.DataBaseModel

//Use Interface For Delete And Edit Data
interface PassDataForProcess {

    fun deleteData(
        specificDataKey: String,
        specificDataPosition: Int,
        imageView: ImageView,
        tickItem: ImageView
    )

    fun editData(
        specificKeyPosition: Int,
        imageView: ImageView,
        tickItem: ImageView
    )

    fun addToArchive(
        listDataForArchive: DataBaseModel
    )
}

//Categorized Data For Add to Spinner
object ViewType {
    const val TypeUncategorized = 0
    const val TypeOffice = 1
    const val TypePersonal = 2
    const val TypeFamily = 3
    const val TypeStudy = 4
    const val TypeShopping = 5
    const val TypeHealth = 6
    const val TypeWork = 7
}

class NoteAdapter(
    private val context: MainActivity,
    private val passDataForProcess: PassDataForProcess
) : RecyclerView.Adapter<NoteViewHolder>() {

    //Array List For All Data
    val arrayListData: ArrayList<DataBaseModel> = ArrayList<DataBaseModel>()

    //Array List For Delete And Edit
    val deleteAndEditArrayList: ArrayList<InformationDataClassDelete> =
        ArrayList<InformationDataClassDelete>()

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

    //Edit ViewType
    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return when (arrayListData[position].categorizedData) {

            "Uncategorized","دسته بندی نشده" -> ViewType.TypeUncategorized
            "Office","دفتر" -> ViewType.TypeOffice
            "Personal","شخصی" -> ViewType.TypePersonal
            "Family affair","خانوادگی" -> ViewType.TypeFamily
            "Study","مطالعه" -> ViewType.TypeStudy
            "Shopping","خرید" -> ViewType.TypeShopping
            "Health","سلامتی" -> ViewType.TypeHealth
            "Work","کار" -> ViewType.TypeWork
            else-> ViewType.TypeUncategorized

        }

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.titleView.text = arrayListData[position].title

        holder.messageView.text = arrayListData[position].message

        holder.currentDateItem.text = arrayListData[position].currentDate

        holder.categorizedView.text = arrayListData[position].categorizedData

        if (arrayListData[position].selectedItem) {

            holder.tickItem.visibility = View.VISIBLE

            holder.imageNote.setColorFilter(R.color.redTransparent, PorterDuff.Mode.DST_IN)
        } else {

            holder.tickItem.visibility = View.INVISIBLE

            holder.imageNote.colorFilter = null
        }

        holder.imageNote.setOnLongClickListener {

            if (deleteAndEditArrayList.size == 0) {

                if (holder.tickItem.isShown) {

                    arrayListData[position].selectedItem = false

                    holder.tickItem.visibility = View.INVISIBLE

                    holder.imageNote.colorFilter = null


                } else {

                    arrayListData[position].selectedItem = true

                    holder.tickItem.visibility = View.VISIBLE

                    holder.imageNote.setColorFilter(R.color.redTransparent, PorterDuff.Mode.DST_IN)
                }

                passDataForProcess.deleteData(
                    arrayListData[position].id,
                    position,
                    holder.imageNote,
                    holder.tickItem
                )

                passDataForProcess.addToArchive(DataBaseModel(
                    "",
                arrayListData[position].title,
                arrayListData[position].message,
                arrayListData[position].currentDate,
                arrayListData[position].categorizedData))
                passDataForProcess.editData(position, holder.imageNote, holder.tickItem)

                deleteAndEditArrayList.add(
                    InformationDataClassDelete(
                        arrayListData[position].id,
                        position,
                        arrayListData[position].title,
                        arrayListData[position].message,
                        arrayListData[position].categorizedData
                    )
                )

                holder.imageNote.setColorFilter(R.color.redTransparent, PorterDuff.Mode.DST_IN)

                holder.tickItem.visibility = View.VISIBLE

                context.mainBinding.buttonAdd.visibility = View.INVISIBLE

            }

            true
        }


        holder.imageNote.setOnClickListener {

            passDataForProcess.editData(position, holder.imageNote, holder.tickItem)

            if (holder.tickItem.isShown) {

                arrayListData[position].selectedItem = false

                holder.tickItem.visibility = View.INVISIBLE

                holder.imageNote.colorFilter = null

                deleteAndEditArrayList.remove(
                    InformationDataClassDelete(
                        arrayListData[position].id,
                        position,
                        arrayListData[position].title,
                        arrayListData[position].message,
                        arrayListData[position].categorizedData
                    )
                )

                if (deleteAndEditArrayList.size == 1)
                    context.mainBinding.editItemView.visibility = View.VISIBLE


                if (deleteAndEditArrayList.size == 0) {

                    context.mainBinding.editItemView.visibility = View.INVISIBLE

                    context.mainBinding.deleteItemView.visibility = View.INVISIBLE

                    if (!context.mainBinding.textSearchView.isVisible) {

                        context.mainBinding.searchIconActionView.visibility = View.VISIBLE

                        context.mainBinding.addActionView.visibility = View.VISIBLE

                    }

                    context.mainBinding.buttonAdd.visibility = View.VISIBLE

                }

            } else {

                if (deleteAndEditArrayList.size == 0) {

                    val intent = Intent(context, AddNote::class.java)

                    intent.putExtra("idExtra", arrayListData[position].id)

                    intent.putExtra("titleExtra", arrayListData[position].title)

                    intent.putExtra("messageExtra", arrayListData[position].message)

                    intent.putExtra("categorizedExtra", arrayListData[position].categorizedData)

                    context.startActivity(intent)

                } else {

                    arrayListData[position].selectedItem = true

                    holder.tickItem.visibility = View.VISIBLE

                    holder.imageNote.setColorFilter(R.color.redTransparent, PorterDuff.Mode.DST_IN)

                    deleteAndEditArrayList.add(
                        InformationDataClassDelete(
                            arrayListData[position].id,
                            position,
                            arrayListData[position].title,
                            arrayListData[position].message,
                            arrayListData[position].categorizedData
                        )
                    )

                    if (deleteAndEditArrayList.size > 1)
                        context.mainBinding.editItemView.visibility = View.INVISIBLE

                }
            }
        }
    }

    override fun getItemCount(): Int {

        return arrayListData.size

    }

}
