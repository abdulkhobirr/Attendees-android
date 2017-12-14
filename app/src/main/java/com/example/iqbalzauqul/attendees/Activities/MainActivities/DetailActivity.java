package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.iqbalzauqul.attendees.Models.PesertaList;
import com.example.iqbalzauqul.attendees.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.app.PendingIntent.getActivity;

public class DetailActivity extends AppCompatActivity {

    Menu collapsedMenu;
    //    private Query mQuery;
    RecyclerView recyclerView;
    String key;
    CoordinatorLayout coordinatorLayout;
    private boolean appBarExpanded;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        fetchView();
        fetchRecyclerView();
        coordinatorLayout = findViewById(R.id.coor_detail);

        key = getIntent().getStringExtra("key");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pesertaKelas").child(key);



    }

    private void fetchRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewPeserta);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.drawable_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }


    private void fetchView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_detail_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClicked();
            }
        });

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String tes = getIntent().getStringExtra("nama");
        collapsingToolbar.setTitle(tes);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarExpanded = true;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.v("offset", String.valueOf(verticalOffset));
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (verticalOffset < (-200)) {
                    Log.v("offset", String.valueOf(verticalOffset));
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
    }

    private void fabClicked() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);
        builder1.setMessage(Html.fromHtml("Bagikan kode ini ke peserta yang telah memiliki akun untuk mengikuti kelas anda: "
                        +"<b>" + key + "</b>"+"<br/>"+"<br/>"+"<br/>"+
                "Jika ada peserta yang belum memiliki akun, anda bisa menambah peserta secara manual."));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "TAMBAH MANUAL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(DetailActivity.this, AddPesertaActivity.class);
                        intent.putExtra("key", key);
                        startActivityForResult(intent, 1);
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
//
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        collapsedMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (collapsedMenu != null
                && (!appBarExpanded)) {
            int size = collapsedMenu.size();

            //collapsed
            collapsedMenu.add("Add")
                    .setIcon(R.drawable.ic_add_white)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PesertaList, PesertaViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PesertaList, PesertaViewHolder>(
                PesertaList.class,
                R.layout.peserta_dalam_kelas_cardview,
                PesertaViewHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(PesertaViewHolder viewHolder, PesertaList model, int position) {
                viewHolder.setNomorIdentitas(model.getNomorIdentitas());
                viewHolder.setNama(model.getNama());
                viewHolder.setAvatar(getApplicationContext(), model.getAvatar());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle() == "Add") {
            fabClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Peserta berhasil ditambahkan.", Snackbar.LENGTH_LONG);


                snackbar.show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

    public static class PesertaViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PesertaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setNomorIdentitas(String nomorIdentitas) {
            TextView noIdPeserta = mView.findViewById(R.id.id_textview);
            noIdPeserta.setText(nomorIdentitas);

        }

        private void setNama(String nama) {
            TextView namaPeserta = mView.findViewById(R.id.nama_peserta_txt);
            namaPeserta.setText(nama);

        }

        public void setAvatar(Context ctx, String avatar) {
            ImageView imageAvatar = mView.findViewById(R.id.avatar_imageview);
            Picasso.with(ctx).load(avatar).into(imageAvatar);
        }
    }
}


