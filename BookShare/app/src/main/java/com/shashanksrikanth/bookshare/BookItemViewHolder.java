package com.shashanksrikanth.bookshare;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BookItemViewHolder extends RecyclerView.ViewHolder {
    // ViewHolder for the RecyclerView in DonorBookPage and BookItem

    TextView recyclerBookItemName;
    TextView recyclerBookItemIsbn;

    public BookItemViewHolder(View view) {
        super(view);
        recyclerBookItemName = view.findViewById(R.id.recyclerBookItemName);
        recyclerBookItemIsbn = view.findViewById(R.id.recyclerBookItemIsbn);
    }
}
