package com.elective.school_management_system;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminManageUsersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This layout contains a FrameLayout with id 'fragment_container'
        setContentView(R.layout.activity_admin_manage_users);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AdminUserListFragment())
                    .commit();
        }
    }
}