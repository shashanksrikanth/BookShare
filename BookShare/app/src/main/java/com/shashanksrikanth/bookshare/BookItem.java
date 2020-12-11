package com.shashanksrikanth.bookshare;

import java.io.Serializable;

public class BookItem implements Serializable {
    // A class that represents a certain book- fields are populated from the results of BookDownloader

    public String bookTitle;
    public String bookAuthor;
    public String bookPublisher;
    public String bookDescription;
    public String bookISBN;
    public int bookAverageRating;
    public String bookImageLink;
    public String bookGenre;
    public boolean bookIsSelected;

    public BookItem() {}

    public BookItem(String bookTitle, String bookAuthor, String bookPublisher, String bookDescription,
                    String bookISBN, int bookAverageRating, String bookImageLink, String bookGenre) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookDescription = bookDescription;
        this.bookISBN = bookISBN;
        this.bookAverageRating = bookAverageRating;
        this.bookImageLink = bookImageLink;
        this.bookGenre = bookGenre;
        this.bookIsSelected = false;
    }

    public void changeStatusSelected(boolean newStatus) {
        this.bookIsSelected = newStatus;
    }
}
