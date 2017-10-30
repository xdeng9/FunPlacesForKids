package com.example.xialong.funplacesforkids;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xialong.funplacesforkids.data.Place;
import com.example.xialong.funplacesforkids.util.DetailUtil;

public class DetailActivity extends AppCompatActivity {

    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Intent intent = getIntent();
        mPlace = bundle.getParcelable("key");
        loadBackdrop();

        DetailUtil detailUtil = new DetailUtil(this);
        Log.d("place id=",mPlace.getPlaceId());
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(mPlace.getPlaceImageUrl()).centerCrop().into(imageView);
    }

}
