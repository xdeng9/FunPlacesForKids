package com.example.xialong.funplacesforkids;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xialong.funplacesforkids.fragment.TabFragment;
import com.example.xialong.funplacesforkids.util.PlaceUtil;
import com.example.xialong.funplacesforkids.util.Util;
import com.example.xialong.funplacesforkids.util.WeatherUtil;
import com.example.xialong.funplacesforkids.view.PagerSlidingTabStrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        WeatherUtil.Callback {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private TextView currentLocation, currentTemperature;
    private ImageView weatherIcon, backdrop;
    private MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private WeatherUtil weather;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", "Unknown");
        editor.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentLocation = (TextView) findViewById(R.id.current_location);
        currentTemperature = (TextView) findViewById(R.id.temperature);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        backdrop = (ImageView) findViewById(R.id.backdrop_image);
        Glide.with(this).load(Util.getRandomBackdrop()).centerCrop().into(backdrop);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_tabs);
        pager = (ViewPager) findViewById(R.id.activity_pager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        checkPermission();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        }
    }

    private void updateLocation(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Util.setCurrentLocation(lat, lon);
            currentLocation.setText(Util.getCity(this, lat, lon));
            weather = new WeatherUtil(this);
            weather.execute(location.getLatitude(), location.getLongitude());
            adapter.notifyDataSetChanged();
            PlaceUtil.fetchPlaces(this);
        }
    }

    @Override
    public void updateWeather(String temperature) {
        if (temperature != null && temperature.length() > 0) {
            String[] temps = temperature.split(" ");
            currentTemperature.setText(Util.formatTemp(temps[1]));
            Glide.with(this).load("http:" + temps[2]).centerCrop().into(weatherIcon);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(3000000);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   //updateLocation(Util.getLocation(MainActivity.this));
                } else {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
                return;
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location changed");
        String currentCity = preferences.getString("city", "Unknown");
        Log.d("city changed? ", Util.cityChanged(MainActivity.this, location, currentCity)+"");
        if(Util.emptyData(MainActivity.this) || Util.cityChanged(MainActivity.this, location, currentCity)){
            updateLocation(location);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("city", Util.getCity(MainActivity.this, location.getLatitude(), location.getLongitude()));
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Util.getTabNames().get(position);
        }

        @Override
        public int getCount() {
            return Util.getTabNames().size();
        }

        @Override
        public Fragment getItem(int position) {
            return TabFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
