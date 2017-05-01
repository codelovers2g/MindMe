package com.mindmesolo.mindme.CreateMobilePages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindmesolo.mindme.CreateMobilePages.models.MapModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class AddMap extends RuntimePermissionsActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSIONS = 20;
    static String MapType = "Map";
    LinearLayout fragment_layout;
    TextView textViewSetText, tv_address_bottom, tv_get_current_location;

    EditText ed_enter_text;

    ToggleButton t_btn_map, t_btn_address, t_btn_both;
    Location myLocation;
    LocationListener locationChangeListrner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Log.i("LocationChanges", location.toString());
            //myLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_elements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupUi();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        if (latLng != null) {

                            myLocation = new Location("test");

                            myLocation.setLatitude(latLng.latitude);

                            myLocation.setLongitude(latLng.longitude);

                            Log.i("Latlong", String.valueOf(latLng.latitude) + "\n");

                            Log.i("Latlong", String.valueOf(latLng.longitude) + "\n");

                            String address = DataHelper.getInstance().getAddress(AddMap.this, myLocation);

                            if (address != null) {
                                ed_enter_text.setText(address);
                                tv_address_bottom.setText(address);
                                mMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).title(address));
                            } else {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
                            }

                        }
                    }
                });

                MapModel mapModel = getIntent().getParcelableExtra("map_data");

                if (mapModel != null) {

                    myLocation = new Location("test");

                    ed_enter_text.setText(mapModel.getAddress());

                    myLocation.setLatitude(mapModel.getLatitude());

                    myLocation.setLongitude(mapModel.getLongitude());

                    mMap.clear();

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));

                    mMap.addMarker(new MarkerOptions().position(
                            new LatLng(myLocation.getLatitude(),
                                    myLocation.getLongitude())
                    ));
                } else {
                    getCurrentLocation();
                }
            }
        });
    }

    private void setupUi() {

        ed_enter_text = (EditText) findViewById(R.id.ed_enter_text);

        tv_address_bottom = (TextView) findViewById(R.id.tv_address_bottom);

        fragment_layout = (LinearLayout) findViewById(R.id.fragment_layout);

        tv_get_current_location = (TextView) findViewById(R.id.tv_get_current_location);

        t_btn_map = (ToggleButton) findViewById(R.id.t_btn_map);
        t_btn_map.setOnClickListener(this);

        t_btn_address = (ToggleButton) findViewById(R.id.t_btn_address);
        t_btn_address.setOnClickListener(this);

        t_btn_both = (ToggleButton) findViewById(R.id.t_btn_both);
        t_btn_both.setOnClickListener(this);

        tv_get_current_location.setOnClickListener(this);

        textViewSetText = (TextView) findViewById(R.id.tv_set_address);

        textViewSetText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_btn_map:
                t_btn_both.setChecked(false);
                t_btn_address.setChecked(false);
                t_btn_map.setChecked(true);
                tv_address_bottom.setVisibility(View.GONE);
                fragment_layout.setVisibility(View.VISIBLE);
                MapType = "Map";
                break;

            case R.id.t_btn_address:
                t_btn_address.setChecked(true);
                t_btn_both.setChecked(false);
                t_btn_map.setChecked(false);
                fragment_layout.setVisibility(View.GONE);
                tv_address_bottom.setVisibility(View.VISIBLE);
                MapType = "Address";
                break;

            case R.id.t_btn_both:
                t_btn_both.setChecked(true);
                t_btn_address.setChecked(false);
                t_btn_map.setChecked(false);
                fragment_layout.setVisibility(View.VISIBLE);
                tv_address_bottom.setVisibility(View.VISIBLE);
                MapType = "Map Address";
                break;

            case R.id.tv_get_current_location:
                getCurrentLocation();
                break;

            case R.id.tv_set_address:
                onMapSearch();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            getIntentData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // if user search on map
    private void onMapSearch() {

        String location = ed_enter_text.getText().toString();

        if (StringUtils.isNotBlank(location)) {

            if (location != null || !location.equals("")) {

                Geocoder geocoder = new Geocoder(this);

                try {

                    List<Address> addressList = geocoder.getFromLocationName(location, 1);

                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.clear();

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ToastShow.setText(AddMap.this, "Please enter correct address", Toast.LENGTH_LONG);
        }
    }

    //get the map data for the mobile page
    private MapModel getMapModel(String bitmap) {
        MapModel mapModel = new MapModel();
        mapModel.setName("MAP");
        switch (MapType) {
            case "Map":
                mapModel.setName("MAP");
                mapModel.setLatitude(myLocation.getLatitude());
                mapModel.setLongitude(myLocation.getLongitude());
                mapModel.setType(MapType);
                mapModel.setMapImage(bitmap);
                break;
            case "Address":
                mapModel.setAddress(DataHelper.getInstance().getAddress(AddMap.this, myLocation));
                mapModel.setType(MapType);
                break;
            case "Map Address":
                mapModel.setName("MAP");
                mapModel.setAddress(DataHelper.getInstance().getAddress(AddMap.this, myLocation));
                mapModel.setLatitude(myLocation.getLatitude());
                mapModel.setLongitude(myLocation.getLongitude());
                mapModel.setType(MapType);
                mapModel.setMapImage(bitmap);
                break;
        }
        return mapModel;
    }

    private void getIntentData() {
        if (mMap != null && myLocation != null) {
            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap snapshot) {
                    Intent returnIntent = new Intent();
                    int nh = (int) (snapshot.getHeight() * (512.0 / snapshot.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(snapshot, 512, nh, true);
                    String BitmapImageData = DataHelper.getInstance().encodeToBase64(scaled, Bitmap.CompressFormat.JPEG, 90);
                    MapModel mapModel = getMapModel(BitmapImageData);
                    returnIntent.putExtra("result", "Done");
                    returnIntent.putExtra("map_data", mapModel);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            };
            mMap.snapshot(callback);
        }
    }

    private void checkPermissions() {
        AddMap.super.requestAppPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.LOCATION_HARDWARE,
                },
                R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    public void getCurrentLocation() {

        Log.i("Location", "location Call");

        // The minimum distance to change Updates in meters
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 0 meters

        // The minimum time between updates in milliseconds
        final long MIN_TIME_BW_UPDATES = 500; //  milliseconds (1000 * 60 *1 = 1 min);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
            return;
        }

        try {
            // Declaring a Location Manager
            LocationManager locationManager = (LocationManager) AddMap.this.getSystemService(LOCATION_SERVICE);
            // Getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // Getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                ToastShow.setText(AddMap.this, "Please enable location service in your device.", Toast.LENGTH_LONG);

                // No network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListrner);
                    if (locationManager != null) {
                        myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (myLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListrner);
                        if (locationManager != null) {
                            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));
            String address = DataHelper.getInstance().getAddress(AddMap.this, myLocation);
            mMap.clear();
            if (address != null) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).title(address));
                ed_enter_text.setText(address);
                tv_address_bottom.setText(address);
            } else {
                mMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
            }
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } else {
            //ToastShow.setText(AddMap.this,"Please enable location service in your device. ",Toast.LENGTH_LONG);
            //Log.i("Location", "location Not found ");
        }
    }

    //check runtime permissions for location service
    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSIONS) {
            getCurrentLocation();
        }
    }
}
