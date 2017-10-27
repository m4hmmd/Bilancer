package com.example.m4hmmd.bilancer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be said to be the main class of the application. Displays posts and has methods to add new ones.
 */
public class Dashboard extends AppCompatActivity {

    // The ArrayList of Posts in the Dashboard
    static List<Post> dashboardAllPosts = new ArrayList<>();

    // Elements to connect to Firebase
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static DatabaseReference postsRef;
    public static DatabaseReference userRef;
    private static FirebaseAuth firebaseAuth;

    // The user that is logged in
    public static User thisUser;
    private String name;
    public static String uID; // ID of the user in database under which the User is stored

    private static ProgressDialog progressDialog;

    // Elements for displaying the posts in the dashboard (as CardViews)
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Elements for the slide menu (NavigationDrawer)
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // get reference to the database in use
        database = FirebaseDatabase.getInstance();

        // Set Up the Slide Menu
        setUpNavigationDrawer();

        setUpDashboardPosts();

        // Build up the RecyclerView for Posts
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // The Dashboard is filled with posts from dashboardAllPosts
        mAdapter = new MyRecyclerViewAdapter( (ArrayList) dashboardAllPosts);

        // Setting onClickListeners to Cards, which will open a new page displaying more info about the post
        mRecyclerView.setAdapter(mAdapter);
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(Dashboard.this, PostDetails.class);
                startActivity(intent);
                PostDetails.initializeDetails(dashboardAllPosts.get(position));
            }
        });

        // The small plus button in the bottom right corner to add a new Post
        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.addButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NewPost.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // User authorization
    public void setUpUser(){
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uID = user.getUid();
            // getting the current user from Firebase
            userRef = database.getReference("Users").child(uID);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    thisUser = dataSnapshot.getValue(User.class);

                    if (thisUser != null) {
                        name = thisUser.getName();
                        // surname = thisUser.getSurname();
                        // username = thisUser.getUsername();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    public static void signOut() {
        firebaseAuth.signOut();
        updateUI(null);

    }
    private static void updateUI(FirebaseUser user) {
        progressDialog.dismiss();
        /*
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }*/
    }

    public void setUpNavigationDrawer(){
        // Navigation Drawer elements
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        setUpUser();


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {}
                else if (position == 1) {
                    Log.i("nameSurname", Boolean.toString(thisUser == null));

                    Intent intent = new Intent(Dashboard.this, MyProfile.class);
                    startActivity(intent);

                    MyProfile.initializeUser(thisUser.getName(), thisUser.getSurname(), thisUser.getUsername());
                    //progressDialog.setMessage("Loading...");
                    //progressDialog.show();
                    //MyProfile.initializeUser(thisUser);
                }
                else if (position == 2) {
                    Intent intent = new Intent(Dashboard.this, NewPost.class);
                    startActivity(intent);
                    //progressDialog.setMessage("Loading...");
                    //progressDialog.show();
                }
                else if (position == 3) {
                    Intent intent = new Intent(Dashboard.this, AboutPage.class);
                    startActivity(intent);
                }
                else if (position == 4) {
                    signOut();
                    Intent intent = new Intent(Dashboard.this, LogIn.class);
                    startActivity(intent);
                    // progressDialog.setMessage("Loading...");
                    //progressDialog.show();
                }
                else
                    Toast.makeText(Dashboard.this, "Time for an upgrade!" + position, Toast.LENGTH_SHORT).show();

                progressDialog.hide();
            }
        });
        //
    }

    public static void setUpDashboardPosts() { //@update
        Log.i("onCreate", "setUpDashboardPosts");

        postsRef = database.getReference("DashboardPosts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dashboardAllPosts.clear();
                for ( DataSnapshot x : dataSnapshot.getChildren()) {
                    /*Post post = x.getValue(Post.class);*/
                    Post post = new Post(x.child("title").getValue().toString(),
                            x.child("description").getValue().toString(),
                            Integer.parseInt(x.child("quota").getValue().toString()),
                            x.child("location").getValue().toString(),
                            x.child("date").getValue().toString(),
                            x.child("category").getValue().toString(),
                            x.child("authorID").getValue().toString());
                    DataSnapshot a = x.child("participants");
                    for (DataSnapshot y : a.getChildren()) {
                        post.addParticipant(y.getValue().toString());
                    }
                    dashboardAllPosts.add(post);
                }
                Log.i("dashboardAllPosts", Integer.toString(dashboardAllPosts.size()));
                mAdapter = new MyRecyclerViewAdapter( (ArrayList) dashboardAllPosts);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }



    public static void addPost( String title, String description, String date, String time,
                                String place, String category, int quota,  String uid) {
        Log.i("addPost", uid);
        Post newPost = new Post(title, description, quota, place, date + ", at " + time, category, uid);
        dashboardAllPosts.add(newPost);
        Log.i("dashboardAllPosts", "just after adding the post" + Integer.toString(dashboardAllPosts.size()));
        myRef = database.getReference("DashboardPosts").child(Integer.toString(dashboardAllPosts.size() - 1));
        myRef.setValue(newPost);


        updateDashboard();
        Log.i("dashboardAllPosts", "after setting the RecyclerViewAdapter:" + Integer.toString(dashboardAllPosts.size()));
    }

    public static void updateDashboard() {
        mAdapter = new MyRecyclerViewAdapter( (ArrayList) dashboardAllPosts); //@update
        mRecyclerView.setAdapter(mAdapter); //@update
    }

    // Navigation Drawer elements
    public void addDrawerItems() {
        String[] osArray = { "Home", "My Profile", "New Post", "About", "Sign out"};
        navigationDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(navigationDrawerAdapter);
    }

    public void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Delete Post to be developed further
    public static void deletePost(Post aPost) {
        int position = dashboardAllPosts.indexOf(aPost); // won't work properly unless the database and the arraylist are constantly synced

        postsRef = database.getReference("DashboardPosts").child(Integer.toString(position));
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });

        postsRef.child("DashBoardPosts").orderByChild("title").equalTo("Apple")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }*/

}
