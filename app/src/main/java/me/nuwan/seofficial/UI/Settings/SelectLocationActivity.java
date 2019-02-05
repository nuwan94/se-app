package me.nuwan.seofficial.UI.Settings;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Toolbar toolbar;
    private SearchView searchView;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        toolbar = findViewById(R.id.selectLocToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView = findViewById(R.id.selectLocSerach);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                geoLocate(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.selectLocMap);
        mapView.getMapAsync(this);
    }


    private void geoLocate(String location) {
        Geocoder geoCoder = new Geocoder(this);
        List<Address> list;
        try {
            list = geoCoder.getFromLocationName(location, 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14F);
                map.animateCamera(update);
                if (marker != null) {
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions().position(latLng));
            } else {
                Common.showToast(this, "No result.");
            }
        } catch (IOException e) {
            Common.showToast(this, "Service currently unavailable.");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(Double.valueOf(User.currentUser.getLat()), Double.valueOf(User.currentUser.getLang()));

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15F);
        map.animateCamera(update);

        MarkerOptions options = new MarkerOptions().position(latLng);
        marker = map.addMarker(options);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResults();
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        setResults();
    }

    public void setResults() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("lat", String.valueOf(marker.getPosition().latitude));
        returnIntent.putExtra("lang", String.valueOf(marker.getPosition().longitude));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
