package com.example.lab2.provider;


import static com.example.lab2.provider.BookEntity.TABLE_NAME;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = TABLE_NAME)
public class BookEntity {

    public static final String TABLE_NAME = "books";
    @PrimaryKey(autoGenerate = true)
    @NonNull

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "isbn")
    private String isbn;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "price")
    private double price;

    public BookEntity(String title, String isbn, String author, String desc, double price) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.desc = desc;
        this.price = price;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
