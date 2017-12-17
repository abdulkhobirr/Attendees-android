package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Target;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.iqbalzauqul.attendees.Models.PesertaList;
import com.example.iqbalzauqul.attendees.R;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
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
    ImageView Header;
    String kelasbg;
    private com.squareup.picasso.Target loadtarget;
    CollapsingToolbarLayout collapsingToolbar;
    FloatingActionButton fabAbsen;
    int m;
    Bitmap mBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        coordinatorLayout = findViewById(R.id.coor_detail);
        kelasbg = getIntent().getStringExtra( "kelasbg" );
        key = getIntent().getStringExtra("key");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pesertaKelas").child(key);
        ImageView Header = findViewById(R.id.header);
        String urlFoto =  kelasbg;
        Picasso.with(this).load(urlFoto).into(Header);
        loadBitmap(urlFoto);
        fetchView();
        fetchRecyclerView();













    }
    public void loadBitmap(final String url) {

        if (loadtarget == null) loadtarget = new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(url).into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap b) {
        mBit = b;


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
        Toolbar toolbar =  findViewById(R.id.anim_toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_detail_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

         fabAbsen = findViewById(R.id.fab_absen);
         fabAbsen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 fabClicked();
             }
         });


        setSupportActionBar(toolbar);
         collapsingToolbar =  findViewById(R.id.collapsing_toolbar);
        Palette.from(mBit).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
               int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });
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
                    .setIcon(R.drawable.ic_create_black_24dp)
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
            protected void populateViewHolder(PesertaViewHolder viewHolder, final PesertaList model, int position) {
                final ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this);
                progressDialog.setMessage("Menghapus Peserta");
                final String kode = getRef(position).getKey();
                viewHolder.setNomorIdentitas(model.getNomorIdentitas());
                viewHolder.setNama(model.getNama());
                viewHolder.setAvatar(getApplicationContext(), model.getAvatar());
                viewHolder.setPresentase(model.getProgress());

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v){
                        final String ava = model.getAvatar();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this, R.style.MyAlertDialogStyle);
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.update_dialog, null);
                        builder1.setView(dialogView);

                        final Button deleteBtn = dialogView.findViewById( R.id.buttonDelete );
                        final Button updateBtn = dialogView.findViewById( R.id.buttonUpdate );
                        final Button cancelBtn = dialogView.findViewById( R.id.buttonCancel );

                        builder1.setCancelable(true);
                        builder1.setTitle(model.getNama());
                        final AlertDialog b = builder1.create();
                        b.show();

                        deleteBtn.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressDialog.show();
                                DatabaseReference drPeserta = FirebaseDatabase.getInstance().getReference("pesertaKelas").child( key ).child( kode );




                                drPeserta.removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Cek apakah avatar null? (kasus join kelas via kode tanpa avatar)
                                        if(model.getAvatar()!=null){
                                            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                                            StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl( ava );
                                            photoRef.delete();
                                        }

                                        b.cancel();
                                        Toast.makeText( getApplicationContext(), model.getNama()+"  Telah Dihapus", Toast.LENGTH_LONG ).show();
                                        progressDialog.dismiss();
                                        //TO DO CODE
                                    }
                                } ).addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText( getApplicationContext(), "GAGAL", Toast.LENGTH_LONG ).show();
                                        //TO DO CODE
                                    }
                                } );
                            }
                        } );

                        updateBtn.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent( DetailActivity.this, updatePeserta.class );
                                intent.putExtra("key", key);
                                intent.putExtra("kode", kode);
                                intent.putExtra( "namaPlaceholder",model.getNama() );
                                intent.putExtra( "IdPlaceholder", model.getNomorIdentitas() );
                                intent.putExtra("avatar",model.getAvatar());
                                b.cancel();
                                startActivity( intent );
                            }
                        } );

                        cancelBtn.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                b.cancel();
                            }
                        } );

                        return true;
                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAbsen.getVisibility() == View.VISIBLE) {
                    fabAbsen.hide();
                } else if (dy < 0 && fabAbsen.getVisibility() != View.VISIBLE) {
                    fabAbsen.show();
                }
            }
        });
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
                Toast.makeText(this,"Berhasil ditambahkan.",Toast.LENGTH_LONG).show();
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

        private void setPresentase(int presentase) {
            DecoView arcView = mView.findViewById(R.id.persenArc);
//            arcView.disableHardwareAccelerationForDecoView();
            String color;
            //Mengeset color
            if (presentase <= 45) {
                color = "#C2185B"; // merah
            } else if (presentase <= 75) {
                color = "#FFC107";// kuning
            } else {
                color = "#4CAF50"; // hijau
            }
            // Create background track
            arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                    .setRange(0, 100, 100)
                    .setInitialVisibility(false)
                    .build());

            //Create data series track
            final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 255, 51, 51))
                    .setRange(0, 100, 0)
                    .setInitialVisibility(false)

                    .build();

            //Create Animation
            int series1Index = arcView.addSeries(seriesItem1);
            arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                    .setDelay(1000)
                    .setDuration(2000)
                    .build());
            arcView.addEvent(new DecoEvent.Builder(presentase) // jumlah presentase
                    .setIndex(series1Index)
                    .setDelay(1000)
                    .setColor(Color.parseColor(color)) //Animate Color
                    .build());
            //Set Text
            final TextView presentaseText = mView.findViewById(R.id.presentaseText);
            final String format = "%.0f%%";
            seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                    if (format.contains("%%")) {
                        float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                        presentaseText.setText(String.format(format, percentFilled * 100f));
                    } else {
                        presentaseText.setText(String.format(format, currentPosition));
                    }
                }

                @Override
                public void onSeriesItemDisplayProgress(float percentComplete) {

                }
            });


        }
    }

}


