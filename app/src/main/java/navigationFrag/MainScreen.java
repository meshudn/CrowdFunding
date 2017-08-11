package navigationFrag;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charity.meshu.charity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adaptor.MainActivityRecyclerView;
import Model.Posts;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainScreen extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase db;
    DatabaseReference mdatabaseReference;


    private String email;
    private String password;

    Context mContext;
    private String userId;
    private String userSearch;

    public MainScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_screen, container, false);



        userId = getArguments().getString("userId");
        userSearch = getArguments().getString("userSearch");

        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

         /*
        *
        * collecting all the posts
        * */


        recyclerView = (RecyclerView) v.findViewById(R.id.homeRecycler);
        recyclerView.setNestedScrollingEnabled(false);

        if(userSearch.equals("1")){
            mdatabaseReference.child("postList").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Posts> allPostList = new ArrayList<Posts>();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    if (children != null) {
                        for (DataSnapshot child : children) {
                            Posts singlePosts = child.getValue(Posts.class);
                            allPostList.add(singlePosts);
                            Log.d("title", singlePosts.title);
                        }
                    }
                    layoutManager = new LinearLayoutManager(getActivity());
                    recycleAdapter = new MainActivityRecyclerView(allPostList, getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(recycleAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(userSearch.equals("0")) {
            mdatabaseReference.child("postList").addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Posts> allPostList = new ArrayList<Posts>();

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    if (children != null) {
                        for (DataSnapshot child : children) {
                            Posts singlePosts = child.getValue(Posts.class);

                            allPostList.add(singlePosts);

                            Log.d("title", singlePosts.title);
                        }
                    }


                    layoutManager = new LinearLayoutManager(getActivity());

                    recycleAdapter = new MainActivityRecyclerView(allPostList, getActivity());

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(recycleAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        /* ----------------------------- --------------------------- */
        }

        else if(userSearch.equals("3")) {
            mdatabaseReference.child("postList").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Posts> allPostList = new ArrayList<Posts>();

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    if (children != null) {
                        for (DataSnapshot child : children) {
                            Posts singlePosts = child.getValue(Posts.class);

                            allPostList.add(singlePosts);

                            Log.d("title", singlePosts.title);
                        }
                    }


                    layoutManager = new LinearLayoutManager(getActivity());

                    recycleAdapter = new MainActivityRecyclerView(allPostList, getActivity(),1);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(recycleAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        /* ----------------------------- --------------------------- */
        }







        return v;
    }



}
