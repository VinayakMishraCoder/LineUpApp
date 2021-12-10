package com.example.testdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class Note(var title: String,var description: String,var priority: Int,var time: String): Serializable
{
    // so that this code is compatible with every jvm
//    private val serialVersionUID = 1L

    @PrimaryKey(autoGenerate = true) var id = 0
}