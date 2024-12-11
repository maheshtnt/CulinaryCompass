package com.culinarycompass.hci;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.culinarycompass.hci.Adapters.DishAdapter;
import com.culinarycompass.hci.Adapters.SectionAdapter;
import com.culinarycompass.hci.Model.DishModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DishScreen extends AppCompatActivity {

    ImageView image;
    TextView text;
    FloatingActionButton menu;
    RecyclerView rec, sectionRec;

    DishAdapter dishAdapter;
    SectionAdapter sectionAdapter;
    LinearLayoutManager dishLayoutManager;
    Map<String, List<DishModel>> groupedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rec = findViewById(R.id.rec);
        sectionRec = findViewById(R.id.sectionRec);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        menu = findViewById(R.id.menu);

        text.setText(getIntent().getStringExtra("title"));
        Glide.with(DishScreen.this).load(getIntent().getStringExtra("image")).into(image);

        rec.setHasFixedSize(true);
        sectionRec.setHasFixedSize(true);

        dishLayoutManager = new LinearLayoutManager(this);
        rec.setLayoutManager(dishLayoutManager);

        fetchDishes();

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

    }

    private void fetchDishes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Dishes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupedData = new LinkedHashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DishModel dish = snapshot.getValue(DishModel.class);

                    if (dish != null && String.valueOf(dish.getRef()).equals(getIntent().getStringExtra("id"))) {
                        String type = dish.getType();
                        if (!groupedData.containsKey(type)) {
                            groupedData.put(type, new ArrayList<>());
                        }
                        groupedData.get(type).add(dish);
                    }
                }

                setupAdapters();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    private void setupAdapters() {
        dishAdapter = new DishAdapter(DishScreen.this, groupedData);
        rec.setAdapter(dishAdapter);

        ((SimpleItemAnimator) rec.getItemAnimator()).setSupportsChangeAnimations(false);

        sectionAdapter = new SectionAdapter(DishScreen.this, new ArrayList<>(groupedData.keySet()), position -> {
            scrollToSection(position);
        });
        sectionRec.setAdapter(sectionAdapter);
        sectionRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void scrollToSection(int position) {
        int targetPosition = dishAdapter.getSectionPosition(position);
        if (targetPosition != -1) {
            dishLayoutManager.scrollToPositionWithOffset(targetPosition, 0);
        }
    }
}
