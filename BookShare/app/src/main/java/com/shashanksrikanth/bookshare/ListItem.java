package com.shashanksrikanth.bookshare;

import java.io.Serializable;
import java.util.ArrayList;

public class ListItem implements Serializable {
    // A class that represents an item in a list (could be any type of list- donor or wishlist or such)

    public String listName;
    public String listDescription;
    public String uid;
    public String listType;
    public String listDatabaseID;
    public ArrayList<String> isbnList;

    public ListItem() {}

    public ListItem(String listName, String listDescription, String uid, String listType, ArrayList<String> isbnList) {
        this.listName = listName;
        this.listDescription = listDescription;
        this.uid = uid;
        this.listType = listType;
        this.isbnList = isbnList;
    }

    public void setListDatabaseID(String databaseID) {
        this.listDatabaseID = databaseID;
    }
}
