package com.example.m4hmmd.bilancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PostDetails extends AppCompatActivity {

    // the Post details of which we are looking at
    static Post thisPost;
    // key of this Post in the database
    int postKey;

    TextView postDetailsTitle;
    TextView postDetailsDescription;
    TextView postDetailsCategory;
    TextView postDetailsDateTime;
    TextView postDetailsLocation;
    TextView postDetailsQuota;
    TextView postDetailsAuthor;
    TextView postDetailsParticipants;

    FirebaseAuth firebaseAuth;
    FirebaseUser thisUser;
    DatabaseReference postRef;
    DatabaseReference authorRef;
    DatabaseReference participantsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postKey = Dashboard.dashboardAllPosts.indexOf(thisPost);

        authorRef = Dashboard.database.getReference("Users").child(thisPost.getAuthorID());
        authorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String authorName = dataSnapshot.child("name").getValue().toString()
                        + " " + dataSnapshot.child("surname").getValue().toString();
                Log.i("authorName", authorName);
                postDetailsAuthor.setText("Author: " + authorName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });

        participantsRef = Dashboard.database.getReference("DashboardPosts").child(Integer.toString(postKey)).child("participants");
        Log.i("authorName", participantsRef.toString());
        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String participants = "Participants: ";
                Log.i("authorName", dataSnapshot.toString());
                for (DataSnapshot aChild : dataSnapshot.getChildren()) {
                    Log.i("authorName", "forLoop");
                    participants = participants + aChild.getValue().toString() + ", ";

                }
                if ( participants.length() > 2) {
                    Log.i("authorName", "ifStatement");
                    participants = participants.substring(0, participants.length() - 2);
                    postDetailsParticipants.setText(participants);
                }
                Log.i("authorName", "Participants: " + participants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        postDetailsTitle = (TextView) findViewById(R.id.postDetailsTitle);
        postDetailsDescription = (TextView) findViewById(R.id.postDetailsDescription);
        postDetailsCategory = (TextView) findViewById(R.id.postDetailsCategory);
        postDetailsDateTime = (TextView) findViewById(R.id.postDetailsDateTime);
        postDetailsLocation = (TextView) findViewById(R.id.postDetailsLocation);
        postDetailsQuota = (TextView) findViewById(R.id.postDetailsQuota);
        postDetailsAuthor = (TextView) findViewById(R.id.postDetailsAuthor);
        postDetailsParticipants = (TextView) findViewById(R.id.postDetailsParticipants);

        postDetailsTitle.setText(thisPost.getTitle());
        postDetailsDescription.setText(thisPost.getDescription());
        postDetailsQuota.setText("Signed Up: " + Integer.toString(thisPost.getParticipants().size()) + "/" + Integer.toString(thisPost.getQuota()));
        postDetailsDateTime.setText("When: " + thisPost.getDate());
        postDetailsLocation.setText("Where: " + thisPost.getLocation());
        postDetailsCategory.setText("Category: " + thisPost.getCategory());
        //if (authorName != null)
           // postDetailsAuthor.setText("Author: " + authorName);

        firebaseAuth = FirebaseAuth.getInstance();
        thisUser = firebaseAuth.getCurrentUser();

    }

    public static void initializeDetails(Post aPost) {
        thisPost = aPost;
    }

    /* For further development
    public void deletePost(View view) {
        // Maybe display an are you sure dialog box
        Dashboard.deletePost(thisPost);
        Intent intent = new Intent(PostDetails.this, Dashboard.class);
        startActivity(intent);
    }

    public void editPost(View view) {

    }
    */
    public void attendEvent(View view) {
        thisPost.addParticipant(thisUser);
        Dashboard.dashboardAllPosts.set(postKey, thisPost);
        postRef = Dashboard.database.getReference("DashboardPosts").child(Integer.toString(postKey));
        postRef.setValue(thisPost);
        Intent intent = new Intent(PostDetails.this, Dashboard.class);
        startActivity(intent);
    }

}

