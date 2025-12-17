package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import static android.content.Context.MODE_PRIVATE;

public class StudentProfileFragment extends Fragment {

    private ImageButton btnBack, btnEdit;
    private TextView btnLogout;
    private TextView tvName, tvEmail;
    private LinearLayout btnChangePassword, btnUpdates, btnHelpSupport;

    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.s_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        initViews(view);
        loadUserProfile();
        setupListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnEdit = view.findViewById(R.id.btnEdit);

        tvName = view.findViewById(R.id.tvProfileName);
        tvEmail = view.findViewById(R.id.tvProfileEmail);

        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnUpdates = view.findViewById(R.id.btnUpdates);
        btnHelpSupport = view.findViewById(R.id.btnHelpSupport);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Hide Back button since we are in a Tab now (optional)
        if(btnBack != null) btnBack.setVisibility(View.GONE);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) return;

        if (tvEmail != null) tvEmail.setText(currentEmail);
        String name = dbHelper.getUsername(currentEmail);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        // Edit Profile
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), StudentEditProfileActivity.class);
                startActivity(intent);
            });
        }

        if (btnLogout != null) btnLogout.setOnClickListener(v -> logout());

        // Settings Menu
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> startActivity(new Intent(requireContext(), StudentResetPasswordActivity.class)));
        }
        if (btnUpdates != null) {
            btnUpdates.setOnClickListener(v -> startActivity(new Intent(requireContext(), GuestUpdatesActivity.class)));
        }
        if (btnHelpSupport != null) {
            btnHelpSupport.setOnClickListener(v -> startActivity(new Intent(requireContext(), StudentHelpSupportActivity.class)));
        }
    }

    private void logout() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireContext(), Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}