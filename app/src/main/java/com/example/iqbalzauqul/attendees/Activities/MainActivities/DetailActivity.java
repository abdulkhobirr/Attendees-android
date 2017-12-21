package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.iqbalzauqul.attendees.Models.PesertaList;
import com.example.iqbalzauqul.attendees.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class DetailActivity extends AppCompatActivity {

    private boolean modeAbsen = false;
    private ArrayList<Integer> selectedToggle = new ArrayList<Integer>();
    private ArrayList<String> pesertaArray = new ArrayList<String>();


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
    CoordinatorLayout.LayoutParams paramsDef;
    DatabaseReference refPertemuan;
    int pertemuanKeInt;
    String pertemuanKe;
    String jmlPertemuan;
    int height;
    private  ArrayList<Boolean> animate = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        coordinatorLayout = findViewById(R.id.coor_detail);
        kelasbg = getIntent().getStringExtra( "kelasbg" );
        key = getIntent().getStringExtra("key");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pesertaKelas").child(key);
         refPertemuan = FirebaseDatabase.getInstance().getReference().child("kelas").
                child(key).child("pertemuanKe");
         refPertemuan.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                     pertemuanKe = dataSnapshot.getValue().toString();
                     pertemuanKeInt = Integer.parseInt( pertemuanKe );
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }

         });
        refPertemuan.getParent().child("jmlPertemuan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jmlPertemuan = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                absenMode();
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
        final String nama = getIntent().getStringExtra("nama");
         String title = nama + "\n(" +pertemuanKeInt + "/" + jmlPertemuan +" Pertemuan)";
        collapsingToolbar.setTitle(title);


        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarExpanded = true;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //Log.v("offset", String.valueOf(verticalOffset));
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (verticalOffset < (-200)) {
//                    Log.v("offset", String.valueOf(verticalOffset));
                    appBarExpanded = false;
                    collapsingToolbar.setTitle(nama);

                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    String titleUpdate = nama + "\n(" +pertemuanKeInt + "/" + jmlPertemuan +" Pertemuan)";
                    collapsingToolbar.setTitle(titleUpdate);
                    invalidateOptionsMenu();
                }
            }
        });
    }

    private void absenMode() {
        RelativeLayout presentase = findViewById(R.id.presentaseContainter);
        LinearLayout toggle = findViewById(R.id.toggleContainer);
        toggle.destroyDrawingCache();
        toggle.setVisibility(View.VISIBLE);
        (DetailActivity.this).startSupportActionMode(actionModeCallbacks);



//                if (presentase.getVisibility() == View.VISIBLE) {
//
//                    presentase.setVisibility(View.GONE);
//                    toggle.setVisibility(View.VISIBLE);
//                }
//                 else {
//                    presentase.setVisibility(View.VISIBLE);
//                    toggle.setVisibility(View.GONE);
//                }
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
        //getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        collapsedMenu = menu;
        menu.add("Update");

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
            //expandedm
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
            protected void populateViewHolder(final PesertaViewHolder viewHolder, final PesertaList model, int position) {
                final ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this);
                progressDialog.setMessage("Menghapus Peserta");
                final String kode = getRef(position).getKey();
                viewHolder.setNomorIdentitas(model.getNomorIdentitas());
                viewHolder.setNama(model.getNama());
                viewHolder.setAvatar(getApplicationContext(), model.getAvatar());

                int i = recyclerView.getAdapter().getItemCount();
                for (;selectedToggle.size() < i;) {
                    selectedToggle.add(0);

                    Log.v("toggle", String.valueOf(selectedToggle));

                }
                for(;animate.size()<i;) {
                    animate.add(false);
                }
                if (pesertaArray.size() < i) {
                    pesertaArray.add(kode);
                }
                viewHolder.setPresentase(model.getProgress(),position,animate);


                if(modeAbsen) {
                    viewHolder.setAbsenMode();





                } else {
                    viewHolder.removeAbsenMode();
                }
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v){
                        final String ava = model.getAvatar();
                        CharSequence menu[] = new CharSequence[] {"Update", "Delete", "Cancel"};

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);


                        builder1.setCancelable(true);
                        builder1.setTitle(model.getNama());
                        builder1.setIcon( R.drawable.defaultava );
                        final AlertDialog b = builder1.create();

                        builder1.setItems(menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent intent = new Intent( DetailActivity.this, updatePeserta.class );
                                        intent.putExtra("key", key);
                                        intent.putExtra("kode", kode);
                                        intent.putExtra( "namaPlaceholder",model.getNama());
                                        intent.putExtra( "IdPlaceholder", model.getNomorIdentitas());
                                        intent.putExtra("avatar",model.getAvatar());
                                        b.cancel();
                                        startActivity(intent);
                                        break;

                                    case 1:
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
                                                Toast.makeText( getApplicationContext(), model.getNama()+"  Telah Dihapus", Toast.LENGTH_SHORT ).show();
                                                progressDialog.dismiss();
                                                //TO DO CODE
                                            }
                                        } ).addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText( getApplicationContext(), "GAGAL", Toast.LENGTH_SHORT ).show();
                                                //TO DO CODE
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

            }

            @Override
            public void onBindViewHolder(final PesertaViewHolder viewHolder, int position) {
                final ToggleButton check = viewHolder.mView.findViewById(R.id.check_toggle);
                final ToggleButton x = viewHolder.mView.findViewById(R.id.x_toggle);
                final ToggleButton seru = viewHolder.mView.findViewById(R.id.seru_toggle);

                if (modeAbsen) {

                    check.setOnCheckedChangeListener(null);
                    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                x.setChecked(false);
                                seru.setChecked(false);
                                selectedToggle.set(viewHolder.getAdapterPosition(), 1);
                                Log.v("Loga", "Loga");

                            }
                        }
                    });
                    x.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                check.setChecked(false);
                                seru.setChecked(false);
                                selectedToggle.set(viewHolder.getAdapterPosition(), 3);
                            }

                        }
                    });
                    seru.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                x.setChecked(false);
                                check.setChecked(false);
                                selectedToggle.set(viewHolder.getAdapterPosition(), 2);
                            }
                        }
                    });
                } else {
                    check.setChecked(false);
                    x.setChecked(false);
                    seru.setChecked(false);
                }


                super.onBindViewHolder(viewHolder, position);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);



            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);
                    if (!modeAbsen) {
                        if (dy > 0 && fabAbsen.getVisibility() == View.VISIBLE) {
                            fabAbsen.hide();
                        } else if (dy < 0 && fabAbsen.getVisibility() != View.VISIBLE) {
                            fabAbsen.show();
                        }
                    }
                }
            });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Update") {

            final String nama = getIntent().getStringExtra("nama");
            final String desc = getIntent().getStringExtra("descPlaceholder");
            Intent intent = new Intent(DetailActivity.this, updateKelas.class);
            intent.putExtra( "jmlPertemuan",jmlPertemuan );
            intent.putExtra( "namaPlaceholder",nama );
            intent.putExtra( "descPlaceholder", desc);
            intent.putExtra( "imageKelas",kelasbg );
            intent.putExtra("key", key);
            startActivity(intent);
        }
        if (item.getTitle() == "Add") {
            absenMode();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"Berhasil ditambahkan.",Toast.LENGTH_SHORT).show();
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

        private void setPresentase(int presentase, int position,ArrayList<Boolean> animated) {
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

            if (animated.get(position) == false) {
                animated.set(position, true);

                //Create Animation
                int series1Index = arcView.addSeries(seriesItem1);
                arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)

                        .setDuration(200)
                        .build());
                arcView.addEvent(new DecoEvent.Builder(presentase) // jumlah presentase
                        .setIndex(series1Index)
                        .setDelay(100)
                        .setDuration(2500)
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

        private void setAbsenMode() {
            RelativeLayout presentaseRelative = mView.findViewById(R.id.presentaseContainter);
            LinearLayout toggleContainer = mView.findViewById(R.id.toggleContainer);

            presentaseRelative.setVisibility(View.GONE);
            toggleContainer.setVisibility(View.VISIBLE);

        }
        private void removeAbsenMode() {
            RelativeLayout presentaseRelative = mView.findViewById(R.id.presentaseContainter);
            LinearLayout toggleContainer = mView.findViewById(R.id.toggleContainer);

            presentaseRelative.setVisibility(View.VISIBLE);
            toggleContainer.setVisibility(View.GONE);

        }








    }


    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            modeAbsen = true;
            menu.add("Absensi Dimulai").setIcon(R.drawable.ic_done_all_black_24dp);
            fabAbsen.setVisibility(View.GONE);
            AppBarLayout layout = findViewById(R.id.appbar);


            paramsDef = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
            height = paramsDef.height;


            paramsDef.height = 3*80; // COLLAPSED_HEIGHT
            layout.setLayoutParams(paramsDef);
            layout.setExpanded(false,true);
            recyclerView.getAdapter().notifyDataSetChanged();

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;

        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getTitle() == "Absensi Dimulai") {
                Log.v("0", String.valueOf(selectedToggle));
                if (!selectedToggle.contains(0)) {


                    for (int i = 0;i<selectedToggle.size();i++) {
                        final int n = i;

                        //set toggle absen
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("pertemuan").
                                child(key).child(pesertaArray.get(i));
                        ref.child(String.valueOf(pertemuanKeInt + 1)).setValue(selectedToggle.get(i));

                        //set presentase ke database
                        ref.orderByValue().equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                              int total = Integer.parseInt(jmlPertemuan);
                              float persen = (dataSnapshot.getChildrenCount()*100)/total;

                              //set presentase ke databsase
                              FirebaseDatabase.getInstance().getReference("pesertaKelas")
                                      .child(key).child(pesertaArray.get(n)).child("progress").setValue(persen);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    refPertemuan.setValue(pertemuanKeInt + 1);



                    Toast.makeText(DetailActivity.this,"Pengabsenan berhasil.",
                            Toast.LENGTH_LONG).show();
                    mode.finish();
                } else

                Toast.makeText(DetailActivity.this,"Ada peserta yang belum di absen, silahkan cek kembali.",
                        Toast.LENGTH_LONG).show();



            }
            return true;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            selectedToggle.clear();
            AppBarLayout layout = findViewById(R.id.appbar);
            layout.setExpanded(true,true);
            paramsDef.height = 512;
            layout.setLayoutParams(paramsDef);
            modeAbsen = false;
            recyclerView.forceLayout();
            recyclerView.getAdapter().notifyDataSetChanged();


        }
    };

}


