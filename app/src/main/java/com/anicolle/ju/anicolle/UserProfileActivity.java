package com.anicolle.ju.anicolle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class UserProfileActivity extends AppCompatActivity {
    private static final int SELECT_FILE = 2;
  //  private static final int REQUEST_CAMERA= 3;
    EditText userNameEditText;
    ImageView userImageProfileView;
    LinearLayout saveProfileBtn;
    private Uri resultUri=null;
    //firebase auth

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String downloadUrl="20";
    //database fields
    private String currentUserID;
    StorageReference UserProfileImagesRef;
    StorageReference mStorageRef;
    private DatabaseReference RootRef;

    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);



        userImageProfileView = (ImageView)findViewById(R.id.userProfileImageView);
        userNameEditText = (EditText) findViewById(R.id.userProfileName);

        saveProfileBtn = (LinearLayout)findViewById(R.id.saveProfile);

        //assign instances

        mAuth = FirebaseAuth.getInstance();
        //progress dialog
        currentUserID = mAuth.getCurrentUser().getUid();
        mProgress = new ProgressDialog(this);

        //firebase database instance

         UserProfileImagesRef= FirebaseStorage.getInstance().getReference().child("User_Profile");
        RootRef = FirebaseDatabase.getInstance().getReference();
      //  mStorageRef = FirebaseStorage.getInstance().getReference();
        // onclick save profile

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //logic for saving user profile
            saveUserProfile();


            }
        });

        //user imageview onclick listener

        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProgress.setTitle("");
                //logic for picking image

                profilePicSelection();
            }
        });
    }

    private void saveUserProfile() {

        final String username = userNameEditText.getText().toString();


        if( TextUtils.isEmpty(username ) && (downloadUrl.equals("20")) && resultUri==null ){

                Toast.makeText(this, "Please complete your profile", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(username) && (downloadUrl.equals("20")) && resultUri != null){
            Toast.makeText(this, "Please entry your username", Toast.LENGTH_SHORT).show();
        }else if (!TextUtils.isEmpty(username) && (downloadUrl.equals("20")) && resultUri == null){
            Toast.makeText(this, "Please choose your profile picture", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(username) && resultUri!= null){

                mProgress.setTitle("Saving profile");
                mProgress.setMessage("Please wait...");
                mProgress.show();

                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username")
                        .equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(UserProfileActivity.this, "choose a different username", Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, String> profileMap = new HashMap<>();
                            profileMap.put("username",username);
                            profileMap.put("userid",currentUserID);
                            profileMap.put("imageurl",resultUri.toString());
                            RootRef.child("Users").child(currentUserID).setValue(profileMap);
                            SendtoMainActivity();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }



    }

    private void profilePicSelection() {

    final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");

        //Set items and listeners
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Library")){
                    galleryIntent();
                }else if (items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
                builder.show();
    }



    private void galleryIntent() {

        Log.d("gola","entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);

    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //save uri from gallery
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK ){
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
               resultUri = result.getUri();
                userImageProfileView.setImageURI(resultUri);
                final StorageReference filePath = UserProfileImagesRef.child(currentUserID+".jpg");


                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                RootRef.child("Users").child(currentUserID).child("imageurl").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            final String dd;
                                            Toast.makeText(UserProfileActivity.this, "Saved in database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String message = task.getException().toString();
                                            Toast.makeText(UserProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        });


                    }
                });
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error =result.getError();
            }
        }

    }
    private void SendtoMainActivity(){
        Intent mainIntent = new Intent(UserProfileActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
