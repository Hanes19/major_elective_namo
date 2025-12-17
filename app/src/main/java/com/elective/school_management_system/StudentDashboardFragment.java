package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class StudentDashboardFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 100;

    // UI Components
    private ViewPager2 viewPagerCarousel;
    private TabLayout dotsIndicator;
    private View cardNextClass;
    private TextView txtNextClassRoom;
    private View btnSettings;

    // Carousel Data
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    private String pendingRoomName;
    private DatabaseHelper dbHelper;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.s_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        SharedPreferences prefs = requireContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        userId = dbHelper.getUserId(email);

        initViews(view);
        setupCarousel();
        setupCardListener();
        updateNextClassCard();
    }

    private void initViews(View view) {
        viewPagerCarousel = view.findViewById(R.id.viewPagerCarousel);
        dotsIndicator = view.findViewById(R.id.worm_dots_indicator);
        cardNextClass = view.findViewById(R.id.cardNextClass);
        txtNextClassRoom = view.findViewById(R.id.txtNextClassRoom);
        // Note: Bottom Nav views are removed from here as they are in the Main Activity
    }

    private void updateNextClassCard() {
        if (txtNextClassRoom == null) return;
        List<Schedule> myClasses = dbHelper.getStudentSchedule(userId);
        if (!myClasses.isEmpty()) {
            Schedule next = myClasses.get(0);
            txtNextClassRoom.setText(next.getRoomName() + " • " + next.getStartTime());
        } else {
            txtNextClassRoom.setText("No classes enrolled");
        }
    }

    private void setupCardListener() {
        if (cardNextClass != null) {
            cardNextClass.setOnClickListener(v -> {
                String roomName = "Navigation Mode";
                if (txtNextClassRoom != null) {
                    String fullText = txtNextClassRoom.getText().toString();
                    if (fullText.contains("•")) {
                        roomName = fullText.split("•")[0].trim();
                    } else {
                        roomName = fullText.trim();
                    }
                }
                if (roomName.equalsIgnoreCase("No classes enrolled")) {
                    Toast.makeText(requireContext(), "No class location to navigate to.", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkCameraPermissionAndOpen(roomName);
            });
        }
    }

    private void setupCarousel() {
        List<StudentDashboardActivity.CarouselItem> items = new ArrayList<>();
        items.add(new StudentDashboardActivity.CarouselItem(R.drawable.db_nav_icon));
        items.add(new StudentDashboardActivity.CarouselItem(R.drawable.db_rooms_ic));
        items.add(new StudentDashboardActivity.CarouselItem(R.drawable.db_instruct_ic));
        items.add(new StudentDashboardActivity.CarouselItem(R.drawable.db_profile_ic));

        StudentDashboardActivity.CarouselAdapter adapter = new StudentDashboardActivity.CarouselAdapter(items);
        viewPagerCarousel.setAdapter(adapter);

        // Carousel setup logic (same as before)
        viewPagerCarousel.setClipToPadding(false);
        viewPagerCarousel.setClipChildren(false);
        viewPagerCarousel.setOffscreenPageLimit(3);
        viewPagerCarousel.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPagerCarousel.setPageTransformer(transformer);

        new TabLayoutMediator(dotsIndicator, viewPagerCarousel, (tab, position) -> {}).attach();

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewPagerCarousel.getCurrentItem() + 1;
                if (nextItem >= items.size()) nextItem = 0;
                viewPagerCarousel.setCurrentItem(nextItem);
                sliderHandler.postDelayed(this, 3000);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        updateNextClassCard();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    // --- Permissions Logic ---
    private void checkCameraPermissionAndOpen(String roomName) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            pendingRoomName = roomName;
            // Use requestPermissions from Fragment
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera(roomName);
        }
    }

    private void openCamera(String roomName) {
        Intent intent = new Intent(requireContext(), StudentNavigationActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String roomToOpen = (pendingRoomName != null) ? pendingRoomName : "Navigation Mode";
                openCamera(roomToOpen);
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}