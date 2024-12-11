package com.culinarycompass.hci.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.culinarycompass.hci.DishInfoScreen;
import com.culinarycompass.hci.Model.DishModel;
import com.culinarycompass.hci.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SECTION_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private final Context context;
    private final List<Object> items;
    private final List<Integer> sectionPositions;

    public DishAdapter(Context context, Map<String, List<DishModel>> groupedData) {
        this.context = context;
        this.items = new ArrayList<>();
        this.sectionPositions = new ArrayList<>();

        int position = 0;
        for (Map.Entry<String, List<DishModel>> entry : groupedData.entrySet()) {
            items.add(entry.getKey());
            sectionPositions.add(position);
            position++;

            items.addAll(entry.getValue());
            position += entry.getValue().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? VIEW_TYPE_SECTION_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.dish_section_header, parent, false);
            return new SectionHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.dish_lay, parent, false);
            return new DishItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionHeaderViewHolder) {
            ((SectionHeaderViewHolder) holder).sectionTitle.setText((String) items.get(position));
        } else {
            DishModel dish = (DishModel) items.get(position);
            DishItemViewHolder dishHolder = (DishItemViewHolder) holder;

            dishHolder.title.setText(dish.getDish());
            dishHolder.description.setText(dish.getDescription());

            int drm = dish.getDRM();

            if (drm >= 0 && drm <= 4) {
                Glide.with(context).load(R.drawable.good_drm).into(dishHolder.image);
            } else if (drm > 4 && drm < 7) {
                Glide.with(context).load(R.drawable.okay_drm).into(dishHolder.image);
            } else if (drm >= 7 && drm <= 10) {
                Glide.with(context).load(R.drawable.bad_drm).into(dishHolder.image);
            }
            holder.itemView.setOnClickListener(V->{
                Intent intent = new Intent(context, DishInfoScreen.class);
                intent.putExtra("key",String.valueOf(dish.getId()));
                context.startActivity(intent);
            });

        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getSectionPosition(int sectionIndex) {
        if (sectionIndex >= 0 && sectionIndex < sectionPositions.size()) {
            return sectionPositions.get(sectionIndex);
        }
        return -1;
    }

    static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;

        SectionHeaderViewHolder(View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
        }
    }

    static class DishItemViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        DishItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
        }
    }
}
