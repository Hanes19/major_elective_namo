package com.elective.school_management_system;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListFragment extends Fragment {

    // Views
    private TextView tabStudent, tabTeacher, tabGuest;
    private EditText etSearch;
    private RecyclerView recyclerViewUsers;
    private ImageButton btnBack;

    // Bottom Nav
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Data
    private DatabaseHelper dbHelper;
    private AdminUserAdapter adapter;
    private List<UserItem> allItems = new ArrayList<>();
    private String currentTab = "Student";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout originally used by the Activity
        return inflater.inflate(R.layout.ad_user_list_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DatabaseHelper with the Fragment's context
        dbHelper = new DatabaseHelper(requireContext());

        initViews(view);
        setupListeners();
        setupRecyclerView();

        // Load default tab
        selectTab("Student");
    }

    private void initViews(View view) {
        tabStudent = view.findViewById(R.id.tabStudent);
        tabTeacher = view.findViewById(R.id.tabTeacher);
        tabGuest = view.findViewById(R.id.tabGuest);
        etSearch = view.findViewById(R.id.etSearch);
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        btnBack = view.findViewById(R.id.btnBack);

        navMaps = view.findViewById(R.id.navMaps);
        navDashboard = view.findViewById(R.id.navDashboard);
        navUpdates = view.findViewById(R.id.navUpdates);
    }

    private void setupRecyclerView() {
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AdminUserAdapter(new ArrayList<>());
        recyclerViewUsers.setAdapter(adapter);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                // Navigate back using the Activity's back stack
                requireActivity().onBackPressed();
            });
        }

        tabStudent.setOnClickListener(v -> selectTab("Student"));
        tabTeacher.setOnClickListener(v -> selectTab("Teacher"));
        tabGuest.setOnClickListener(v -> selectTab("Guest"));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filterList(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bottom Navigation Logic
        // Using Intents ensures we correctly switch tabs in AdminMainActivity even if this fragment
        // is nested or in a different container.

        // 1. Maps (Tab Index 0)
        if (navMaps != null) {
            navMaps.setOnClickListener(v -> navigateToMainTab(0));
        }

        // 2. Dashboard (Tab Index 1)
        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> navigateToMainTab(1));
        }

        // 3. Updates/Reports (Tab Index 2)
        if (navUpdates != null) {
            navUpdates.setOnClickListener(v -> navigateToMainTab(2));
        }
    }

    private void navigateToMainTab(int tabIndex) {
        Intent intent = new Intent(requireContext(), AdminMainActivity.class);
        intent.putExtra("TAB_INDEX", tabIndex);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void selectTab(String tabName) {
        currentTab = tabName;
        resetTabStyles();

        Drawable activeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.glass_tab_active);
        int whiteColor = Color.WHITE;

        switch (tabName) {
            case "Student":
                tabStudent.setBackground(activeDrawable);
                tabStudent.setTextColor(whiteColor);
                loadStudents();
                break;
            case "Teacher":
                tabTeacher.setBackground(activeDrawable);
                tabTeacher.setTextColor(whiteColor);
                loadTeachers();
                break;
            case "Guest":
                tabGuest.setBackground(activeDrawable);
                tabGuest.setTextColor(whiteColor);
                loadGuests();
                break;
        }
    }

    private void resetTabStyles() {
        int grayColor = Color.parseColor("#B0B0B0");
        tabStudent.setBackground(null);
        tabStudent.setTextColor(grayColor);
        tabTeacher.setBackground(null);
        tabTeacher.setTextColor(grayColor);
        tabGuest.setBackground(null);
        tabGuest.setTextColor(grayColor);
    }

    private void loadStudents() {
        allItems.clear();
        List<UserItem> students = dbHelper.getAllStudents();
        if (students != null) allItems.addAll(students);
        adapter.updateList(allItems);
    }

    private void loadTeachers() {
        allItems.clear();
        List<Instructor> instructors = dbHelper.getAllInstructors();
        if (instructors != null) {
            for (Instructor inst : instructors) {
                // Ensure Instructor getters match your model
                allItems.add(new UserItem(inst.getId(), inst.getName(), inst.getDepartment(), "Teacher"));
            }
        }
        adapter.updateList(allItems);
    }

    private void loadGuests() {
        allItems.clear();
        List<UserItem> guests = dbHelper.getAllGuests();
        if (guests != null) allItems.addAll(guests);
        adapter.updateList(allItems);
    }

    private void filterList(String query) {
        List<UserItem> filtered = new ArrayList<>();
        for (UserItem item : allItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                    item.getSubtitle().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }

    // Static Inner Class for UserItem Model
    public static class UserItem {
        private int id;
        private String name;
        private String subtitle;
        private String type;

        public UserItem(int id, String name, String subtitle, String type) {
            this.id = id;
            this.name = name;
            this.subtitle = subtitle;
            this.type = type;
        }
        public String getName() { return name; }
        public String getSubtitle() { return subtitle; }
    }

    // RecyclerView Adapter
    // Inside AdminUserListFragment.java

    public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {
        private List<UserItem> userList;

        public AdminUserAdapter(List<UserItem> userList) {
            this.userList = userList;
        }

        public void updateList(List<UserItem> newList) {
            this.userList = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Since all three layouts (student, teacher, guest) are currently identical,
            // inflating this one is fine for now.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_row_student, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            UserItem item = userList.get(position);

            // FIX: Ensure these match the IDs assigned in the ViewHolder
            holder.tvName.setText(item.getName());
            holder.tvSubtitle.setText(item.getSubtitle());

            // Optional: Set up listeners for the buttons
            holder.btnEdit.setOnClickListener(v -> {
                // Handle edit logic here
            });

            holder.btnDelete.setOnClickListener(v -> {
                // Handle delete/ban logic here
            });
        }

        @Override
        public int getItemCount() { return userList.size(); }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvSubtitle, tvStatus;
            ImageButton btnEdit, btnDelete;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                // FIX: Use the correct IDs from your XML layouts
                tvName = itemView.findViewById(R.id.tvUserName);
                tvSubtitle = itemView.findViewById(R.id.tvUserID);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                btnEdit = itemView.findViewById(R.id.btnEditUser);
                btnDelete = itemView.findViewById(R.id.btnBanUser);
            }
        }
    }
}