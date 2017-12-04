package com.example.iqbalzauqul.attendees;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import static android.app.PendingIntent.getActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String tes = getIntent().getStringExtra("nama");
        collapsingToolbar.setTitle(tes);







    }
}
