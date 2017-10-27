package com.example.m4hmmd.bilancer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    // Slide Menu elements
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    // UI elements
    private Button editProfile;
    public static TextView name_surname;
    public static TextView usernameText;

    // Current User Properties
    private static String name;
    private static String surname;
    private static String username;

    public static FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        editProfile = (Button) findViewById(R.id.editProfile);
        editProfile.setOnClickListener(this);



        // Other UI elements
        name_surname = (TextView) findViewById(R.id.name_surname);
        usernameText = (TextView) findViewById(R.id.username);
        name_surname.setText(name + " " + surname);
        usernameText.setText("@" + username);

        // getting reference to the database
        database = FirebaseDatabase.getInstance();

        // Slide Menu elements
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MyProfile.this, Dashboard.class);
                    startActivity(intent);
                }
                else if (position == 1) {}
                else if (position == 2) {
                    Intent intent = new Intent(MyProfile.this, NewPost.class);
                    startActivity(intent);
                }
                else if (position == 3) {
                    Intent intent = new Intent(MyProfile.this, AboutPage.class);
                    startActivity(intent);
                }
                else if (position == 4) {
                    Dashboard.signOut();
                    Intent intent = new Intent(MyProfile.this, LogIn.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MyProfile.this, "Time for an upgrade!" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigation Drawer elements
    public void addDrawerItems() {
        String[] osArray = { "Home", "My Profile", "New Post", "About", "Sign out"};
        navigationDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(navigationDrawerAdapter);
    }
    public static void initializeUser(String aName, String aSurname, String aUsername){
        name = aName;
        surname = aSurname;
        username = aUsername;
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
    @Override
    public void onClick(View view){
        if (view == editProfile){
            Intent intent = new Intent(MyProfile.this, EditProfileActivity.class);
            startActivity(intent);
        }
    }
}
