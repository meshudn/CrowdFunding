package com.charity.meshu.charity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Posts;
import Model.User;
import navigationFrag.CreatePost;
import navigationFrag.MainScreen;
import navigationFrag.PaymentHistoryPage;
import navigationFrag.ProfileActivity;
import navigationFrag.SinglePosts;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter recycleAdapter;
    public RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase db;
    DatabaseReference mdatabaseReference;

    String uid;


    private FirebaseAuth mAuth;
    private String email;
    private String password;

    private ImageView userImage;
    private TextView userName, userEmail;
    private String userNameTxt;
    private String userEmailTxt;
    private Boolean successLogin;
    private View navHeaderView;

    User singleUser;
    String authUserEmail;

    public Posts queryObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);





        /* *****************************************************
        * Navigation Drawer
        * */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /********************   End of Navigation Drawer ******************** */


        mAuth = FirebaseAuth.getInstance();

        successLogin = false;


        Menu nav_Menu = navigationView.getMenu();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            authUserEmail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();

            successLogin = true;
        } else {
            uid = "";
            nav_Menu.findItem(R.id.allProject).setEnabled(false);
            nav_Menu.findItem(R.id.createProject).setEnabled(false);
            nav_Menu.findItem(R.id.editProject).setEnabled(false);
            nav_Menu.findItem(R.id.myProject).setEnabled(false);
            nav_Menu.findItem(R.id.payments).setEnabled(false);
            nav_Menu.findItem(R.id.love).setEnabled(false);
            nav_Menu.findItem(R.id.recommend).setEnabled(false);
            nav_Menu.findItem(R.id.nav_profile).setEnabled(false);
            nav_Menu.findItem(R.id.nav_editProfile).setEnabled(false);
            nav_Menu.findItem(R.id.nav_logout).setEnabled(false);

            nav_Menu.findItem(R.id.nav_login).setVisible(true);


        }



        Bundle bundle = new Bundle();
        bundle.putString("userId", uid);
        bundle.putString("userSearch", "0");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MainScreen mainScreen = new MainScreen();
        mainScreen.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_user_home, mainScreen);
        fragmentTransaction.commit();


        /*
        *
        *  Initialization of View's
        *
        * textView = userName, userEmail
        * imageView = userImage
        * */
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        /*userImage = (ImageView) navHeaderView.findViewById(R.id.logInImage);*/
        userName = (TextView) hView.findViewById(R.id.userName);
        userEmail = (TextView) hView.findViewById(R.id.userEmail);
        userImage = (ImageView) hView.findViewById(R.id.logInImage);

        userEmail.setText(authUserEmail);

        /********************** End of init ************************/


        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

         /*
        *
        * collecting all the posts
        * */

        if (!uid.equals("")) {

            mdatabaseReference.child("userList").orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    if (children != null) {
                        for (DataSnapshot child : children) {
                            singleUser = child.getValue(User.class);
                        }

                        if (singleUser.imageUri != null && !singleUser.equals("")) {
                            //placing the user image into imageview by downloading from firebase
                            Picasso.with(UserHome.this).load(singleUser.imageUri).into(userImage);
                        }
                        if (singleUser.name != null) {
                            userName.setText(singleUser.name);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        /* ----------------------------- --------------------------- */
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (uid.equals("") && id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        if (id == R.id.allProject) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", uid);
            bundle.putString("userSearch", "0");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, mainScreen);
            fragmentTransaction.commit();

        } else if (id == R.id.myProject) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", uid);
            bundle.putString("userSearch", "1");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, mainScreen);
            fragmentTransaction.commit();

        } else if (id == R.id.createProject) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            CreatePost createPost = new CreatePost();
            fragmentTransaction.replace(R.id.content_user_home, createPost);
            fragmentTransaction.commit();

        } else if (id == R.id.editProject) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", uid);
            bundle.putString("userSearch", "3");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, mainScreen);
            fragmentTransaction.commit();

        } else if (id == R.id.payments) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            PaymentHistoryPage paymentHistoryPage = new PaymentHistoryPage();
            fragmentTransaction.replace(R.id.content_user_home, paymentHistoryPage);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.love) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", uid);
            bundle.putString("userSearch", "4");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, mainScreen);
            fragmentTransaction.commit();

        } else if (id == R.id.recommend) {

        } else if (id == R.id.nav_profile) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", uid);
            bundle.putString("userEmail", authUserEmail);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            ProfileActivity profileActivity = new ProfileActivity();
            profileActivity.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, profileActivity);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_editProfile) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void switchFragment(Posts posts) {
        this.getSupportFragmentManager().popBackStack();
        this.queryObject = posts;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SinglePosts mainScreen = new SinglePosts();
        fragmentTransaction.replace(R.id.content_user_home, mainScreen);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchFragmentForEdit(Posts posts) {
        this.queryObject = posts;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CreatePost createPost = new CreatePost();
        fragmentTransaction.replace(R.id.content_user_home, createPost);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void setQueryObject(Posts posts) {
        this.queryObject = posts;
    }

    public Posts getQueryObject() {
        return queryObject;
    }


}
