package com.example.iqbalzauqul.attendees;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class TambahPesertaActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    EditText namaField;
    EditText IdField;
    Button addButton;
    ImageButton imageButton;
    StorageReference storageReference;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_peserta);

        namaField = findViewById(R.id.namaPeserta_txt);
        IdField = findViewById(R.id.nomorID_txt);
        addButton = findViewById(R.id.addButton);
        imageButton = findViewById(R.id.imageButton_avatarPeserta);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama = namaField.getText().toString();
                String id = IdField.getText().toString();
                if (!nama.isEmpty() && !id.isEmpty() && imageUri != null) {
                    addPeserta(nama, id);
                } else {
                    Toast.makeText(TambahPesertaActivity.this, "Harap Masukan Semua Field", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void addPeserta(final String nama, final String id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menambah Pertemuan");
        progressDialog.show();

        String key = getIntent().getStringExtra("key");

        final DatabaseReference datRef = FirebaseDatabase.getInstance().getReference().child("pesertaKelas").child(key).push();

        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference filePath = storageReference.child("Avatar").child(id);
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                datRef.child("nama").setValue(nama);
                datRef.child("nomorIdentitas").setValue(id);
                datRef.child("avatar").setValue(downloadUrl.toString());
                progressDialog.dismiss();


                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(TambahPesertaActivity.this, "Terjadi Kesalahan" + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                imageButton.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(TambahPesertaActivity.this, "Ada Kesalahan :" + error, Toast.LENGTH_SHORT).show();
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
