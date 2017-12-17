package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.UUID;

public class updateKelas extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    ImageButton kegiatanImgBtn;
    EditText namaEditText;
    EditText descEditText;
    EditText pertemuanEditText;
    Button updateBtn;
    StorageReference storageReference;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String key;
    String namaPlaceholder;
    String descPlaceholder;
    String pertemuanPlaceholder;
    String imgKelas;


    private Uri imageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_kelas );
        key = getIntent().getStringExtra("key");
        imgKelas = getIntent().getStringExtra( "imageKelas" );
        namaPlaceholder = getIntent().getStringExtra("namaPlaceholder");
        descPlaceholder  = getIntent().getStringExtra( "descPlaceholder" );
        pertemuanPlaceholder = getIntent().getStringExtra( "jmlPertemuan" );
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        kegiatanImgBtn = findViewById(R.id.kegiatan_imgBtn);
        namaEditText = findViewById(R.id.namaKegiatan_Edt);
        descEditText = findViewById(R.id.descKegiatan_Edt);
        pertemuanEditText = findViewById( R.id.jumlahPertemuan_Edt );
        updateBtn = findViewById(R.id.updateBtn);

        namaEditText.setText(namaPlaceholder);
        descEditText.setText( descPlaceholder );
        pertemuanEditText.setText( pertemuanPlaceholder );

        String urlFoto =  imgKelas;
        Picasso.with(this).load(urlFoto).into(kegiatanImgBtn);

        kegiatanImgBtn.setOnClickListener(new View.OnClickListener() {
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

                updateKelas();


            }
        });
    }

    public void updateKelas(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Kelas");
        progressDialog.show();
        final String nama = namaEditText.getText().toString().trim();
        final String desc = descEditText.getText().toString().trim();
        final String jmlPertemuan = pertemuanEditText.getText().toString();

        String id = UUID.randomUUID().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference kelasRef = FirebaseDatabase.getInstance().getReference("kelas").child(key);
        final StorageReference filePath = storageReference.child("Kegiatan_Images").child( id );

        //final String finalNama = nama;

        if(imageUri!=null){
            filePath.putFile(imageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if(nama!=null){
                        kelasRef.child("nama").setValue( nama );
                    }
                    if(desc!=null){
                        kelasRef.child("desc").setValue(desc);
                    }
                    if(jmlPertemuan!=null){
                        kelasRef.child( "jmlPertemuan" ).setValue( jmlPertemuan );
                    }
                    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                    final StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl( imgKelas );
                    photoRef.delete();
                    kelasRef.child("image").setValue(downloadUrl.toString());
                    progressDialog.dismiss();

                    Toast.makeText( getApplicationContext(),"Data Berhasil Di Update",Toast.LENGTH_SHORT ).show();

                    finish();
                }
            }).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( getApplicationContext(),"GAGAL",Toast.LENGTH_LONG ).show();
                }
            } );
        }else{
            if(nama!=null){
                kelasRef.child("nama").setValue( nama );
            }
            if(desc!=null){
                kelasRef.child("desc").setValue(desc);
            }
            if(jmlPertemuan!=null){
                kelasRef.child( "jmlPertemuan" ).setValue( jmlPertemuan );
            }
            progressDialog.dismiss();
            Toast.makeText( getApplicationContext(),"Data Berhasil Di Update",Toast.LENGTH_SHORT ).show();

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
                    .setAspectRatio(16, 9)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                kegiatanImgBtn.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), "Ada Kesalahan :" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult( Activity.RESULT_CANCELED, returnIntent);
        finish();

    }
}
