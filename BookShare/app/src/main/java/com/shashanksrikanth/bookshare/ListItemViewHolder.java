package com.shashanksrikanth.bookshare;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemViewHolder extends RecyclerView.ViewHolder {
    TextView listName;

    public ListItemViewHolder(View view) {
        super(view);
        listName = view.findViewById(R.id.recyclerListItemName);
    }
}