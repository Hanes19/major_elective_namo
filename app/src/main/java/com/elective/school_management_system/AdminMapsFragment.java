package com.elective.school_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdminMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_room_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Map
        // Note: In fragments, we use getChildFragmentManager()
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Filter Button
        btnFilter = view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Filter clicked", Toast.LENGTH_SHORT).show();
            // Add your filter dialog logic here
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Example Campus Coordinates (Update these to your real values)
        LatLng campusLocation = new LatLng(8.4859, 124.6567); // CDO Example
        mMap.addMarker(new MarkerOptions().position(campusLocation).title("Campus Main"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLocation, 18f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}