package com.elective.school_management_system;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentInstructorAdapter extends RecyclerView.Adapter<StudentInstructorAdapter.ViewHolder> {

    private Context context;
    private List<Instructor> instructorList;

    public StudentInstructorAdapter(Context context, List<Instructor> instructorList) {
        this.context = context;
        this.instructorList = instructorList;
    }

    // NEW METHOD: Used to update the list when searching
    public void filterList(List<Instructor> filteredList) {
        this.instructorList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_instructor_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instructor instructor = instructorList.get(position);

        holder.tvName.setText(instructor.getName());
        holder.tvDept.setText(instructor.getDepartment());

        holder.btnNavigate.setOnClickListener(v -> {
            Toast.makeText(context, "Navigating to: " + instructor.getName(), Toast.LENGTH_SHORT).show();
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Cannot open camera", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDept;
        LinearLayout btnNavigate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDept = itemView.findViewById(R.id.tvDept);
            btnNavigate = itemView.findViewById(R.id.btnNavigate);
        }
    }
}