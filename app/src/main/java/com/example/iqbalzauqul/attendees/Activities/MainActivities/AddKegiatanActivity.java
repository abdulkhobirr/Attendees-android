package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.UUID;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.RandomStringUtils;

public class AddKegiatanActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    ImageButton kegiatanImageButton;
    EditText namaKegiatanEditText;
    EditText descKegiatanEditText;
    EditText jmlPertemuanEditText;
    Button tambahKegiatanButton;
    StorageReference storageReference;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kegiatan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        kegiatanImageButton = findViewById(R.id.kegiatan_imgBtn);
        namaKegiatanEditText = findViewById(R.id.namaKegiatan_Edt);
        descKegiatanEditText = findViewById(R.id.descKegiatan_Edt);
        jmlPertemuanEditText = findViewById(R.id.jumlahPertemuan_Edt);
        tambahKegiatanButton = findViewById(R.id.tambahPertemuan_Btn);

        kegiatanImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
        tambahKegiatanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addKegiatan();


            }
        });


    }

    private void addKegiatan() {


        final String nama = namaKegiatanEditText.getText().toString().trim();
        final String desc = descKegiatanEditText.getText().toString().trim();
        final String jmlP = jmlPertemuanEditText.getText().toString();


        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(desc) && imageUri != null && !TextUtils.isEmpty(jmlP)) {
            progressDialog.setMessage("Menambah Pertemuan");
            progressDialog.show();
            String id = UUID.randomUUID().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = user.getUid();
            database = FirebaseDatabase.getInstance();
            String kodeKelas = RandomStringUtils.randomAlphanumeric(4, 6);
            final DatabaseReference kelasRef = database.getReference("kelas").child(kodeKelas);


            StorageReference filePath = storageReference.child("Kegiatan_Images").child(id);

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    kelasRef.child("nama").setValue(nama);
                    kelasRef.child("desc").setValue(desc);
                    kelasRef.child("jmlPertemuan").setValue(jmlP);
                    kelasRef.child("uid").setValue(uid);
                    kelasRef.child("image").setValue(downloadUrl.toString());


                    progressDialog.dismiss();
                    finish();



                }


            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddKegiatanActivity.this, "Terjadi Kesalahan" + e, Toast.LENGTH_SHORT).show();
                        }
                    });



        } else {
            Toast.makeText(AddKegiatanActivity.this, "Harap Masukan Semua Field", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16, 9)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                kegiatanImageButton.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AddKegiatanActivity.this, "Ada Kesalahan :" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
