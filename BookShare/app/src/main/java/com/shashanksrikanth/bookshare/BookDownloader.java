package com.shashanksrikanth.bookshare;

import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookDownloader implements Runnable{
    // Calls Google Books API for a given ISBN and processes the results

    String isbn;
    String query = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    DonorBookPage bookPage;

    public BookDownloader(String isbn, DonorBookPage bookPage) {
        this.isbn = isbn;
        this.bookPage = bookPage;
    }

    @Override
    public void run() {
        Uri.Builder builder = Uri.parse(query + isbn).buildUpon();
        String urlToUse = builder.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append('\n');
        }
        catch (Exception e) {
            return;
        }
        processData(sb.toString());
    }

    public void processData(String string) {
        try{
            JSONObject jsonResult = new JSONObject(string);
            int numberOfBooks = jsonResult.getInt("totalItems");
            if(numberOfBooks == 0) {
                bookPage.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookPage.noBookError(isbn);
                    }
                });
                return;
            }
            JSONObject bookDetails = jsonResult.getJSONArray("items").getJSONObject(0);
            String bookTitle = "NULL";
            String bookAuthor = "NULL";
            if(bookDetails.has("volumeInfo")) {
                JSONObject volumeInfo = bookDetails.getJSONObject("volumeInfo");
                if(volumeInfo.has("title")) bookTitle = volumeInfo.getString("title");
                if(volumeInfo.has("authors")) bookAuthor = volumeInfo.getJSONArray("authors").getString(0);
            }
            String bookPublisher = "NULL";
            if(bookDetails.has("publisher")) bookPublisher = bookDetails.getString("publisher");
            String bookDescription = "NULL";
            if(bookDetails.has("searchInfo")) {
                JSONObject searchInfo = bookDetails.getJSONObject("searchInfo");
                if(searchInfo.has("textSnippet")) bookDescription = searchInfo.getString("textSnippet");
            }
            int bookAverageRating = -1;
            if(bookDetails.has("averageRating")) bookAverageRating = bookDetails.getInt("averageRating");
            String bookImageLink = "NULL";
            if(bookDetails.has("imageLinks")) {
                JSONObject imageLinks = bookDetails.getJSONObject("imageLinks");
                if(imageLinks.has("smallThumbnail")) bookImageLink = imageLinks.getString("smallThumbnail");
            }
            final BookItem bookItem = new BookItem(bookTitle, bookAuthor, bookPublisher, bookDescription, isbn, bookAverageRating, bookImageLink);
            bookPage.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bookPage.addBookToList(isbn, bookItem);
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
