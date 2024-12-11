package com.culinarycompass.hci;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.culinarycompass.hci.Fragments.ChatBotDialog;
import com.culinarycompass.hci.Fragments.Homefragment;

public class MainActivity extends AppCompatActivity {


    TextView search;
    ImageView back;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);


        fragmentManager = getSupportFragmentManager();

        replaceFragment(new Homefragment());

        search.setOnClickListener(v -> {
          startActivity(new Intent(MainActivity.this, SearchScreen.class));
        });

        findViewById(R.id.menu).setOnClickListener(v -> {
                ChatBotDialog chatBotDialog = new ChatBotDialog(null);
                chatBotDialog.show(getSupportFragmentManager(), "ChatBotDialog");
        });

        back.setOnClickListener(v->{
            replaceFragment(new Homefragment());
        });


    }
    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}