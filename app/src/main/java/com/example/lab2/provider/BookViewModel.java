package com.example.lab2.provider;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mRepository;
    private LiveData<List<BookEntity>> mAllItems;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllItems = mRepository.getAllItems();
    }

    public LiveData<List<BookEntity>> getAllItems() {
        return mAllItems;
    }

    public LiveData<List<BookEntity>> getTitleFilteredBooks() {
        return mRepository.getTitleFilteredBooks();
    }

    public LiveData<List<BookEntity>> getPriceFilteredBooks() {
        return mRepository.getPriceFilteredBooks();
    }

    public void insert(BookEntity book) {
        mRepository.insert(book);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteLastBook(){
        mRepository.deleteLastBook();
    }

    public void deleteUnknown() { mRepository.deleteUnknown(); }

    public void deleteExpensive() { mRepository.deleteExpensive(); }
}
