package com.culinarycompass.hci.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.culinarycompass.hci.DishScreen;
import com.culinarycompass.hci.Filters.RestFilter;
import com.culinarycompass.hci.R;
import com.culinarycompass.hci.Model.RestNearModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHandler> implements Filterable {

    Context context;
   public ArrayList<RestNearModel> arrayList,filteredList;

   private RestFilter restFilter;


    public SearchAdapter(Context context, ArrayList<RestNearModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        filteredList = arrayList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_lay, parent, false);
        return new ViewHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHandler holder, int position) {

        RestNearModel item = arrayList.get(position); // Use filteredList here
        Glide.with(context).load(item.getImage()).into(holder.image);

        int roundedRating = (item.getNratings() / 50) * 50;
        String formattedRating = roundedRating >= 50 ? roundedRating + "+" : String.valueOf(item.getNratings());

        holder.title.setText(item.getTitle());
        holder.rating.setText(String.valueOf(item.getRating()) + "★ ( " + formattedRating + " ) " + item.getDistance() + " mi");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishScreen.class);
            intent.putExtra("id", String.valueOf(item.getId()));
            intent.putExtra("image", String.valueOf(item.getImage()));
            intent.putExtra("title", String.valueOf(item.getTitle()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    @Override
    public Filter getFilter() {
       if(restFilter == null){
           restFilter = new RestFilter(this, filteredList);
       }
       return restFilter;
    }

    public class ViewHandler extends RecyclerView.ViewHolder {
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
