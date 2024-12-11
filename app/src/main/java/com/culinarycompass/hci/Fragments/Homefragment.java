package com.culinarycompass.hci.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.culinarycompass.hci.Adapters.RestNearAdapter;
import com.culinarycompass.hci.Model.RestNearModel;
import com.culinarycompass.hci.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Homefragment extends Fragment {

    RecyclerView rec,rec_rest_near,rec_rest_near_dia;
    RestNearAdapter adapter;
    ArrayList<RestNearModel> restaurantList = new ArrayList<>();
    ArrayList<RestNearModel> restaurantList1 = new ArrayList<>();
    ArrayList<RestNearModel> restaurantList2 = new ArrayList<>();
    DatabaseReference databaseReference;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Homefragment() {
    }
    public static Homefragment newInstance(String param1, String param2) {
        Homefragment fragment = new Homefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homefragment, container, false);

        rec = view.findViewById(R.id.rec_rest_near_me);
        rec_rest_near = view.findViewById(R.id.rec_rest_near);
        rec_rest_near_dia = view.findViewById(R.id.rec_rest_near_dia);
        rec.setHasFixedSize(true);
        rec_rest_near.setHasFixedSize(true);
        rec_rest_near_dia.setHasFixedSize(true);

        fetchRestaurants();
        fetchRestaurant();
        fetchRestaurantDia();

        return view;
    }

    private void fetchRestaurants() {


        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");


        databaseReference.orderByChild("distance").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestNearModel restaurant = snapshot.getValue(RestNearModel.class);
                    restaurantList.add(restaurant);
                }
                adapter = new RestNearAdapter(getContext(), restaurantList);
                rec.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });

    }


    private void fetchRestaurant() {



        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Restaurants");

        databaseReference1.orderByChild("distance").endAt(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantList1.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestNearModel restaurant = snapshot.getValue(RestNearModel.class);

                    if (restaurant != null) {
                        restaurantList1.add(restaurant);
                    }
                }

                Collections.sort(restaurantList1, (r1, r2) -> Float.compare(r2.getRating(), r1.getRating()));


                adapter = new RestNearAdapter(getContext(), restaurantList1);
                rec_rest_near.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });

    }

    private void fetchRestaurantDia() {



        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Restaurants");

        databaseReference1.orderByChild("distance").endAt(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantList2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestNearModel restaurant = snapshot.getValue(RestNearModel.class);

                    if (restaurant != null) {
                        Log.d("FirebaseData", "Restaurant: " + restaurant.getTitle() +
                                ", Distance: " + restaurant.getDistance() +
                                ", DiabeticFriendly: " + restaurant.isDiabeticFriendly());

                        if (restaurant.isDiabeticFriendly()) {
                            restaurantList2.add(restaurant);
                        }
                    }
                }

                Collections.sort(restaurantList2, (r1, r2) -> Float.compare(r2.getRating(), r1.getRating()));

                if (restaurantList2.size() > 10) {
                    restaurantList2 = (ArrayList<RestNearModel>) restaurantList2.subList(0, 10);
                }

                adapter = new RestNearAdapter(getContext(), restaurantList2);
                rec_rest_near_dia.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });



    }
}