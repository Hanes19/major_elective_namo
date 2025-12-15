package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class TeacherRoomsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnSearch;
    private AppCompatButton btnGetDirections; // The button in the bottom sheet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_rooms);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);

        // This button is inside the ConstraintLayout 'bottomSheet' in t_rooms.xml
        // Ensure the ID in XML is generic or assign one like @+id/btnGetDirections
        // Based on your file, it just says <AppCompatButton ... text="Get Directions" ... />
        // You MUST add android:id="@+id/btnGetDirections" to that button in the XML for this to work.
        btnGetDirections = findViewById(R.id.bottomSheet).findViewWithTag("directions");
        // Note: Since I can't edit your XML, findByID(R.id.btnGetDirections) assumes you added the ID.
        // If you didn't add an ID, use: findViewById(R.id.bottomSheet).getChildAt(3) (risky) or better yet, add the ID.
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v ->
                Toast.makeText(this, "Search Map...", Toast.LENGTH_SHORT).show());

        // Launch AR Navigation
        // IMPORTANT: Add android:id="@+id/btnGetDirections" to the AppCompatButton in t_rooms.xml
        AppCompatButton realBtnDirections = findViewById(R.id.bottomSheet).findViewById(R.id.btnDirections_added_by_you);
        // Assuming you add the ID. For now, I will find it by traversing if needed, but best practice is adding ID.

        // Placeholder listener if you add the ID "@+id/btnGetDirections"
        if(btnGetDirections != null) {
            btnGetDirections.setOnClickListener(v -> {
                Intent intent = new Intent(TeacherRoomsActivity.this, TeacherARNavigationActivity.class);
                intent.putExtra("TARGET_ROOM", "Classroom 101"); // Pass data if needed
                startActivity(intent);
            });
        }
    }
}