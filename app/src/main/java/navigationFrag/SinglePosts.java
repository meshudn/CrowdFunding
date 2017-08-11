package navigationFrag;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charity.meshu.charity.LoginActivity;
import com.charity.meshu.charity.R;
import com.charity.meshu.charity.UserHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Model.Posts;
import Model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class SinglePosts extends Fragment implements View.OnClickListener{

    AlphaAnimation buttonClickAnimation;
    ImageView postPhoto,userPhoto;
    TextView postTitle,createdBy,desc,tag,location,backers,target,funded,remainDay,createdByTxt,deadlineTxt;
    RelativeLayout campaignBtn,commentBtn,updateBtn; /* its used as combine button*/
    Button donateNow;

    Posts posts;
    User users;

    DatabaseReference mdatabaseReference;
    private String postId;
    private String authUserEmail,uid;
    private User mainUser;

    public SinglePosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_posts, container, false);

        /*
        *  all initialization
        * */
        UserHome userHome = (UserHome) getContext();
        posts = userHome.getQueryObject();

        Log.d("title",posts.title);
        Log.d("title",posts.desc); /* successful */

        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

        postPhoto = (ImageView) v.findViewById(R.id.postPhoto);
        userPhoto = (ImageView) v.findViewById(R.id.userPhoto);
        postTitle = (TextView) v.findViewById(R.id.singlePostTitle);
        createdBy = (TextView) v.findViewById(R.id.createdBy);
        createdByTxt = (TextView) v.findViewById(R.id.createdBytxt);
        desc = (TextView) v.findViewById(R.id.desc);
        tag = (TextView) v.findViewById(R.id.singlePostTag);
        location = (TextView) v.findViewById(R.id.singlePostLocation);
        backers = (TextView) v.findViewById(R.id.backers);
        target = (TextView) v.findViewById(R.id.targetFund);
        funded = (TextView) v.findViewById(R.id.funded);
        remainDay = (TextView) v.findViewById(R.id.remainDays);
        deadlineTxt = (TextView) v.findViewById(R.id.deadlineTxt);

        donateNow = (Button) v.findViewById(R.id.singlePostDonateBtn);

//        campaignBtn = (RelativeLayout) v.findViewById(R.id.campaignBtn);
        updateBtn = (RelativeLayout) v.findViewById(R.id.updateBtn);
        commentBtn = (RelativeLayout) v.findViewById(R.id.commentBtn);

//        campaignBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
        donateNow.setOnClickListener(this);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            authUserEmail = user.getEmail();
            uid = user.getUid();
        }else{
            uid = "";
        }



        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

        /*
        *
        * downloading user table
        * */

        mdatabaseReference.child("userList").orderByChild("userId").equalTo(posts.userId).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        users = child.getValue(User.class);
                    }

                    if (users.imageUri != null) {
                        Picasso.with(getContext()).load(users.imageUri).into(userPhoto);
                    }else{

                    }
                    if(users.name != null){
                        createdBy.setText("by "+ users.name);
                        createdByTxt.setText(users.name);
                        createdBy.setPaintFlags(createdBy.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */

        /*
        *  collecting main user data
        * */

        /*
        *
        * downloading user table
        * */

        mdatabaseReference.child("userList").orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        mainUser = child.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */



        /* setting cover photo */
        if(posts.coverUri != null) {
            Log.d("photo post:===",posts.coverUri);
            Picasso.with(getContext()).load(posts.coverUri.toString()).into(postPhoto);
        }

        String deadline = "This project will only be archived if at least $"+posts.fund+" is pledged within "+posts.remainDays + " days";

        postTitle.setText(posts.title);
        desc.setText(posts.desc);

        tag.setText(posts.tag);
        location.setText(posts.location);
        backers.setText(posts.backers);
        target.setText("$"+posts.fund);
        funded.setText("$"+posts.currentFund);
        remainDay.setText(posts.remainDays);
        deadlineTxt.setText(deadline);


        return v;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClickAnimation);


        if(v == commentBtn){
            Bundle bundle = new Bundle();
            bundle.putString("userId",posts.userId);
            bundle.putString("postId",posts.postId);
            if(users.name!=null){
                bundle.putString("userName",users.name);
            }


            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Comments comments = new Comments();
            comments.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, comments);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(v == updateBtn){
            Bundle bundle = new Bundle();
            bundle.putString("userId",posts.userId);
            bundle.putString("postId",posts.postId);
            if(users.name!=null){
                bundle.putString("userName",users.name);
            }


            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            UpdateActivity updateActivity = new UpdateActivity();
            updateActivity.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, updateActivity);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(v == donateNow){
            Bundle bundle = new Bundle();
            bundle.putString("userId",mainUser.userId);
            bundle.putString("postId",posts.postId);
            bundle.putString("userName",mainUser.name);
            bundle.putString("userImage",mainUser.imageUri);
            bundle.putString("postTitle",posts.title);


            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            DonatePage mainScreen = new DonatePage();
            mainScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_user_home, mainScreen);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}


   /* Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

    BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
    Paint paint = new Paint();
paint.setShader(shader);

        Canvas c=new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2,bitmap.getHeight()/2,bitmap.getWidth()/2,paint);

        imageView.setImageBitmap(circleBitmap);*/