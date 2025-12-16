package com.elective.school_management_system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    // UI Components
    private ViewPager2 viewPagerCarousel;
    private TabLayout dotsIndicator;
    private LinearLayout navHome, navNav, navProfile;

    // New Views for Clickable Card
    private View cardNextClass;
    private TextView txtNextClassRoom;

    // Settings Button
    private View btnSettings;

    // Carousel Data
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    // School Coordinates (TS Building, Hagkol, Valencia City)
    private static final double SCHOOL_LAT = 7.9230;
    private static final double SCHOOL_LNG = 125.0953;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_dashboard);

        initViews();
        setupCarousel();
        setupBottomNav();
        setupCardListener(); // Initialize the card click listener
    }

    private void initViews() {
        viewPagerCarousel = findViewById(R.id.viewPagerCarousel);
        dotsIndicator = findViewById(R.id.worm_dots_indicator);

        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);

        // Initialize Card Views
        cardNextClass = findViewById(R.id.cardNextClass);
        txtNextClassRoom = findViewById(R.id.txtNextClassRoom);

        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupCardListener() {
        if (cardNextClass != null) {
            cardNextClass.setOnClickListener(v -> {
                // Open Google Maps Navigation
                openGoogleMaps();
            });
        }
    }

    private void openGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + SCHOOL_LAT + "," + SCHOOL_LNG);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
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

        new TabLayoutMediator(dotsIndicator, viewPagerCarousel, (tab, position) -> {
        }).attach();

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewPagerCarousel.getCurrentItem() + 1;
                if (nextItem >= items.size()) {
                    nextItem = 0;
                }
                viewPagerCarousel.setCurrentItem(nextItem);
                sliderHandler.postDelayed(this, 3000);
            }
        };
    }

    private void setupBottomNav() {
        navHome.setOnClickListener(v -> viewPagerCarousel.setCurrentItem(0));

        navNav.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentRoomMapActivity.class);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

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