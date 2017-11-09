package com.example.xialong.funplacesforkids;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xialong.funplacesforkids.data.Place;
import com.example.xialong.funplacesforkids.util.DetailUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private Place mPlace;
    private ShareActionProvider mShareActionProvider;
    private boolean mIsFav;
    @BindView(R.id.place_name) TextView placeName;
    @BindView(R.id.place_address) TextView placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mPlace.getPlaceName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

//        placeName = (TextView) findViewById(R.id.place_name);
//        placeAddress = (TextView) findViewById(R.id.place_address);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mPlace = bundle.getParcelable("key");
        loadBackdrop();
        placeName.setText(mPlace.getPlaceName());
        placeAddress.setText(mPlace.getPlaceAddress());

        DetailUtil detailUtil = new DetailUtil(this);
        Log.d("place id=", mPlace.getPlaceId());
        mIsFav = mPlace.getFav() == 0? false : true;
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(mPlace.getPlaceImageUrl()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.share_item);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(getShareIntent());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem fav = menu.findItem(R.id.fav_item);
        MenuItem unfav = menu.findItem(R.id.unfav_item);

        fav.setVisible(mIsFav);
        unfav.setVisible(!mIsFav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.fav_item:
                mIsFav = false;
                invalidateOptionsMenu();
                //update database here
                displayToast();
                return true;
            case R.id.unfav_item:
                mIsFav = true;
                invalidateOptionsMenu();
                //update database here
                displayToast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mPlace.getPlaceName());
        intent.putExtra(Intent.EXTRA_TEXT, mPlace.getPlaceAddress());
        return intent;
    }

    private void displayToast(){
        if(mIsFav){
            Toast.makeText(this, "Bookmarked!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Removed Bookmark.", Toast.LENGTH_SHORT).show();
        }
    }
}
