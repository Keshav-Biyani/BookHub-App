package com.keshav.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBook(): List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id = :BookId")
    fun getBookById(BookId: String): BookEntity
}