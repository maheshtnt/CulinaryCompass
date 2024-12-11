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
import com.culinarycompass.hci.DishScreen;
import com.culinarycompass.hci.R;
import com.culinarycompass.hci.Model.RestNearModel;

import java.util.ArrayList;

public class RestNearAdapter extends RecyclerView.Adapter<RestNearAdapter.ViewHandler> {

    Context context;
    ArrayList<RestNearModel> arrayList;

    public RestNearAdapter(Context context, ArrayList<RestNearModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RestNearAdapter.ViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rest_near_me, parent, false);
        return new ViewHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestNearAdapter.ViewHandler holder, int position) {


        Glide.with(context) .load(arrayList.get(position).getImage()).into(holder.image);


        int roundedRating = (arrayList.get(position).getNratings() / 50) * 50;

        String formattedRating = roundedRating >= 50 ? roundedRating + "+" : String.valueOf(arrayList.get(position).getNratings());


        holder.title.setText(arrayList.get(position).getTitle());
        holder.rating.setText(String.valueOf(arrayList.get(position).getRating()) + "★ ( " +formattedRating + " ) "  + String.valueOf(arrayList.get(position).getDistance()) +" mi");

        holder.itemView.setOnClickListener(v-> {
            Intent intent = new Intent(context, DishScreen.class);
            intent.putExtra("id", String.valueOf(arrayList.get(position).getId()));
            intent.putExtra("image", String.valueOf(arrayList.get(position).getImage()));
            intent.putExtra("title", String.valueOf(arrayList.get(position).getTitle()));
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHandler extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title,rating;

        public ViewHandler(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);


        }
    }
}
