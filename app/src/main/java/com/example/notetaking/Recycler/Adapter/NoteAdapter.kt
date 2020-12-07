package com.example.notetaking.Recycler.Adapter

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaking.AddNote
import com.example.notetaking.DataHolder.InformationDataClassDelete
import com.example.notetaking.DataHolder.NoteInformationDataClass
import com.example.notetaking.MainActivity
import com.example.notetaking.R
import com.example.notetaking.Recycler.DataHolder.NoteViewHolder

//Use Interface For Delete And Edit Data
interface PassDataForProcess {

    fun deleteData(specificDataKey: Int, specificDataPosition: Int)

    fun editData(
        specificKeyPosition: Int,
        imageView: ImageView,
        tickItem: ImageView
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
    val arrayListData: ArrayList<NoteInformationDataClass> = ArrayList<NoteInformationDataClass>()

    //Array List For Delete And Edit
    val deleteAndEditArrayList: ArrayList<InformationDataClassDelete> = ArrayList<InformationDataClassDelete>()

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

        holder.titleView.text = arrayListData[position].title

        holder.messageView.text = arrayListData[position].message

        holder.currentDateItem.text = arrayListData[position].currentDate

        holder.categorizedView.text = arrayListData[position].categorizedData

        holder.tickItem.visibility = View.INVISIBLE

        holder.imageNote.colorFilter=null

        holder.imageNote.setOnLongClickListener {

            if (deleteAndEditArrayList.size==0) {

                passDataForProcess.deleteData(arrayListData[position].id, position)

                passDataForProcess.editData(position,holder.imageNote,holder.tickItem)

                holder.imageNote.setColorFilter(R.color.redTransparent,PorterDuff.Mode.DST_OUT)

                //deleteAndEditArrayList.add(arrayListData[position].id)
                deleteAndEditArrayList.add(InformationDataClassDelete(arrayListData[position].id,position))

                holder.tickItem.visibility = View.VISIBLE

            }

            true
        }


        holder.imageNote.setOnClickListener {

            if (holder.tickItem.isVisible) {

                holder.imageNote.colorFilter = null

                holder.tickItem.visibility = View.INVISIBLE

                //deleteAndEditArrayList.remove(arrayListData[position].id)

                deleteAndEditArrayList.remove(InformationDataClassDelete(arrayListData[position].id,position))

                if (deleteAndEditArrayList.size == 1) {

                    context.mainBinding.editItemView.visibility = View.VISIBLE

                }

                if (deleteAndEditArrayList.size == 0) {

                    context.mainBinding.editItemView.visibility = View.INVISIBLE

                    context.mainBinding.deleteItemView.visibility = View.INVISIBLE

                }

            } else {

                if (deleteAndEditArrayList.size == 0) {

                    val intent: Intent = Intent(context, AddNote::class.java)

                    val s=arrayListData[position].id.toString()
                    intent.putExtra("idExtra", arrayListData[position].id.toString())

                    intent.putExtra("title", arrayListData[position].title)

                    intent.putExtra("message", arrayListData[position].message)

                    intent.putExtra("categorized", arrayListData[position].categorizedData)

                    context.startActivity(intent)

                } else {


                    holder.tickItem.visibility = View.VISIBLE

                    //deleteAndEditArrayList.add(arrayListData[position].id)
                    deleteAndEditArrayList.add(InformationDataClassDelete(arrayListData[position].id,position))


                    holder.imageNote.setColorFilter(R.color.redTransparent,PorterDuff.Mode.DST_OUT)

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
