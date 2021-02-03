package com.example.notetaking.RoomDataBase

import androidx.room.*

@Dao
interface DataBaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewNoteSuspend(vararg arrayOffDataBaseModel: DataBaseModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNoteSuspend(vararg arrayOffDataBaseModel: DataBaseModel)

    @Delete
    suspend fun deleteNoteSuspend(dataBaseModel: DataBaseModel)

    @Query("SELECT * FROM NoteData ORDER BY id ASC")
    suspend fun getAllNoteDataSuspend(): List<DataBaseModel>

    @Query("SELECT * FROM NoteData WHERE Title IN (:title) OR Message IN (:message)")
    suspend fun searchNoteByTitleOrMessageSuspend(title: String, message: String): List<DataBaseModel>


}