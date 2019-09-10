package com.example.lovers_hub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mUserDatabase;
    private FirebaseUser CurrentUser;
    private CircleImageView SettingsImg;
    private TextView SettingsName,SettingsStatus;
    private Button SettingsStatus_btn,SettingsImg_btn;
    String name,image,status,thumb_image;
    private StorageReference ImageStorage;
    Uri resultImgUri;
    UploadTask uploadTask;
    UploadTask uploadTaskThumb;
    byte[] thumbByte;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsImg=findViewById(R.id.SettingsImg);
        SettingsName=findViewById(R.id.SettingsName);
        SettingsStatus=findViewById(R.id.SettingsStatus);
        SettingsStatus_btn=findViewById(R.id.SettingsStatus_btn);
        SettingsImg_btn=findViewById(R.id.SettingsImg_btn);
        SettingsStatus_btn.setOnClickListener(this);
        SettingsImg_btn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        //.......................................................
        CurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String CurrentUserId=CurrentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             name =dataSnapshot.child("name").getValue().toString();
             image=dataSnapshot.child("img").getValue().toString();
             status=dataSnapshot.child("status").getValue().toString();
             thumb_image=dataSnapshot.child("thumb_image").getValue().toString();
             SettingsName.setText(name);
             SettingsStatus.setText(status);
             if(!image.isEmpty()) {
             Picasso.get().load(image)
                        .placeholder(R.mipmap.avatar)
                        .into(SettingsImg);


            }}



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageStorage= FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.SettingsStatus_btn:
        Intent StatusIntent= new Intent(getApplicationContext(),ActivityStatus.class);
        StatusIntent.putExtra("status",status);
        startActivity(StatusIntent);
        break;
    case R.id.SettingsImg_btn:

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1,1)
                .start(this);

       /* Intent GallaryIntent=new Intent();
        GallaryIntent.setType("image/*");
        GallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(GallaryIntent,"Select an image"),GallaryPickCode);
        break;*/




}
    }

    @Override
    public void onBackPressed() {
        Intent MainIntent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainIntent);
        finish();
        super.onBackPressed();
    }



@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            progressDialog.setTitle("Uploading Image...");
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            resultImgUri = result.getUri();
            File thumm_filepath=new File(resultImgUri.getPath());
            String Current_user_id=CurrentUser.getUid();
            try {
                Bitmap thumb_Bitmab = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(70)
                        .compressToBitmap(thumm_filepath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_Bitmab.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                thumbByte = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


            final StorageReference FilePath=ImageStorage.child("ProfileImages").child(Current_user_id+".jpg");
            StorageReference thumb_filepath=ImageStorage.child("ProfileImages").child("thumbs").child(Current_user_id+".jpg");
            uploadTask=FilePath.putFile(resultImgUri);
            uploadTaskThumb=thumb_filepath.putBytes(thumbByte);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return FilePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();

                        mUserDatabase.child("img").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Toast.makeText(getApplicationContext(),"Image updated Successfully",Toast.LENGTH_LONG).show();

                                    Task<Uri> uriTask_thumb=uploadTaskThumb.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()){
                                                throw task.getException();

                                            }
                                            return thumb_filepath.getDownloadUrl();

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri downloadUri_thumb=task.getResult();
                                            String downloadURL_thumb=downloadUri_thumb.toString();
                                            mUserDatabase.child("thumb_image").setValue(downloadURL_thumb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){


                                                        Toast.makeText(getApplicationContext(),"Thumb updated Successfully",Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();

                                                    }else {

                                                        Toast.makeText(getApplicationContext(),"Thumb Update Failed",Toast.LENGTH_LONG).show();


                                                    }
                                                }
                                            });

                                        }
                                    });







                                }

                            }
                        });





                    } else {

                        Toast.makeText(getApplicationContext(),"Failed to upload the image Please, try again!",Toast.LENGTH_LONG).show();

                    }
                }
            });





        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }



}
/*public  String MakeRandom(){

    Random rand = new Random();
    StringBuilder stringBuilder=new StringBuilder();
   int RandomLength= rand.nextInt(100);
   char temp;
   for(int i =0;i<RandomLength;i++){

       temp =(char) (rand.nextInt(96)+36);
       stringBuilder.append(temp);


   }

        return stringBuilder.toString();

}*/







}
