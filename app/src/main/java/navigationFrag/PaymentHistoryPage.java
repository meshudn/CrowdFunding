package navigationFrag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.charity.meshu.charity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adaptor.PaymentHistoryAdapter;
import Adaptor.UpdateAdapter;
import Model.Payments;
import Model.Updates;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentHistoryPage extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;


    private AlphaAnimation buttonClickAnimation;
    public String userId;
    private DatabaseReference mdatabaseReference;

    String signedUserId;

    public PaymentHistoryPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_history_page, container, false);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference();

        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

        recyclerView = (RecyclerView) v.findViewById(R.id.payRecyclerView);


        /*
        *  get user id by auth checking
        * */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            signedUserId = user.getUid();
        } else {
            signedUserId = "";
        }

        /* ------  --------------------------------- */



        mdatabaseReference.child("paymentList").orderByChild("userId").equalTo(signedUserId).addValueEventListener(new ValueEventListener() {
            ArrayList<Payments> allUpdateList = new ArrayList<Payments>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                if(children != null) {
                    for (DataSnapshot child : children) {
                        Payments payments = child.getValue(Payments.class);

                        allUpdateList.add(payments);

                        Log.d("Payment Class object",payments.toString());
                    }
                }


                adapter = new PaymentHistoryAdapter(allUpdateList,getActivity());

                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* ----------------------------- --------------------------- */

        return v;
    }

}
