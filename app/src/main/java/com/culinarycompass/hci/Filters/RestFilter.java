package com.culinarycompass.hci.Filters;

import android.widget.Filter;

import com.culinarycompass.hci.Adapters.SearchAdapter;
import com.culinarycompass.hci.Model.RestNearModel;

import java.util.ArrayList;

public class RestFilter  extends Filter {

    private SearchAdapter adapter;
    private ArrayList<RestNearModel> filterList;

    public RestFilter(SearchAdapter adapter, ArrayList<RestNearModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){




            constraint = constraint.toString().toUpperCase();

            ArrayList<RestNearModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(
                        filterList.get(i).getTitle().toUpperCase().contains(constraint)
                ){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.arrayList = (ArrayList<RestNearModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }
}
