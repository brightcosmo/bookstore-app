package com.example.lab2.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<BookEntity>> mAllItems;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllItems = mBookDao.getAllBooks();
    }
    LiveData<List<BookEntity>> getAllItems() {
        return mAllItems;
    }

    LiveData<List<BookEntity>> getPriceFilteredBooks(){
        return mBookDao.getPriceFilteredBooks();
    }

    LiveData<List<BookEntity>> getTitleFilteredBooks(){
        return mBookDao.getTitleFilteredBooks();
    }
    void insert(BookEntity book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBooks();
        });
    }


    void deleteLastBook(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteLastBook();
        });
    }

    void deleteUnknown(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteUnknown();
        });
    }

    void deleteExpensive(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteExpensive();
        });
    }
}
