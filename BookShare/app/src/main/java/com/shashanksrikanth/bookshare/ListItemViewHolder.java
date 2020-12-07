package com.shashanksrikanth.bookshare;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemViewHolder extends RecyclerView.ViewHolder {
    // ViewHolder for the RecyclerView in DonorHomePage and ListItem

    TextView listName;
    TextView listDescription;

    public ListItemViewHolder(View view) {
        super(view);
        listName = view.findViewById(R.id.recyclerListItemName);
        listDescription = view.findViewById(R.id.recyclerListItemDescription);
    }
}