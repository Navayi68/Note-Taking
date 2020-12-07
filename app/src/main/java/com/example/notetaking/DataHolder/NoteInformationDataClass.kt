package com.example.notetaking.DataHolder

//Use Data For Process
data class NoteInformationDataClass(
    val id: Int,
    val title: String,
    val message: String,
    val currentDate: String,
    val categorizedData: String = "Uncategorized"
)

data class InformationDataClassDelete(
    val key: Int,
    val position: Int
)