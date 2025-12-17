package com.elective.school_management_system;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminViewPagerAdapter extends FragmentStateAdapter {

    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminMapsFragment();
            case 1:
                return new AdminDashboardFragment();
            case 2:
                return new AdminReportsFragment();
            default:
                return new AdminDashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Maps, Dashboard, Updates
    }
}