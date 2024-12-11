package com.culinarycompass.hci;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.viewpager2.widget.ViewPager2;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import com.culinarycompass.hci.Adapters.ImageAdapter;
import com.culinarycompass.hci.Adapters.ViewPagerAdapter;
import com.culinarycompass.hci.Fragments.ChatBotDialog;
import com.culinarycompass.hci.Model.DishinfoModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DishInfoScreen extends AppCompatActivity {

    TextView dish_name, description, Ingredients, note, Calories, Protein, Carbohydrates, fat, recommended;
    RecyclerView rec;
    DishinfoModel currentDish;

    private ViewPager2 viewPager;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private int currentPage = 0;
    private ArrayList<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish_info_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rec = findViewById(R.id.images);
        description = findViewById(R.id.description);
        dish_name = findViewById(R.id.dish_name);
        Ingredients = findViewById(R.id.Ingredients);
        recommended = findViewById(R.id.diabetic_recommendation);
        note = findViewById(R.id.note);
        Calories = findViewById(R.id.Calories);
        Protein = findViewById(R.id.Protein);
        Carbohydrates = findViewById(R.id.Carbohydrates);
        fat = findViewById(R.id.fat);

        viewPager = findViewById(R.id.viewPager);

        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imageUrls.size()) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
                autoScrollHandler.postDelayed(this, 3000);
            }
        };

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String dishKey = getIntent().getStringExtra("key");
        if (dishKey != null) {
            fetchDishInfo(dishKey);
        }

        findViewById(R.id.menu).setOnClickListener(v -> {
            if (currentDish != null) {
                ChatBotDialog chatBotDialog = new ChatBotDialog(currentDish);
                chatBotDialog.show(getSupportFragmentManager(), "ChatBotDialog");
            }
        });

    }

    private void fetchDishInfo(String dishKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DishInfo");
        databaseReference.orderByChild("id").equalTo(Integer.parseInt(dishKey))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                DishinfoModel dish = snapshot.getValue(DishinfoModel.class);
                                if (dish != null) {
                                    currentDish = dish;
                                    populateUI(dish);
                                }
                            }
                        } else {
                            dish_name.setText("Dish not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dish_name.setText("Error fetching data");
                    }
                });
    }

    private void populateUI(DishinfoModel dish) {
        dish_name.setText(dish.getName());
        description.setText(dish.getDescription());
        String ingredientsList = dish.getIngredients();

        Ingredients.setText(ingredientsList);

        String NotesList = dish.getNote();
        SpannableString spannableNoteString = new SpannableString(NotesList);

        note.setText(NotesList);
        Calories.setText(String.valueOf(dish.getNutritional_info().getCalories()));
        Protein.setText(String.valueOf(dish.getNutritional_info().getProtein()));
        Carbohydrates.setText(String.valueOf(dish.getNutritional_info().getCarbohydrates()));
        fat.setText(String.valueOf(dish.getNutritional_info().getFat()));

        if (dish.isDiabetic_rec()) {
            recommended.setText("Diabetic Friendly");
            recommended.setBackgroundResource(R.drawable.diabetic_friendly_background);
        } else {
            recommended.setText("Diabetic Unfriendly");
            recommended.setBackgroundResource(R.drawable.diabetic_unfriendly_background);
        }

        imageUrls = dish.getImage();
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);

        setupImageRecyclerView(dish.getImage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void setupImageRecyclerView(ArrayList<String> imageUrls) {

        ImageAdapter adapter = new ImageAdapter(this, imageUrls);
        rec.setAdapter(adapter);
    }
}
