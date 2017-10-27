package com.example.m4hmmd.bilancer;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Post {

    public static FirebaseDatabase database;
    public static DatabaseReference participantsRef;

    private String title;
    private String description;
    private int quota;
    private String location;
    private String date;
    private String category;
    private String authorID;
    private ArrayList<String> participants;

    Post(String aTitle, String aDescription, int quota, String aLocation, String aDate, String category, String authorID){
        database = FirebaseDatabase.getInstance();
        participantsRef = database.getReference("DashboardPosts");

        title = aTitle;
        description = aDescription;
        this.quota = quota;
        location = aLocation;
        date = aDate;
        this.category = category;
        this.authorID = authorID;
        participants = new ArrayList<>();
        participants.add(authorID);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuota() { return quota; }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) { this.category = category; }

    public String getAuthorID() { return authorID; }

    public ArrayList<String> getParticipants() {return participants;    }

    public void addParticipant(FirebaseUser participant) {
        boolean acceptable = true;
        for ( String aParticipant : participants) {
            if (aParticipant.equals(participant.getUid()))
                acceptable = false;
        }
        if (participants.size() < quota && acceptable) {
            participants.add(participant.getUid());
        }
    }

    public void addParticipant(String uid) {
        // check if it is a proper uid

        if (!participants.contains(uid))
            participants.add(uid);
    }
}


