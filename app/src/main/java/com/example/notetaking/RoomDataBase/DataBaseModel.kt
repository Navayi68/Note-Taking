package com.example.notetaking.RoomDataBase

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


const val DataBaseName = "NoteData"

@Entity(tableName = DataBaseName)
data class DataBaseModel(
    @NonNull @PrimaryKey var id: String,


    @Nullable @ColumnInfo(name = "Title") var title: String,
    @Nullable @ColumnInfo(name = "Message") var message: String,
    @Nullable @ColumnInfo(name = "CurrentDate") var currentDate: String,
    @Nullable @ColumnInfo(name = "CategorizedData") var categorizedData: String,

    @NonNull @ColumnInfo(name = "SelectItem") var selectedItem: Boolean = false,

    )
