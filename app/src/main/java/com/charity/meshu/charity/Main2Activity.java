package com.charity.meshu.charity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adaptor.MainActivityRecyclerView;
import Model.Posts;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase db;
    DatabaseReference mdatabaseReference;

    ArrayList<Posts> allPostList = new ArrayList<Posts>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

         /*
        *
        * collecting all the posts
        * */

        mdatabaseReference.child("postList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        Posts singlePosts = child.getValue(Posts.class);

                        allPostList.add(singlePosts);

                        Log.d("title",singlePosts.title);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */




        recyclerView = (RecyclerView) findViewById(R.id.homeRecycler);
        recycleAdapter = new MainActivityRecyclerView(allPostList,this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recycleAdapter);

    }
}
