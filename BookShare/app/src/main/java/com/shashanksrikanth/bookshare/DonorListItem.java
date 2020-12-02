package com.shashanksrikanth.bookshare;

import java.util.ArrayList;

public class DonorListItem {
    public String listName;
    public String listDescription;
    public String uid;
    public ArrayList<String> isbnList;

    public DonorListItem(String listName, String listDescription, String uid, ArrayList<String> isbnList) {
        this.listName = listName;
        this.listDescription = listDescription;
        this.uid = uid;
        this.isbnList = isbnList;
    }
}
