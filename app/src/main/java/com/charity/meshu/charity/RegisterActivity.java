package com.charity.meshu.charity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Model.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    DatabaseReference userList;

    EditText Email,Password,ConfirmPassword,ContactNumber,NIDnumber,userName;
    String email,password,confirmPassword,contactNumber,nidNumer;
    private Button Register;
    LinearLayout uploadImage;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private static final int IMAGE_REQ = 200 ;
    private Uri filePath;

    private UploadTask uploadTask;
    private String fullPath="";
    private boolean flag = false;
    private Bitmap bitmap;

    String userId;
    private AlphaAnimation buttonClickAnimation;
    private String userNameTxt;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonClickAnimation = new AlphaAnimation(1F, 0.4F);

        userName = (EditText) findViewById(R.id.userNameTv);
        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.FirstPassword);
        ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        ContactNumber = (EditText) findViewById(R.id.ContactNumber);
        NIDnumber = (EditText) findViewById(R.id.NIDNumber);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();



        Register = (Button) findViewById(R.id.Register);
        uploadImage = (LinearLayout) findViewById(R.id.uploadImage);
        imageView = (ImageView) findViewById(R.id.coverPhoto);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                v.setClickable(false);

                userNameTxt = userName.getText().toString();
                email = Email.getText().toString();
                password = Password.getText().toString();
                confirmPassword = ConfirmPassword.getText().toString();
                contactNumber = ContactNumber.getText().toString();
                nidNumer = NIDnumber.getText().toString();


             if(password.equals(confirmPassword)) {
                 mAuth.createUserWithEmailAndPassword(email, password)
                         .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     // Sign in success, update UI with the signed-in user's information
                                     FirebaseUser user = mAuth.getCurrentUser();

                                     Toast.makeText(RegisterActivity.this, "Authentication successful.",
                                             Toast.LENGTH_SHORT).show();

                                     uploadFile();

                                     userId = user.getUid().toString();

                                     if(filePath == null){
                                         updateDatabase(userId,nidNumer,contactNumber,userNameTxt);

                                         Intent intent = new Intent(RegisterActivity.this, UserHome.class);
                                         startActivity(intent);
                                         finish();
                                     }

                                 } else {
                                     // If sign in fails, display a message to the user.
                                     Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                             Toast.LENGTH_SHORT).show();

                                 }
                             }
                         });


             }else{
                 Toast.makeText(RegisterActivity.this, "Password not matched.",
                         Toast.LENGTH_SHORT).show();
             }


            }


        }); // end of register button click listener method.


        /*
        *  Click action for upload image button
        * */

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                showFileChooser();
            }
        });
    } //end of on create method...


    private  void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                bitmap = getResizedBitmap(bm,bm.getWidth()/3,bm.getHeight()/3);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end of onActivity result method

    private void uploadFile(){

        if(filePath != null ) {
         /*   final ProgressDialog progressDialog = new ProgressDialog(this);*/
        /*    progressDialog.setTitle("Processing...");
            progressDialog.show();*/

            StorageReference riversRef = storageRef.child("images/" + filePath.getLastPathSegment());
         /*   uploadTask = riversRef.putFile(filePath);*/

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

                     fullPath = taskSnapshot.getDownloadUrl().toString();

                    Toast.makeText(RegisterActivity.this, "Image Upload Completed",
                            Toast.LENGTH_SHORT).show();


                    updateDatabase(userId,nidNumer,contactNumber,userNameTxt);

                    Intent intent = new Intent(RegisterActivity.this, UserHome.class);
                    startActivity(intent);
                    finish();


                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                   double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage((int) progress + "% Processing...");
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "Image not found",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void updateDatabase(String userId, String nid,String phone,String userName){
        db = FirebaseDatabase.getInstance();

        userList = db.getReference("userList");

        User user = new User(userId,phone,nid,fullPath,userName);

        userList.push().setValue(user);
    }


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
