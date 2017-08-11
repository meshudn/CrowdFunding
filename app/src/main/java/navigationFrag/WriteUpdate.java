package navigationFrag;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.charity.meshu.charity.R;
import com.charity.meshu.charity.UserHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Model.Posts;
import Model.Updates;
import Model.ValidateString;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteUpdate extends Fragment implements View.OnClickListener{

    EditText updateBox,updateTitle;
    Button updateSubmit,updateCancle;

    DatabaseReference mdatabaseReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase db;


    String userName,userId,postId;
    LinearLayout uploadImage;
    private AlphaAnimation buttonClickAnimation;
    private String imageUri="";
    private int IMAGE_REQ=300;

    Bitmap bitmap;
    private Uri filePath;
    ImageView  imageView;

    public WriteUpdate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_write_update, container, false);

        userId = getArguments().getString("userId");
        postId = getArguments().getString("postId");
        userName = getArguments().getString("userName");

        updateBox = (EditText) v.findViewById(R.id.updateDesc);
        updateTitle = (EditText) v.findViewById(R.id.updateTitle);
        updateSubmit = (Button) v.findViewById(R.id.updateSubmit);
        updateCancle = (Button) v.findViewById(R.id.updateCancle);
        uploadImage = (LinearLayout) v.findViewById(R.id.uploadImage);
        imageView = (ImageView) v.findViewById(R.id.coverPhoto);

        updateSubmit.setOnClickListener(this);
        updateCancle.setOnClickListener(this);
        uploadImage.setOnClickListener(this);


        buttonClickAnimation = new AlphaAnimation(1F, 0.6F);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseDatabase.getInstance();
        mdatabaseReference = db.getReference("updateList");

        return v;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClickAnimation);
        v.setClickable(false);

        if(uploadImage == v){
            showFileChooser();
            v.setClickable(true);
        }


        if(v == updateSubmit){
            String str = updateBox.getText().toString();
            String title = updateTitle.getText().toString();

            uploadImageFile(str,title);

            if(filePath == null && !str.equals("") && !str.equals("")){
                Updates updates = new Updates(userId,postId,str,userName,title,"");
                mdatabaseReference.push().setValue(updates);
                Toast.makeText(getActivity(), "done!",
                        Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                Toast.makeText(getActivity(), "Please provide all information",
                        Toast.LENGTH_SHORT).show();
            }

            v.setClickable(true);
        }

        if(v == updateCancle){
            getActivity().getSupportFragmentManager().popBackStack();

            v.setClickable(true);
        }
    }



    /*private Boolean checkValidation(ArrayList<ValidateString> checkList) {

        for(int i=0;i< checkList.size();i++){
            if(!checkList.get(i).getValue().equals("") && checkList.get(i).getValue() != null
                    && checkList.get(i).getIgnore()){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }
*/

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





    private void uploadImageFile(final String str,final String title){



        if(filePath != null ) {

            StorageReference riversRef = storageRef.child("update/" + filePath.getLastPathSegment());
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

                    imageUri = taskSnapshot.getDownloadUrl().toString();

                    Toast.makeText(getActivity(), "Upload complete",
                            Toast.LENGTH_SHORT).show();


                    Updates updates = new Updates(userId,postId,str,userName,title,imageUri);
                    mdatabaseReference.push().setValue(updates);
                    Toast.makeText(getActivity(), "done!",
                            Toast.LENGTH_SHORT).show();


                    Toast.makeText(getActivity(), "Successfully Posted",
                            Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

        }else{
            Toast.makeText(getActivity(), "Image not found",
                    Toast.LENGTH_SHORT).show();

        }
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
