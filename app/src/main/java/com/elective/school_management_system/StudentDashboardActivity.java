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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView; // Imported TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    // UI Components
    private ViewPager2 viewPagerCarousel;
    private TabLayout dotsIndicator;
    private LinearLayout navHome, navNav, navProfile;

    // --- NEW: Text Views for Next Class ---
    private TextView txtNextClassSubject, txtNextClassRoom;

    // Settings Button
    private View btnSettings;

    // Database & Carousel Data
    private DatabaseHelper dbHelper; // Added DatabaseHelper
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_dashboard);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        initViews();
        setupCarousel();
        setupBottomNav();
    }

    private void initViews() {
        viewPagerCarousel = findViewById(R.id.viewPagerCarousel);
        dotsIndicator = findViewById(R.id.worm_dots_indicator);

        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);

        // --- NEW: Bind the TextViews for the upcoming class card ---
        txtNextClassSubject = findViewById(R.id.txtNextClassSubject);
        txtNextClassRoom = findViewById(R.id.txtNextClassRoom);

        btnSettings = findViewById(R.id.btnSettings);
    }

    // --- NEW: Method to load the next class from the database ---
    private void loadUpcomingClass() {
        // 1. Get current user's email from session
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");

        if (email.isEmpty()) return;

        // 2. Get User ID
        int userId = dbHelper.getUserId(email);

        // 3. Fetch Next Class using existing logic in DatabaseHelper
        Schedule nextClass = dbHelper.getNextClass(userId);

        // 4. Update UI
        if (nextClass != null) {
            txtNextClassSubject.setText(nextClass.getSubject());
            // Format: "Room 101 • 08:00"
            String details = nextClass.getRoomName() + " • " + nextClass.getStartTime();
            txtNextClassRoom.setText(details);
        } else {
            // Handle case where there are no more classes today
            txtNextClassSubject.setText("No Upcoming Classes");
            txtNextClassRoom.setText("You are free for now!");
        }
    }

    private void setupCarousel() {
        List<CarouselItem> items = new ArrayList<>();
        items.add(new CarouselItem(R.drawable.db_nav_icon));
        items.add(new CarouselItem(R.drawable.db_rooms_ic));
        items.add(new CarouselItem(R.drawable.db_instruct_ic));
        items.add(new CarouselItem(R.drawable.db_profile_ic));

        CarouselAdapter adapter = new CarouselAdapter(items);
        viewPagerCarousel.setAdapter(adapter);

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

        sliderRunnable = () -> {
            int nextItem = viewPagerCarousel.getCurrentItem() + 1;
            if (nextItem >= items.size()) {
                nextItem = 0;
            }
            viewPagerCarousel.setCurrentItem(nextItem);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
    }

    private void setupBottomNav() {
        navHome.setOnClickListener(v -> viewPagerCarousel.setCurrentItem(0));

        navNav.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentNavigationListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(StudentDashboardActivity.this, NavSettingsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        // --- NEW: Refresh class data whenever the dashboard appears ---
        loadUpcomingClass();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    // --- Permissions & Camera Logic (unchanged) ---
    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentNavigationActivity.class);
        intent.putExtra("ROOM_NAME", "Navigation Mode");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use Navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ================== INNER CLASSES FOR CAROUSEL ==================
    static class CarouselItem {
        int iconRes;
        public CarouselItem(int iconRes) { this.iconRes = iconRes; }
    }

    static class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
        private final List<CarouselItem> items;
        public CarouselAdapter(List<CarouselItem> items) { this.items = items; }

        @NonNull
        @Override
        public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel_card, parent, false);
            return new CarouselViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
            CarouselItem item = items.get(position);
            holder.icon.setImageResource(item.iconRes);
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class CarouselViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            public CarouselViewHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.slide_image);
            }
        }
    }
}