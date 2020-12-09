package com.example.notetaking.DataHolder

//Use Data For Process
data class NoteInformationDataClass(
    val id: String,
    val title: String,
    val message: String,
    val currentDate: String,
    val categorizedData: String = "Uncategorized",
    var selectedItem: Boolean = false
)

data class InformationDataClassDelete(
    val key: String,
    val position: Int
)