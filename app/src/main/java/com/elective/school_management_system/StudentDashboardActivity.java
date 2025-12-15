package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    // Carousel Data
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_dashboard);

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
    }

    private void setupCarousel() {
        List<CarouselItem> items = new ArrayList<>();
        // 1. Navigation (The "Commercial" Feature)
        items.add(new CarouselItem(
                "AR Navigation",
                "Experience our state-of-the-art Augmented Reality navigation to find your way.",
                R.drawable.db_nav_icon,
                v -> checkCameraPermissionAndOpen()
        ));

        // 2. Rooms
        items.add(new CarouselItem(
                "Find Rooms",
                "Locate classrooms, labs, and offices instantly with our digital map.",
                R.drawable.db_rooms_ic,
                v -> startActivity(new Intent(StudentDashboardActivity.this, StudentRoomsListActivity.class))
        ));

        // 3. Instructors
        items.add(new CarouselItem(
                "Instructors",
                "Search for professors and view their schedules and room assignments.",
                R.drawable.db_instruct_ic,
                v -> startActivity(new Intent(StudentDashboardActivity.this, StudentInstructorsListActivity.class))
        ));

        // 4. Profile
        items.add(new CarouselItem(
                "Your Profile",
                "Manage your account settings and view your personal information.",
                R.drawable.db_profile_ic,
                v -> startActivity(new Intent(StudentDashboardActivity.this, StudentProfileActivity.class))
        ));

        CarouselAdapter adapter = new CarouselAdapter(items);
        viewPagerCarousel.setAdapter(adapter);

        // Visual Effect for Carousel (Zoom/Fade)
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

        // Link Dots to ViewPager
        new TabLayoutMediator(dotsIndicator, viewPagerCarousel, (tab, position) -> {
            // Dots are handled by the drawable selector
        }).attach();

        // Auto-Scroll Logic
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewPagerCarousel.getCurrentItem() + 1;
                if (nextItem >= items.size()) {
                    nextItem = 0;
                }
                viewPagerCarousel.setCurrentItem(nextItem);
                sliderHandler.postDelayed(this, 3000); // 3 seconds delay
            }
        };
    }

    private void setupBottomNav() {
        navHome.setOnClickListener(v -> viewPagerCarousel.setCurrentItem(0));
        navNav.setOnClickListener(v -> checkCameraPermissionAndOpen());
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    // --- Permissions & Camera ---
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
        String title;
        String description;
        int iconRes;
        View.OnClickListener listener;

        public CarouselItem(String title, String description, int iconRes, View.OnClickListener listener) {
            this.title = title;
            this.description = description;
            this.iconRes = iconRes;
            this.listener = listener;
        }
    }

    static class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
        private final List<CarouselItem> items;

        public CarouselAdapter(List<CarouselItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel_card, parent, false);
            return new CarouselViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
            CarouselItem item = items.get(position);
            holder.title.setText(item.title);
            holder.desc.setText(item.description);
            holder.icon.setImageResource(item.iconRes);
            holder.actionBtn.setOnClickListener(item.listener);
            holder.itemView.setOnClickListener(item.listener);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class CarouselViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView title, desc;
            Button actionBtn;

            public CarouselViewHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.slide_image);
                title = itemView.findViewById(R.id.slide_title);
                desc = itemView.findViewById(R.id.slide_desc);
                actionBtn = itemView.findViewById(R.id.btn_action);
            }
        }
    }
}