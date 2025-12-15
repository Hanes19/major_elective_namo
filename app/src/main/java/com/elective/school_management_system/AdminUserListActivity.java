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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {

    // Views
    private TextView tabStudent, tabTeacher, tabGuest;
    private EditText etSearch;
    private RecyclerView recyclerViewUsers;
    private ImageButton btnBack;
    private LinearLayout navHome, navNavigation, navProfile; // Bottom Nav

    // Data
    private DatabaseHelper dbHelper;
    private AdminUserAdapter adapter;
    private List<UserItem> allItems = new ArrayList<>(); // Stores all loaded items for search filtering
    private String currentTab = "Student"; // Default tab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We use the student layout as the base, but we will manipulate the UI for other tabs
        setContentView(R.layout.ad_user_list_student);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
        setupRecyclerView();

        // Load default tab
        selectTab("Student");
    }

    private void initViews() {
        tabStudent = findViewById(R.id.tabStudent);
        tabTeacher = findViewById(R.id.tabTeacher);
        tabGuest = findViewById(R.id.tabGuest);
        etSearch = findViewById(R.id.etSearch);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        btnBack = findViewById(R.id.btnBack);

        // Bottom Navigation
        LinearLayout bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            navHome = (LinearLayout) bottomNav.getChildAt(0); // Assuming order: Home, Nav, Profile
            navNavigation = (LinearLayout) bottomNav.getChildAt(1);
            navProfile = (LinearLayout) bottomNav.getChildAt(2);
        }
    }

    private void setupRecyclerView() {
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminUserAdapter(new ArrayList<>());
        recyclerViewUsers.setAdapter(adapter);
    }

    private void setupListeners() {
        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        // Tabs
        tabStudent.setOnClickListener(v -> selectTab("Student"));
        tabTeacher.setOnClickListener(v -> selectTab("Teacher"));
        tabGuest.setOnClickListener(v -> selectTab("Guest"));

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Bottom Navigation
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
            });
        }

        if (navNavigation != null) {
            navNavigation.setOnClickListener(v -> {
                startActivity(new Intent(this, StudentRoomMapActivity.class));
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Optional: Navigate to a generic profile or admin profile
                Toast.makeText(this, "Admin Profile", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void selectTab(String tabName) {
        currentTab = tabName;
        resetTabStyles();

        // Highlight selected tab
        Drawable activeDrawable = ContextCompat.getDrawable(this, R.drawable.glass_tab_active);
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
        Drawable transparent = null;

        tabStudent.setBackground(transparent);
        tabStudent.setTextColor(grayColor);

        tabTeacher.setBackground(transparent);
        tabTeacher.setTextColor(grayColor);

        tabGuest.setBackground(transparent);
        tabGuest.setTextColor(grayColor);
    }

    // --- Data Loading Methods ---

    private void loadStudents() {
        allItems.clear();
        // Fetch students (Users who have profiles)
        List<UserItem> students = dbHelper.getAllStudents();
        if (students != null) {
            allItems.addAll(students);
        }
        adapter.updateList(allItems);
    }

    private void loadTeachers() {
        allItems.clear();
        // Fetch instructors
        List<Instructor> instructors = dbHelper.getAllInstructors();
        if (instructors != null) {
            for (Instructor inst : instructors) {
                allItems.add(new UserItem(inst.getId(), inst.getName(), inst.getDepartment(), "Teacher"));
            }
        }
        adapter.updateList(allItems);
    }

    private void loadGuests() {
        allItems.clear();
        // Fetch guests (Users without profiles)
        List<UserItem> guests = dbHelper.getAllGuests();
        if (guests != null) {
            allItems.addAll(guests);
        }
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

    // --- Inner Classes for Adapter and Model ---

    public static class UserItem {
        private int id;
        private String name;
        private String subtitle; // Email or Department or Details
        private String type; // Student, Teacher, Guest

        public UserItem(int id, String name, String subtitle, String type) {
            this.id = id;
            this.name = name;
            this.subtitle = subtitle;
            this.type = type;
        }

        public String getName() { return name; }
        public String getSubtitle() { return subtitle; }
    }

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
            // Using student row as generic
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_row_student, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            UserItem item = userList.get(position);

            if (holder.tvName != null) {
                holder.tvName.setText(item.getName());
            }
            if (holder.tvSubtitle != null) {
                holder.tvSubtitle.setText(item.getSubtitle());
            }
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvSubtitle;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                // Corrected: Removed reference to non-existent IDs (tvStudentName, tvStudentId)
                // Assuming tvName and tvEmail exist in item_user_row_student.xml
                tvName = itemView.findViewById(R.id.tvName);
                tvSubtitle = itemView.findViewById(R.id.tvEmail);
            }
        }
    }
}