package com.shashanksrikanth.bookshare;

public class BookItem {
    public String isbn;
    public String bookName;
    public String bookAuthor;
    public String bookDescription;

    public BookItem() {}

    public BookItem(String isbn, String bookName, String bookAuthor, String bookDescription) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDescription = bookDescription;
    }
}
