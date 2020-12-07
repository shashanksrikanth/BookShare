package com.shashanksrikanth.bookshare;

import java.util.ArrayList;

public class IsbnItem {
    // A class that holds certain fields about the book and an array list of lists it appears in
    // This is used in the database for filtration

    public String bookIsbn;
    public String bookTitle;
    public String bookAuthor;
    public String bookGenre;
    public ArrayList<String> listIds;

    public IsbnItem() {}

    public IsbnItem(String bookIsbn, String bookTitle, String bookAuthor, String bookGenre, ArrayList<String> listIds) {
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookGenre = bookGenre;
        this.listIds = listIds;
    }
}
