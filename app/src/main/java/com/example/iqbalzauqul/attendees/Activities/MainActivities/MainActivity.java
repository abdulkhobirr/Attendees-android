package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iqbalzauqul.attendees.Activities.SignupOrLogin.LoginActivity;
import com.example.iqbalzauqul.attendees.Models.Kelas;
import com.example.iqbalzauqul.attendees.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

//Activity utama

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FirebaseUser user;
    String uid;
    CoordinatorLayout coordinatorLayout;
    private DatabaseReference mDatabase;
    private Query mQuery;

    // Method yang dipanggil saat membuka aplikasi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchView();
        fetchUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("kelas");
        mQuery = mDatabase.orderByChild("uid").equalTo(uid);


        recyclerView = findViewById(R.id.recycleViewKelas);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(glm);

        registerForContextMenu( recyclerView );

        if (getIntent().hasExtra("signup")) {

            Toast.makeText(MainActivity.this, "Sign-Up Success", Toast.LENGTH_LONG).show();
        }
        coordinatorLayout = findViewById(R.id.coor_main);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Halo,  " + user.getDisplayName(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        snackbar.show();
    }

    private void fetchView() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setSupportActionBar(toolbar);

        //
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddKegiatanActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        //Membold teks pada posisi activity di nav drawer
        navigationView.setCheckedItem(R.id.nav_pengabsen);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetchUser() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        NavigationView navigationView = findViewById(R.id.nav_view);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pengabsen) {

            // Handle the camera action
        } else if (id == R.id.nav_peserta) {
            Intent intent = new Intent(MainActivity.this, PesertaActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Membold teks pada posisi activity di nav drawer
        navigationView.setCheckedItem(R.id.nav_pengabsen);

        final FirebaseRecyclerAdapter<Kelas, KelasViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Kelas, KelasViewHolder>(
                Kelas.class,
                R.layout.row_kelas,
                KelasViewHolder.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(final KelasViewHolder viewHolder, final Kelas model, final int position) {
                final String key = getRef(position).getKey();
                viewHolder.setNama(model.getnama());
                viewHolder.setDesc(model.getdesc());
                viewHolder.setImage(getApplicationContext(), model.getimage());

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v){
                        CharSequence menu[] = new CharSequence[] {"Update", "Delete", "Cancel"};

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);

                        builder1.setCancelable(true);
                        builder1.setTitle(model.getnama())
                        .setIcon(R.drawable.header);
                        final AlertDialog b = builder1.create();

                        builder1.setItems(menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        Intent intent = new Intent( MainActivity.this, updateKelas.class );
                                        intent.putExtra("key", key);
                                        intent.putExtra( "jmlPertemuan",model.getJmlPertemuan() );
                                        intent.putExtra( "namaPlaceholder",model.getnama() );
                                        intent.putExtra( "descPlaceholder", model.getdesc() );
                                        intent.putExtra("imageKelas",model.getimage());
                                        b.cancel();
                                        startActivity( intent );
                                        break;

                                    case 1:
                                        final String link = model.getimage();
                                        DatabaseReference drKelas = FirebaseDatabase.getInstance().getReference("kelas").child( key );
                                        final  DatabaseReference drPesertaKelas = FirebaseDatabase.getInstance().getReference("pesertaKelas").child( key );
                                        final DatabaseReference drPertemuan = FirebaseDatabase.getInstance().getReference("pertemuan").child( key );

                                        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                                        final StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl( link );

                                        drKelas.removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                drPesertaKelas.removeValue();
                                                drPertemuan.removeValue();
                                                photoRef.delete();
                                                b.cancel();
                                                Toast.makeText( getApplicationContext(), "Kelas Telah Dihapus", Toast.LENGTH_LONG ).show();
                                            }
                                        } ).addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText( getApplicationContext(), "GAGAL", Toast.LENGTH_LONG ).show();
                                            }
                                        } );
                                        break;

                                    case 2:
                                        b.cancel();
                                }
                            }
                        });
                        builder1.show();

                        return true;
                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra( "descPlaceholder", model.getdesc() );
                        intent.putExtra("nama", model.getnama());
                        intent.putExtra( "kelasbg",model.getimage() );
                        intent.putExtra("key", key);
                        startActivity(intent);

                    }
                });



            }


        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class KelasViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public KelasViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setNama(String nama) {

            TextView namaKelas = mView.findViewById(R.id.lbl_kelasID);
            namaKelas.setText(nama);
        }

        public void setDesc(String desc) {
            TextView descKelas = mView.findViewById(R.id.lbl_pengabsenID);
            descKelas.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView imageKelas = mView.findViewById(R.id.imageID);
            Picasso.with(ctx).load(image).into(imageKelas);
        }

    }

}
