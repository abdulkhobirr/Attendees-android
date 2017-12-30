package com.example.iqbalzauqul.attendees.Activities.MainActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.iqbalzauqul.attendees.Manifest;
import com.example.iqbalzauqul.attendees.Models.PesertaList;
import com.example.iqbalzauqul.attendees.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.wnameless.json.flattener.JsonFlattener;
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


import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.app.PendingIntent.getActivity;
import static java.lang.Integer.parseInt;

public class DetailActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE = 0;
    private boolean modeAbsen = false;
    private ArrayList<Integer> selectedToggle = new ArrayList<Integer>();
    private ArrayList<String> pesertaArray = new ArrayList<String>();


    Boolean absenFinished = false;
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
     ArrayList<String> namaList = new ArrayList<String>();
     ArrayList<String> idList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        Log.v("create","created");
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
                     pertemuanKeInt = parseInt( pertemuanKe );
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

        FloatingActionButton fab = findViewById(R.id.avatar);
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


        AppBarLayout appBarLayout = findViewById(R.id.appbar_pes);
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
        menu.add("Export");

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
    protected void onResume() {



            FirebaseRecyclerAdapter<PesertaList, PesertaViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PesertaList, PesertaViewHolder>(
                    PesertaList.class,
                    R.layout.peserta_dalam_kelas_cardview,
                    PesertaViewHolder.class,
                    mDatabaseReference

            ) {
                @Override
                protected void populateViewHolder(final PesertaViewHolder viewHolder, final PesertaList model,  int position) {

                    final ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this);
                    progressDialog.setMessage("Menghapus Peserta");
                    viewHolder.setNomorIdentitas(model.getNomorIdentitas());
                    viewHolder.setNama(model.getNama());
                    viewHolder.setAvatar(getApplicationContext(), model.getAvatar());
                    final String kode = getRef(position).getKey();
                    Log.v("codes",kode);
                    int i = recyclerView.getAdapter().getItemCount();
                    for (;selectedToggle.size() < i;) {
                        selectedToggle.add(0);

                        Log.v("toggle", String.valueOf(i));

                    }
                    for(;animate.size()<i;) {
                        animate.add(false);
                    }
                    // check apakah ada item baru yang ditambahkan
                    if (pesertaArray.size() < i) {
                        //check apakah kode yang dioper merupakan kode baru
                        if (!pesertaArray.contains(kode)) {
                            pesertaArray.add(kode);
                        }
                    }


                    if (modeAbsen) {
                        viewHolder.setAbsenMode();
                        viewHolder.mView.setOnLongClickListener(null); {

                        };







                    } else {
                        viewHolder.removeAbsenMode();
                        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final String ava = model.getAvatar();
                                CharSequence menu[] = new CharSequence[]{"Update", "Delete", "Cancel"};

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);


                                builder1.setCancelable(true);
                                builder1.setTitle(model.getNama());
                                builder1.setIcon(R.drawable.defaultava);
                                final AlertDialog b = builder1.create();

                                builder1.setItems(menu, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case 0:

                                                Intent intent = new Intent(DetailActivity.this, updatePeserta.class);
                                                intent.putExtra("key", key);
                                                intent.putExtra("kode", kode);
                                                intent.putExtra("namaPlaceholder", model.getNama());
                                                intent.putExtra("IdPlaceholder", model.getNomorIdentitas());
                                                intent.putExtra("avatar", model.getAvatar());
                                                b.cancel();
                                                startActivity(intent);
                                                break;

                                            case 1:
                                                progressDialog.show();
                                                DatabaseReference drPeserta = FirebaseDatabase.getInstance().getReference("pesertaKelas").child(key).child(kode);


                                                drPeserta.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //Cek apakah avatar null? (kasus join kelas via kode tanpa avatar)
                                                        if (model.getAvatar() != null) {
                                                            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                                                            StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(ava);
                                                            photoRef.delete();
                                                        }

                                                        b.cancel();
                                                        Toast.makeText(getApplicationContext(), model.getNama() + "  Telah Dihapus", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        //TO DO CODE
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "GAGAL", Toast.LENGTH_SHORT).show();
                                                        //TO DO CODE
                                                    }
                                                });
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
                    if (absenFinished == false) {
                        viewHolder.setPresentase(model.getProgress(), position, animate);
                    }


                }

                @Override
                public boolean onFailedToRecycleView(PesertaViewHolder holder) {
                    Log.v("failed","d");
                    return super.onFailedToRecycleView(holder);
                }



                @Override
                public void onDataChanged() {

                    super.onDataChanged();
                    if (absenFinished){
                        absen();
                    }
                }

                @Override
                public void onBindViewHolder(final PesertaViewHolder viewHolder, int position) {
                    final ToggleButton check = viewHolder.mView.findViewById(R.id.check_toggle);
                    final ToggleButton x = viewHolder.mView.findViewById(R.id.x_toggle);
                    final ToggleButton seru = viewHolder.mView.findViewById(R.id.seru_toggle);

//                    if (absenFinished){
//                        absen();
//                    }





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
            Log.v("resumecalled","resumecalled");



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


        super.onResume();
    }

    private void absen() {
        absenFinished = false;
//       recyclerView.getAdapter().notifyDataSetChanged();

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
        if (item.getTitle() == "Export") {
            listener();

        }
        if (item.getTitle() == "Add") {
            absenMode();
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listener() {

        DatabaseReference absenRef = FirebaseDatabase.getInstance().getReference("pesertaKelas/"+key);
        absenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot children:dataSnapshot.getChildren()){
                    String nama = children.child("nama").getValue().toString();
                    Log.v("nama",nama);
                    String id = children.child("nomorIdentitas").getValue().toString();
                    namaList.add(nama);
                    idList.add(id);
                }

                convert();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"Berhasil ditambahkan.",Toast.LENGTH_SHORT).show();
                recyclerView.getAdapter().notifyDataSetChanged();
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

        private void setPresentase(final int presentase, final int position, ArrayList<Boolean> animated) {
            final DecoView arcView = mView.findViewById(R.id.persenArc);
            Log.v("numv",String.valueOf(presentase));


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
                Log.v("rekonstru",String.valueOf(position));

                //Create Animation
                int series1Index = arcView.addSeries(seriesItem1);
                arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)

                        .setDuration(200)
                        .build());

                arcView.addEvent(new DecoEvent.Builder(presentase) // jumlah presentase
                        .setIndex(series1Index)
//                        .setDelay(100)
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
                        Log.v("percen",String.valueOf(percentComplete));


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

            toggleContainer.setVisibility(View.GONE);
            presentaseRelative.setVisibility(View.VISIBLE);


        }








    }


    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            modeAbsen = true;
            menu.add("Absensi Dimulai").setIcon(R.drawable.ic_done_all_black_24dp);
            fabAbsen.setVisibility(View.GONE);
            AppBarLayout layout = findViewById(R.id.appbar_pes);


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
                Log.v("sise", String.valueOf(selectedToggle.size()));
                if (!selectedToggle.contains(0)) {



                    for (int i = 0;i<=selectedToggle.size()-1;i++) {
                        final int n = i;

                        //set toggle absen
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("pertemuan").
                                child(key).child(pesertaArray.get(i));
                        ref.child(String.valueOf(pertemuanKeInt + 1)).setValue(selectedToggle.get(i));

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ref.orderByValue().equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        int total = pertemuanKeInt + 1;
                                        float persen = (dataSnapshot.getChildrenCount()*100)/pertemuanKeInt;
                                        Log.v("ofN",String.valueOf(n));
                                        Log.v("pesertaArrayArray",String.valueOf(pesertaArray));
                                        //set presentase ke databsase
                                        FirebaseDatabase.getInstance().getReference("pesertaKelas")
                                                .child(key).child(pesertaArray.get(n)).child("progress").setValue(persen);


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //set presentase ke database


                    }
                    absenFinished = true;
                    recyclerView.getAdapter().notifyDataSetChanged();
                    refPertemuan.setValue(pertemuanKeInt + 1);
                    Log.v("first","first");
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                    convert();
                    mode.finish();



//                    Toast.makeText(DetailActivity.this,"Pengabsenan berhasil.",
//                            Toast.LENGTH_LONG).show();
                } else

                Toast.makeText(DetailActivity.this,"Ada peserta yang belum di absen, silahkan cek kembali.",
                        Toast.LENGTH_LONG).show();



            }
            return true;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            fabAbsen.setVisibility(View.VISIBLE);
            selectedToggle.clear();
            AppBarLayout layout = findViewById(R.id.appbar_pes);
            layout.setExpanded(true,true);
            paramsDef.height = height;
            layout.setLayoutParams(paramsDef);
            modeAbsen = false;
            for(int i=0;i<animate.size();i++) {

                animate.set(i, false);
                Log.v("animater", String.valueOf(animate));

            }
            if(absenFinished == false)
           recyclerView.getAdapter().notifyDataSetChanged();
            Log.v("second  ", "");

        }

    };

    @Override
    public void onBackPressed() {
       FirebaseRecyclerAdapter adapter = (FirebaseRecyclerAdapter) recyclerView.getAdapter();

       ((FirebaseRecyclerAdapter) recyclerView.getAdapter()).cleanup();
//        recyclerView.getAdapter().notifyDataSetChanged();
        Log.v("back","backpressed");
//        recyclerView.setAdapter(adapter);
        super.onBackPressed();

    }



    @Override
    protected void onPause() {
        super.onPause();
        for(int i = 0;i<animate.size();i++) {
            animate.set(i,false);
        }
        ((FirebaseRecyclerAdapter) recyclerView.getAdapter()).cleanup();

    }

   public void convert() {

       // Here, thisActivity is the current activity
       if (ContextCompat.checkSelfPermission(this,
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {

           // Should we show an explanation?
           if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                   android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

               // Show an explanation to the user *asynchronously* -- don't block
               // this thread waiting for the user's response! After the user
               // sees the explanation, try again to request the permission.

           } else {

               // No explanation needed, we can request the permission.

               ActivityCompat.requestPermissions(this,
                       new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                       PERMISSION_REQUEST_WRITE);

               // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
               // app-defined int constant. The callback method gets the
               // result of the request.
           }
           return;
       }


       final WritableWorkbook m_workbook;
       String pathToExternalStorage = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS)).toString();
       Log.v("HGH",pathToExternalStorage);

           try
           {

                m_workbook = Workbook.createWorkbook(new File(pathToExternalStorage,"excel2.xls"));
               // this will create new new sheet in workbook
               final WritableSheet sheet = m_workbook.createSheet("Absensi", 0);
               sheet.setColumnView(0,25);
               sheet.setColumnView(1,18);
               sheet.addCell(new Label(0, 0, "ID"));
               sheet.addCell(new Label(1, 0, "Nama"));
               // Nomor absen ke-
                for(int i=0;i<parseInt(jmlPertemuan);i++) {


                    sheet.setColumnView(i+2, 4);
                    sheet.addCell(new Label(2+i,0,String.valueOf(i+1)));
                }


                for(int i=0;i<namaList.size();i++){
                    Log.v("idList",idList.get(i));

                    sheet.addCell(new Label(0,i+1,idList.get(i)));
                    sheet.addCell(new Label(1,i+1,namaList.get(i)));
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pertemuan/"+ key);
               ref.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       int n = 1;
                       for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                            for(DataSnapshot children:postSnapshot.getChildren()){
                                int i=1;
                                int m = children.getValue(int.class);
                                int keys = parseInt(children.getKey());
                                Log.v("mnight",String.valueOf(keys));
                                i = i+ keys;
                                Log.v("keys",String.valueOf(i));

                                Log.v("childrenshotz",String.valueOf(m));
                                if (m==1) {
                                    try {
                                        WritableCellFormat format = new WritableCellFormat();
                                        format.setBackground(Colour.GREEN);

                                        sheet.addCell(new Label(i, n, "✓",format));
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
                                } else if(m==2) {
                                    try {
                                        WritableCellFormat format = new WritableCellFormat();
                                        format.setBackground(Colour.YELLOW);


                                        sheet.addCell(new Label(i, n, "!",format));
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
                                } else if(m==3) {
                                    try {
                                        WritableCellFormat format = new WritableCellFormat();
                                        format.setBackground(Colour.RED);

                                        sheet.addCell(new Label(i, n, "x",format));
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            n++;
                       }
                       try {
                           m_workbook.write();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       try {
                           m_workbook.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       } catch (WriteException e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }

               });


               Toast.makeText(DetailActivity.this,"Done",Toast.LENGTH_LONG).show();

           }
           catch (Exception e) {
               Toast.makeText(DetailActivity.this,String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();


           }
           {

           }


//       HSSFWorkbook workbook = new HSSFWorkbook();
//       HSSFSheet sheet = workbook.createSheet("1");
//       HSSFRow row = sheet.createRow(0);
//       row.createCell(0).setCellValue("ID");
//       row.createCell(1).setCellValue("Nama");
//
//       workbook.write(new FileOutputStream("excel.xls"));
//       workbook.close();
//       Toast.makeText(DetailActivity.this,"Done",Toast.LENGTH_LONG).show();





//        Map <String,Object> map = JsonFlattener.flattenAsMap(jsonString);
//       List<Map<String, String>> flatJson = (List<Map<String, String>>) map;
//
//       CSVWriter.writeToFile(CSVWriter.getCSV(flatJson), "files/sample_string.csv");



   }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Granted.
                    convert();

                }
                else{
                    //Denied.
                }
                break;
        }
    }

}


