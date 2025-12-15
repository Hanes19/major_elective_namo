package com.elective.school_management_system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private Context context;
    private List<Report> list;
    private OnReportClickListener listener;

    public interface OnReportClickListener {
        void onViewReport(int reportId);
    }

    public ReportAdapter(Context context, List<Report> list, OnReportClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = list.get(position);
        holder.tvRoomName.setText(report.getRoomName());
        holder.tvDate.setText(report.getCategory() + " • " + report.getDate());

        holder.tvStatus.setText("• " + report.getStatus());
        int color = report.getStatus().equals("Resolved") ? 0xFF00E676 : 0xFFFF5252; // Green : Red
        holder.tvStatus.setTextColor(color);

        holder.btnView.setOnClickListener(v -> listener.onViewReport(report.getId()));
        holder.itemView.setOnClickListener(v -> listener.onViewReport(report.getId()));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvDate, tvStatus;
        ImageButton btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnView = itemView.findViewById(R.id.btnViewReport);
        }
    }
}