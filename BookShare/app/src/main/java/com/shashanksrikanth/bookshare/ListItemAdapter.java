package com.shashanksrikanth.bookshare;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
    // Adapter for the ListItem class and RecyclerView in the DonorHomePage

    ArrayList<ListItem> listItems;
    DonorHomePage donorHomePage;
    private static final String TAG = "ListItemAdapter";

    public ListItemAdapter(ArrayList<ListItem> listItems, DonorHomePage donorHomePage) {
        this.listItems = listItems;
        this.donorHomePage = donorHomePage;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recycler_row, parent, false);
        listItemView.setOnClickListener(donorHomePage);
        listItemView.setOnLongClickListener(donorHomePage);
        ListItemViewHolder holder = new ListItemViewHolder(listItemView);
        Log.d(TAG, "onCreateViewHolder: created ListItemViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ListItem item = listItems.get(position);
        String itemName = item.listName;
        holder.listName.setText(itemName);
        Log.d(TAG, "onBindViewHolder: name of item is " + itemName);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
