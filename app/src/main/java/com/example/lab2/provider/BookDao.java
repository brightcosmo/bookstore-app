package com.example.lab2.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Query("select * from books")
    LiveData<List<BookEntity>> getAllBooks();

    @Query("select * from books order by price")
    LiveData<List<BookEntity>> getPriceFilteredBooks();

    @Query("select * from books order by title")
    LiveData<List<BookEntity>> getTitleFilteredBooks();

    @Query("select * from books where id=:id")
    List<BookEntity> getBook(String id);

    @Insert
    void addBook(BookEntity item);

    @Query("delete FROM books")
    void deleteAllBooks();

    @Query("delete from books where id = (SELECT Max(id) FROM books)")
    void deleteLastBook();

    @Query("delete FROM books where author like '%unknown%'")
    void deleteUnknown();

    @Query("delete FROM books where price > 50")
    void deleteExpensive();
}
