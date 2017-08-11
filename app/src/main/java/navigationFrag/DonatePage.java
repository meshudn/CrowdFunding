package navigationFrag;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.charity.meshu.charity.LoginActivity;
import com.charity.meshu.charity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Model.Payments;
import Model.Posts;
import Model.User;

import static com.charity.meshu.charity.R.drawable.user;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonatePage extends Fragment implements View.OnClickListener {

    TextView holderName, bankAccount, bkashNo, phoneNumber;
    Button finalSubmitBtn, btn10, btn20, btn30, btn50, btn100, btn500;
    EditText otherPay;
    private AlphaAnimation buttonClickAnimation;
    private String payAmount, userId, userName, postId, userImage, postTitle;
    private DatabaseReference mdatabaseReference;
    Boolean flag = false;
    User users;
    private String OtherpayAmount;
    private Posts singlePost;
    private boolean dataCollect = false;

    public DonatePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_donate_page, container, false);


        userId = getArguments().getString("userId");
        postId = getArguments().getString("postId");
        userName = getArguments().getString("userName");
        userImage = getArguments().getString("userImage");
        postTitle = getArguments().getString("postTitle");

        Log.d("userName",userName);
        Log.d("userImage",userImage);

        finalSubmitBtn = (Button) v.findViewById(R.id.finalSubmitBtn);
        btn10 = (Button) v.findViewById(R.id.btn10);
        btn20 = (Button) v.findViewById(R.id.btn20);
        btn30 = (Button) v.findViewById(R.id.btn30);
        btn50 = (Button) v.findViewById(R.id.btn50);
        btn100 = (Button) v.findViewById(R.id.btn100);
        btn500 = (Button) v.findViewById(R.id.btn500);
        otherPay = (EditText) v.findViewById(R.id.otherPayBtn);

        btn10.setOnClickListener(this);
        btn20.setOnClickListener(this);
        btn30.setOnClickListener(this);
        btn50.setOnClickListener(this);
        btn100.setOnClickListener(this);
        btn500.setOnClickListener(this);
        finalSubmitBtn.setOnClickListener(this);

        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);


        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

         /*
        *
        * downloading user table
        * */

        mdatabaseReference.child("postList").orderByChild("postId").equalTo(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        singlePost = child.getValue(Posts.class);
                    }
                    dataCollect = true;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */



        flag = false;

        return v;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClickAnimation);

        if (btn10 == v) {
            flag = true;
            payAmount = "10";
        }
        if (btn20 == v) {
            flag = true;
            payAmount = "20";
        }
        if (btn30 == v) {
            flag = true;
            payAmount = "30";
        }
        if (btn50 == v) {
            flag = true;
            payAmount = "50";
        }
        if (btn100 == v) {
            flag = true;
            payAmount = "100";
        }
        if (btn500 == v) {
            flag = true;
            payAmount = "500";
        }


        if (finalSubmitBtn == v) {
            OtherpayAmount = otherPay.getText().toString();
            Log.d("payment",payAmount);
            int cash = 0;
            if (payAmount.equals("")) {
                cash = 0;
                Toast.makeText(getActivity(), "Please select any option!",
                        Toast.LENGTH_SHORT).show();

            } else {
                cash = Integer.parseInt(payAmount);


                Payments payments = new Payments(userId, postId, Integer.toString(cash), userName, userImage, postTitle);


                mdatabaseReference.child("paymentList").push().setValue(payments);

                if(singlePost != null && dataCollect){
                    singlePost.backers = singlePost.backers + 1;
                    int tempFund = Integer.parseInt(singlePost.currentFund);
                    tempFund = tempFund + cash;
                    singlePost.currentFund = Integer.toString(tempFund);
                    mdatabaseReference.child("postList").child(postId).setValue(singlePost);
                }


                Toast.makeText(getActivity(), "done!",
                        Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().popBackStack();
            }


        }

/*
        if (flag == true){
            v.setBackgroundResource(R.drawable.buttonborder);
            flag = false;
        }
        else {
            v.setBackgroundColor(Color.rgb(246, 246, 246));
        }*/


    }
}
