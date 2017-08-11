package navigationFrag;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charity.meshu.charity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileActivity extends Fragment {

    TextView profileName,profileBio,profileEmail,profileLastLogin,profileFriends,profileDonate;
    ImageView profileImage;
    private String userId,userEmail,bio;
    private int totalDonate,totalFriend;
    private User users;
    private DatabaseReference mdatabaseReference;

    public ProfileActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = (TextView) v.findViewById(R.id.profileName);
        profileEmail = (TextView) v.findViewById(R.id.profileEmail);
        profileBio = (TextView) v.findViewById(R.id.profileBio);
        profileLastLogin = (TextView) v.findViewById(R.id.profileLastLogin);
        profileFriends = (TextView) v.findViewById(R.id.profileFriends);
        profileDonate = (TextView) v.findViewById(R.id.profileBackers);

        bio = "A biography, or simply bio, is a detailed description of a person's life. It involves more than just the basic facts like education, work, relationships, and death; it portrays a person's experience of these life events.";

        profileImage = (ImageView) v.findViewById(R.id.profilePhoto);


        userId = getArguments().getString("userId");
        userEmail = getArguments().getString("userEmail");

        profileEmail.setText("Email: "+userEmail);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference();


        /*
        *
        * downloading user table
        * */


        mdatabaseReference.child("userList").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(children != null) {
                    for (DataSnapshot child : children) {
                        users = child.getValue(User.class);
                    }
                    if(!users.imageUri.equals("") || users.imageUri != null){
                        Picasso.with(getContext()).load(users.imageUri).into(profileImage);
                    }
                    if(users != null){
                        profileName.setText(users.name);
                        profileBio.setText(users.bio);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        /* ----------------------------- --------------------------- */



        /*
        *
        * fetching Total Donate number of this user
        * from paymentList table...
        * Sql format should be ::: select * paymentList where userId = this.userId
        * */


        mdatabaseReference.child("paymentList").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(children != null) {
                    for (DataSnapshot child : children) {
                       totalDonate++;
                    }
                }else{
                    totalDonate = 0;
                }
                profileDonate.setText("Total Donate: $"+totalDonate);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */



         /*
        *
        * fetching Total Friends number of this user
        * from Friends List table...
        * Sql format should be ::: select * firendList where userId = this.userId
        * */
/*
        mdatabaseReference.child("friendList").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(children != null) {
                    for (DataSnapshot child : children) {
                        totalFriend++;
                    }
                }else{
                    totalFriend = 0;
                }
                profileFriends.setText(totalFriend+" friends");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        /* ----------------------------- --------------------------- */






        return v;
    }

}
