package navigationFrag;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.charity.meshu.charity.LoginActivity;
import com.charity.meshu.charity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adaptor.CommentAdaptor;
import Adaptor.MainActivityRecyclerView;
import Model.Posts;

/**
 * A simple {@link Fragment} subclass.
 */
public class Comments extends Fragment {
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    TextView postTitle,createdBy;
    Button writeComment;
    private String userId,postId,userName;

    private AlphaAnimation buttonClickAnimation;
    DatabaseReference mdatabaseReference;

    public Comments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_comment_main, container, false);


         userId = getArguments().getString("userId");
         postId = getArguments().getString("postId");
         userName = getArguments().getString("userName");


        mdatabaseReference = FirebaseDatabase.getInstance().getReference();
        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

    /*    List<String> Username = new ArrayList<String>();
        List<String> Description = new ArrayList<String>();

        Username.add("Tonoy");
        Username.add("Dip");
        Username.add("Tonoy");
        Username.add("Dip");
        Username.add("Meshu");
        Username.add("Dip");

        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");
        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");
        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");
        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");
        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");
        Description.add("Lorem Ipsum Generator. Generate lorem ipsum in paragraphs, words or sentences. Optional html markup - paragraph, italic and bold tags.");

*/

        recyclerView = (RecyclerView) v.findViewById(R.id.commentRecyclerView);

        mdatabaseReference.child("commentList").orderByChild("joinId").equalTo(userId+"_"+postId).addValueEventListener(new ValueEventListener() {
            ArrayList<Model.Comments> allCommentList = new ArrayList<Model.Comments>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        Model.Comments singleComment = child.getValue(Model.Comments.class);

                        allCommentList.add(singleComment);

                        Log.d("userId",singleComment.getUserId());
                    }
                }


                adapter = new CommentAdaptor(allCommentList);

                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */

        writeComment = (Button) v.findViewById(R.id.commentBtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            writeComment.setVisibility(View.GONE);
        }



        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);



                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                bundle.putString("postId",postId);
                bundle.putString("userName",userName);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                WriteCommentActivity writeCommentActivity = new WriteCommentActivity();
                writeCommentActivity.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_user_home, writeCommentActivity);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
