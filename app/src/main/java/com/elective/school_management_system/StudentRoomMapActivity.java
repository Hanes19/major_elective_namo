package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StudentRoomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton btnBack;
    private TextView tabList;
    private Button btnNavigateSchool;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LatLng selectedDestination;
    private String selectedCollegeName;
    private Polyline currentPolyline;

    // TODO: PUT YOUR ACTUAL GOOGLE CLOUD API KEY HERE
    private static final String GOOGLE_API_KEY = "YOUR_GOOGLE_MAPS_API_KEY_HERE";

    // Center of the Campus (Adjust these to your actual school center)
    private static final double SCHOOL_LAT = 14.5995;
    private static final double SCHOOL_LNG = 120.9842;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_room_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initViews();
        setupListeners();
        setupMap();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);
        btnNavigateSchool = findViewById(R.id.btnNavigateSchool);

        // Update button text to reflect new scope
        btnNavigateSchool.setText("Navigate to College");
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentRoomsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // ACTION: Open Google Maps App for Navigation (Best when away from school)
        btnNavigateSchool.setOnClickListener(v -> {
            if (selectedDestination == null) {
                Toast.makeText(this, "Please select a College on the map first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Launches external Google Maps app
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedDestination.latitude + "," + selectedDestination.longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Get User Location
        enableUserLocation();

        // 2. Add College/Building Markers instead of Rooms
        List<Room> colleges = getCampusColleges();
        for (Room college : colleges) {
            LatLng location = new LatLng(college.getLatitude(), college.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(college.getRoomName()) // Using roomName as College Name
                    .snippet(college.getDescription()) // e.g., "Engineering Building"
                    // Optional: Use a different color marker for Colleges
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        // 3. Move Camera to Campus Center
        LatLng center = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 17f)); // Zoom level 17 is good for campus view

        // 4. Handle Marker Clicks (Select Destination)
        mMap.setOnMarkerClickListener(marker -> {
            selectedDestination = marker.getPosition();
            selectedCollegeName = marker.getTitle();
            marker.showInfoWindow();

            Toast.makeText(this, "Selected: " + selectedCollegeName, Toast.LENGTH_SHORT).show();

            // Automatically try to draw path if we have user location
            if (currentLocation != null) {
                LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                drawRoute(origin, selectedDestination);
            }
            return true;
        });
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLocation = location;
                        }
                    });
        }
    }

    // --- MOCK DATA FOR COLLEGES ---
    private List<Room> getCampusColleges() {
        List<Room> colleges = new ArrayList<>();

        // Reusing Room constructor: ID, Name, Description, AR_ID, Lat, Lng
        // Replace coordinates with actual Building locations
        colleges.add(new Room(1, "College of Engineering", "Engineering & Architecture Building", "bldg_eng", 14.5995, 120.9842));
        colleges.add(new Room(2, "College of Nursing", "Health Sciences Building", "bldg_nursing", 14.5998, 120.9845));
        colleges.add(new Room(3, "College of Arts & Sciences", "Main Liberal Arts Hall", "bldg_cas", 14.5992, 120.9839));
        colleges.add(new Room(4, "College of Business", "Business & Accountancy Hall", "bldg_cba", 14.5994, 120.9848));
        colleges.add(new Room(5, "Admin Building", "Registrar & Accounting", "bldg_admin", 14.5999, 120.9840));

        return colleges;
    }

    // --- DIRECTIONS API HELPERS ---
    private void drawRoute(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=walking";
        String key = "key=" + GOOGLE_API_KEY;
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + key;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;

        new FetchURL().execute(url);
    }

    private class FetchURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try {
                data = downloadUrl(strings[0]);
            } catch (Exception e) {
                Log.e("Map", "Download URL failed", e);
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String strUrl) throws Exception {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } finally {
            if (iStream != null) iStream.close();
            if (urlConnection != null) urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<LatLng> routes = new ArrayList<>();
            try {
                jObject = new JSONObject(jsonData[0]);
                JSONArray jRoutes = jObject.getJSONArray("routes");
                if (jRoutes.length() > 0) {
                    JSONObject route = jRoutes.getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String encodedString = overviewPolyline.getString("points");
                    routes = decodePoly(encodedString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            if (points != null && !points.isEmpty()) {
                if (currentPolyline != null) currentPolyline.remove();

                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
                currentPolyline = mMap.addPolyline(lineOptions);
            }
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            }
        }
    }
}