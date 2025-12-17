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

    // Bottom Nav
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Data
    private DatabaseHelper dbHelper;
    private AdminUserAdapter adapter;
    private List<UserItem> allItems = new ArrayList<>();
    private String currentTab = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_user_list_student);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
        setupRecyclerView();
        selectTab("Student");
    }

    private void initViews() {
        tabStudent = findViewById(R.id.tabStudent);
        tabTeacher = findViewById(R.id.tabTeacher);
        tabGuest = findViewById(R.id.tabGuest);
        etSearch = findViewById(R.id.etSearch);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        btnBack = findViewById(R.id.btnBack);

        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupRecyclerView() {
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminUserAdapter(new ArrayList<>());
        recyclerViewUsers.setAdapter(adapter);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

        // FIX: Bottom Navigation now targets AdminMainActivity with specific Tab Index

        // 1. Maps (Tab Index 0)
        if (navMaps != null) {
            navMaps.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminMainActivity.class);
                intent.putExtra("TAB_INDEX", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        // 2. Dashboard (Tab Index 1)
        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminMainActivity.class);
                intent.putExtra("TAB_INDEX", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        // 3. Updates/Reports (Tab Index 2)
        if (navUpdates != null) {
            navUpdates.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminMainActivity.class);
                intent.putExtra("TAB_INDEX", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }

    private void selectTab(String tabName) {
        currentTab = tabName;
        resetTabStyles();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_row_student, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            UserItem item = userList.get(position);
            holder.tvName.setText(item.getName());
            holder.tvSubtitle.setText(item.getSubtitle());
        }

        @Override
        public int getItemCount() { return userList.size(); }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvSubtitle;
            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvSubtitle = itemView.findViewById(R.id.tvEmail);
            }
        }
    }
}