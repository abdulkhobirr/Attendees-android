package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.iqbalzauqul.attendees.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class updatePeserta extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1;
    ImageButton avatarBtn;
    EditText namaEditText;
    EditText IdEditText;
    Button updateBtn;
    StorageReference storageReference;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String key;
    String kode;
    String namaPlaceholder;
    String IdPlaceholder;
    String avatar;


    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_peserta );
        key = getIntent().getStringExtra("key");
        kode = getIntent().getStringExtra("kode");
        avatar = getIntent().getStringExtra( "avatar" );
        namaPlaceholder = getIntent().getStringExtra("namaPlaceholder");
        IdPlaceholder  = getIntent().getStringExtra( "IdPlaceholder" );
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        avatarBtn = findViewById(R.id.avatarButton);
        namaEditText = findViewById(R.id.namaKegiatan_Edt);
        IdEditText = findViewById(R.id.updateID);
        updateBtn = findViewById(R.id.tambahPertemuan_Btn);

        namaEditText.setText(namaPlaceholder);
        IdEditText.setText( IdPlaceholder );

        String urlFoto =  avatar;
        Picasso.with(this).load(urlFoto).into(avatarBtn);
        avatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePeserta();


            }
        });
    }

    public void updatePeserta(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Peserta");
        progressDialog.show();
        final String nama = namaEditText.getText().toString().trim();
        final String nomorIdentitas = IdEditText.getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference pesertaRef = FirebaseDatabase.getInstance().getReference("pesertaKelas").child(key).child(kode);
        final StorageReference filePath = storageReference.child("Avatar").child( nomorIdentitas );

        //final String finalNama = nama;

        if(imageUri!=null){
            filePath.putFile(imageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if(nama!=null){
                        pesertaRef.child("nama").setValue( nama );
                    }
                    if(nomorIdentitas!=null){
                        pesertaRef.child("nomorIdentitas").setValue(nomorIdentitas);
                    }
                    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                    final StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl( avatar );
                    photoRef.delete();
                    pesertaRef.child("avatar").setValue(downloadUrl.toString());
                    progressDialog.dismiss();

                    finish();
                }
            }).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( getApplicationContext(),"GAGAL",Toast.LENGTH_LONG ).show();
                }
            } );
        }else{
            progressDialog.show();
                        if(nama!=null){
                            pesertaRef.child("nama").setValue( nama );
                        }
                        if(nomorIdentitas!=null){
                            pesertaRef.child("nomorIdentitas").setValue(nomorIdentitas);
                        }
                        progressDialog.dismiss();
                        Toast.makeText( getApplicationContext(),"Data Berhasil Di Update",Toast.LENGTH_LONG ).show();

                        finish();

                    }
                }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                avatarBtn.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), "Ada Kesalahan :" + error, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }
}
