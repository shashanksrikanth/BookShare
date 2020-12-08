package com.shashanksrikanth.bookshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemViewHolder> {

    private ArrayList<BookItem> books;
    private DonorBookPage donorBookPage;
    private ReceiverBookPage receiverBookPage;

    public BookItemAdapter(ArrayList<BookItem> books, DonorBookPage donorBookPage, ReceiverBookPage receiverBookPage) {
        this.books = books;
        this.donorBookPage = donorBookPage;
        this.receiverBookPage = receiverBookPage;
    }

    @NonNull
    @Override
    public BookItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View bookView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem_recycler_row, parent, false);
        if(donorBookPage!=null) {
            bookView.setOnClickListener(donorBookPage);
            bookView.setOnLongClickListener(donorBookPage);
        }
        else {
            bookView.setOnClickListener(receiverBookPage);
            bookView.setOnLongClickListener(receiverBookPage);
        }
        return new BookItemViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(BookItemViewHolder holder, int position) {
        BookItem book = books.get(position);
        holder.recyclerBookItemName.setText(book.bookTitle);
        holder.recyclerBookItemIsbn.setText(book.bookISBN);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
