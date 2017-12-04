package com.example.iqbalzauqul.attendees;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

//Activity utama

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;

    List<Kelas> itemList;
    FirebaseUser user;
    String uid;
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

        if (getIntent().hasExtra("signup")) {

            Toast.makeText(MainActivity.this, "Sign-Up Success", Toast.LENGTH_LONG).show();
        }


    }

    private void fetchView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setSupportActionBar(toolbar);

        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, tambahKegiatanActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetchUser() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Kelas, KelasViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Kelas, KelasViewHolder>(
                Kelas.class,
                R.layout.row_kelas,
                KelasViewHolder.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(final KelasViewHolder viewHolder, final Kelas model, int position) {
                viewHolder.setNama(model.getnama());
                viewHolder.setDesc(model.getdesc());
                viewHolder.setImage(getApplicationContext(), model.getimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("nama", model.getnama());
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
