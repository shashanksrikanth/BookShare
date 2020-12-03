package com.shashanksrikanth.bookshare;

import java.util.ArrayList;

public class ListItem {
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
