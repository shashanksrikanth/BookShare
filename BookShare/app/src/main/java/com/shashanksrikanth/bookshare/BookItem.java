package com.shashanksrikanth.bookshare;

public class BookItem {
    // A class that represents a certain book- fields are populated from the results of BookDownloader

    public String bookTitle;
    public String bookPublishedDate;
    public String bookPublisher;
    public String bookDescription;
    public String bookISBN;
    public int bookAverageRating;
    public String bookImageLink;

    public BookItem() {}

    public BookItem(String bookTitle, String bookPublishedDate, String bookPublisher, String bookDescription,
                    String bookISBN, int bookAverageRating, String bookImageLink) {
        this.bookTitle = bookTitle;
        this.bookPublishedDate = bookPublishedDate;
        this.bookPublisher = bookPublisher;
        this.bookDescription = bookDescription;
        this.bookISBN = bookISBN;
        this.bookAverageRating = bookAverageRating;
        this.bookImageLink = bookImageLink;
    }
}
