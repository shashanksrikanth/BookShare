package com.shashanksrikanth.bookshare;

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
    ReceiverHomePage receiverHomePage;

    public ListItemAdapter(ArrayList<ListItem> listItems, DonorHomePage donorHomePage, ReceiverHomePage receiverHomePage) {
        this.listItems = listItems;
        this.donorHomePage = donorHomePage;
        this.receiverHomePage = receiverHomePage;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recycler_row, parent, false);
        if(donorHomePage!=null) {
            listItemView.setOnClickListener(donorHomePage);
            listItemView.setOnLongClickListener(donorHomePage);
        }
        else {
            listItemView.setOnClickListener(receiverHomePage);
            listItemView.setOnLongClickListener(receiverHomePage);
        }
        ListItemViewHolder holder = new ListItemViewHolder(listItemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ListItem item = listItems.get(position);
        String itemName = item.listName;
        holder.listName.setText(itemName);
        String itemDescription = item.listDescription;
        if(itemDescription.equals("")) holder.listDescription.setText(R.string.no_description_available);
        else if (itemDescription.length()>80) {
            String description = itemDescription.substring(0,79) + "...";
            holder.listDescription.setText(description);
        }
        else holder.listDescription.setText(itemDescription);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
