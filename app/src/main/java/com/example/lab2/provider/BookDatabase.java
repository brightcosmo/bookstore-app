package com.example.lab2.provider;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BookEntity.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    public static final String ITEM_DATABASE_NAME = "book_database";

    public abstract BookDao bookDao();

    private static volatile BookDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BookDatabase.class, ITEM_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
