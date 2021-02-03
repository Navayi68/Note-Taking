package com.example.notetaking.RoomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DataBaseModel::class],exportSchema = false, version = 1)

abstract class DataBaseInterFace : RoomDatabase() {

    abstract fun initializeDataAccessObject() : DataBaseDAO

}