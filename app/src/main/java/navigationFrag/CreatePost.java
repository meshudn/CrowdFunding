package navigationFrag;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.charity.meshu.charity.R;
import com.charity.meshu.charity.RegisterActivity;
import com.charity.meshu.charity.UserHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import Model.Posts;
import Model.User;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePost extends Fragment implements View.OnClickListener{

    EditText postTitle, postDesc, postRemain, targetFund,tag,location;
    Button createPost;
    LinearLayout uploadImage;
    private int IMAGE_REQ=222;
    private Uri filePath;
    private UploadTask uploadTask;


    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference postList;

    AlphaAnimation buttonClickAnimation;

    public String imageUri;
    Bitmap bitmap;
    ImageView imageView;
    Posts posts;

    String title,desc,remainDay,target,locationTxt,tagTxt;
    private String postKey;
    private Posts incomingPost;
    private boolean imageData = false;

    public CreatePost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_post, container, false);

        UserHome userHome = (UserHome) getActivity();
        //Log.d("userList",userHome.getQueryObject().toString()); // successful
        if(userHome.getQueryObject() != null)
           incomingPost = userHome.getQueryObject();

        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);

        uploadImage = (LinearLayout) v.findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(this);

        postTitle = (EditText) v.findViewById(R.id.postTitle);
        postDesc = (EditText) v.findViewById(R.id.postDesc);
        postRemain = (EditText) v.findViewById(R.id.postRemainDays);
        targetFund = (EditText) v.findViewById(R.id.postTargetFund);
        tag = (EditText) v.findViewById(R.id.postTag);
        location = (EditText) v.findViewById(R.id.postLocation);
        imageView = (ImageView) v.findViewById(R.id.coverPhoto);

        createPost = (Button) v.findViewById(R.id.creatPost);
        createPost.setOnClickListener(this);

        if(incomingPost != null)
           initialTextview();

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseDatabase.getInstance();

        postList = db.getReference("postList");

        return v;
    }

    private void initialTextview() {
        postTitle.setText(incomingPost.title);
        postDesc.setText(incomingPost.desc);
        location.setText(incomingPost.location);
        tag.setText(incomingPost.tag);
        targetFund.setText(incomingPost.fund);
        postRemain.setText(incomingPost.remainDays);

        if(!incomingPost.coverUri.equals("") || incomingPost.coverUri != null){
           Picasso.with(getContext()).load(incomingPost.coverUri).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            imageData = true;
        }
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClickAnimation);
        v.setClickable(false);

        if (v == uploadImage){
            showFileChooser();
            v.setClickable(true);
        }
        if(v == createPost){


             title = postTitle.getText().toString();
             desc = postDesc.getText().toString();
             remainDay = postRemain.getText().toString();
             target = targetFund.getText().toString();
             tagTxt = tag.getText().toString();
             locationTxt = location.getText().toString();

            Boolean result = checkValidation(title,desc,remainDay,target,tagTxt, locationTxt);

            if(result){
                Boolean flag = uploadFile();

            }
            else{
                Toast.makeText(getActivity(), "Please provide all information",
                        Toast.LENGTH_SHORT).show();
            }

            v.setClickable(true);
        }
    }

    private Boolean checkValidation(String title, String desc, String remainDay, String target,String tag,String location) {
       if(!title.equals("") && title != null &&
        !desc.equals("") && desc != null &&
        !remainDay.equals("") && remainDay != null &&
        !tag.equals("") && tag != null &&
        !location.equals("") && location != null &&
        !target.equals("") && target != null
        ){
            return true;
        }
        else
           return false;
    }

    private  void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),IMAGE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();


            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);

                bitmap = getResizedBitmap(bm,bm.getWidth()/3,bm.getHeight()/3);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end of onActivity result method



    private Boolean uploadFile(){
       Boolean flag = false;


        if(filePath != null) {

            StorageReference riversRef = storageRef.child("post/" + filePath.getLastPathSegment());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = riversRef.putBytes(data);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    imageUri = taskSnapshot.getDownloadUrl().toString();

                    Toast.makeText(getActivity(), "Upload complete",
                            Toast.LENGTH_SHORT).show();


                    postKey = postList.push().getKey();

                    posts = new Posts(user.getUid(),postKey,tagTxt,locationTxt,title,desc,imageUri,target,remainDay);

                    postList.child(postKey).setValue(posts);

                    Toast.makeText(getActivity(), "Successfully Posted",
                            Toast.LENGTH_SHORT).show();


                    UserHome userhome = (UserHome) getContext();
                    userhome.switchFragment(posts);

                }
            });

            flag = true;
        }else{
            if(!imageData)
               Toast.makeText(getActivity(), "Image not found",
                    Toast.LENGTH_SHORT).show();
            flag = false;
        }

        if(imageData){

            posts = new Posts(user.getUid(),incomingPost.postId,tagTxt,locationTxt,title,desc,incomingPost.coverUri,target,remainDay);
            postList.child(incomingPost.postId).setValue(posts);

            Toast.makeText(getActivity(), "Updated !",
                    Toast.LENGTH_SHORT).show();

            UserHome userhome = (UserHome) getContext();
            userhome.switchFragment(posts);

        }

        return flag;
    }




    /*
    *  for compressing an image
    * */

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
