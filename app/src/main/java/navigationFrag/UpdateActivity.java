package navigationFrag;


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
import android.widget.Toast;

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

import Adaptor.UpdateAdapter;
import Model.Updates;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateActivity extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    Button updateBtn;
    private AlphaAnimation buttonClickAnimation;
    public String userId,postId,userName;
    private DatabaseReference mdatabaseReference;

    String signedUserId;

    public UpdateActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update, container, false);

        userId = getArguments().getString("userId");
        postId = getArguments().getString("postId");
        userName = getArguments().getString("userName");


        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);
        updateBtn = (Button) v.findViewById(R.id.updateBtn);



        recyclerView = (RecyclerView) v.findViewById(R.id.updateRecyclerView);

        mdatabaseReference.child("updateList").orderByChild("joinId").equalTo(userId+"_"+postId).addValueEventListener(new ValueEventListener() {
            ArrayList<Updates> allUpdateList = new ArrayList<Updates>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        Updates singleUpdate = child.getValue(Updates.class);

                        allUpdateList.add(singleUpdate);

                        Log.d("userId",singleUpdate.toString());
                    }
                }


                adapter = new UpdateAdapter(allUpdateList,getActivity());

                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            signedUserId = user.getUid();
        } else {
            signedUserId = "";
        }

        if(signedUserId.equals(userId)){
            updateBtn.setVisibility(View.VISIBLE);
        }else {
            updateBtn.setVisibility(View.GONE);
        }



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                bundle.putString("postId",postId);
                bundle.putString("userName",userName);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                WriteUpdate writeUpdate = new WriteUpdate();
                writeUpdate.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_user_home, writeUpdate);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
