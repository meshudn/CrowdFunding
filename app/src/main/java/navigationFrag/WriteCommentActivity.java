package navigationFrag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charity.meshu.charity.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.*;
import Model.Comments;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteCommentActivity extends Fragment implements View.OnClickListener{

    EditText commentBox;
    Button commentSubmit,commentCancle;
    private AlphaAnimation buttonClickAnimation;

    DatabaseReference mdatabaseReference;

    private String userId,postId;
    private String userName;

    public WriteCommentActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_write_comment, container, false);

        userId = getArguments().getString("userId");
        postId = getArguments().getString("postId");
        userName = getArguments().getString("userName");


        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

        commentBox = (EditText) v.findViewById(R.id.commentDesc);
        commentSubmit = (Button) v.findViewById(R.id.commentSubmit);
        commentCancle = (Button) v.findViewById(R.id.commentCancle);

        commentSubmit.setOnClickListener(this);
        commentCancle.setOnClickListener(this);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("commentList");

        return v;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClickAnimation);



        if(v == commentSubmit){
            String str = commentBox.getText().toString();
            Model.Comments comments = new Comments(userId,postId,str,userName);
            mdatabaseReference.push().setValue(comments);
            Toast.makeText(getActivity(), "done!",
                    Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        if(v == commentCancle){
            getActivity().getSupportFragmentManager().popBackStack();
        }


    }
}
