package com.culinarycompass.hci.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culinarycompass.hci.R;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private final Context context;
    private final List<String> sections;
    private final OnSectionClickListener listener;

    public SectionAdapter(Context context, List<String> sections, OnSectionClickListener listener) {
        this.context = context;
        this.sections = sections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String section = sections.get(position);
        holder.sectionName.setText(section);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSectionClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;

        ViewHolder(View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
        }
    }

    public interface OnSectionClickListener {
        void onSectionClick(int position);
    }
}
