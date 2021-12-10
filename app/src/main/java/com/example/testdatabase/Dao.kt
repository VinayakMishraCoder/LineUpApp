package com.example.testdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Insert
    fun insert(note : Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note : Note)

    @Query("SELECT * FROM notes_table ORDER BY priority")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery ORDER BY priority")
    fun getNotes(searchQuery: String): LiveData<List<Note>>
}