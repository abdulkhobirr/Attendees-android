package com.example.iqbalzauqul.attendees;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PesertaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user;
    Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_peserta);
        fetchUser();
        fetchView();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view_peserta);
        //Membold teks pada posisi activity di nav drawer

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetchView() {
        final EditText kodeKelas = findViewById(R.id.kode_kelas_edt);
        Button join = (Button) findViewById(R.id.joinBtn);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kode = kodeKelas.getText().toString();
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("kelas").child(kode);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference ref = mDatabase.child("nama");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String nama = dataSnapshot.getValue().toString();
                                    kodeJoin(nama, kode);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else {
                            kodeGagal();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    private void kodeGagal() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PesertaActivity.this);
        builder1.setMessage("Kode kelas tidak ditemukan. Harap periksa kembali.");
        builder1.setCancelable(true);


        builder1.setNegativeButton(
                "DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void kodeJoin(String nama, final String kode) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PesertaActivity.this);
        builder1.setMessage("Anda yakin ingin mengikuti kelas: " + nama + "?");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "JOIN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showChangeLangDialog(kode);
                    }
                });

        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pengabsen) {
            finish();

            // Handle the camera action
        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PesertaActivity.this, LoginActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_peserta);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = findViewById(R.id.nav_view_peserta);
        navigationView.setCheckedItem(R.id.nav_peserta);
    }

    public void showChangeLangDialog(final String kode) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Mengikuti Kelas");
        dialogBuilder.setMessage("Masukan Nomor Identitas");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String nim = edt.getText().toString();
                String nama = user.getDisplayName();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pesertaKelas").child(kode)
                        .push();
                databaseReference.child("nama").setValue(nama);
                databaseReference.child("nomorIdentitas").setValue(nim);
                Toast.makeText(PesertaActivity.this, "Berhasil join kelas.", Toast.LENGTH_LONG).show();
                dialog.cancel();

                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void fetchUser() {

        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_peserta);
        navigationView.setNavigationItemSelectedListener(this);
        final View navHeader = navigationView.getHeaderView(0);

        final TextView navUser = navHeader.findViewById(R.id.namaUserDrawer);

        user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                navUser.setText(user.getDisplayName());


                TextView navEmail = navHeader.findViewById(R.id.emailUserDrawer);
                navEmail.setText(user.getEmail());

            }
        });


    }


}


