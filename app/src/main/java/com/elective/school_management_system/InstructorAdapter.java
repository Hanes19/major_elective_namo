package com.elective.school_management_system;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {

    private Context context;
    private List<Instructor> instructorList;
    private DatabaseHelper dbHelper;

    public InstructorAdapter(Context context, List<Instructor> instructorList, DatabaseHelper dbHelper) {
        this.context = context;
        this.instructorList = instructorList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We reuse the room row layout or a generic row layout if you have one.
        // Ideally, create a layout named 'item_instructor_row.xml'
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_row, parent, false);
        return new InstructorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        Instructor instructor = instructorList.get(position);
        holder.tvName.setText(instructor.getName());
        holder.tvDesc.setText(instructor.getDepartment());

        // Handle Click to Edit
        holder.itemView.setOnClickListener(v -> showEditDialog(instructor, position));

        // Use Long Click to Delete
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(instructor, position);
            return true;
        });
    }

    private void showEditDialog(Instructor instructor, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Instructor");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText etName = new EditText(context);
        etName.setText(instructor.getName());
        etName.setHint("Name");
        layout.addView(etName);

        final EditText etDept = new EditText(context);
        etDept.setText(instructor.getDepartment());
        etDept.setHint("Department");
        layout.addView(etDept);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            boolean success = dbHelper.updateInstructor(instructor.getId(), etName.getText().toString(), etDept.getText().toString());
            if (success) {
                instructorList.set(position, new Instructor(instructor.getId(), etName.getText().toString(), etDept.getText().toString()));
                notifyItemChanged(position);
                Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteDialog(Instructor instructor, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Instructor")
                .setMessage("Are you sure you want to delete " + instructor.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean success = dbHelper.deleteInstructor(instructor.getId());
                    if (success) {
                        instructorList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    public static class InstructorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc;
        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRoomName); // Reusing existing IDs for simplicity
            tvDesc = itemView.findViewById(R.id.tvRoomDesc);
        }
    }
}