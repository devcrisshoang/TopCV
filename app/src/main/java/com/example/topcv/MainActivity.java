package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.topcv.fragment.AccountFragment;
import com.example.topcv.fragment.MessengerFragment;
import com.example.topcv.fragment.NewsFeedFragment;
import com.example.topcv.fragment.NotificationFragment;
import com.example.topcv.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation_view_main;
    private LinearLayout layout_header;
    private EditText search_edit_text;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
    private static final int FRAGMENT_MESSENGER = 4;
    private int currentFragment = FRAGMENT_HOME;
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
        navigation_view_main = findViewById(R.id.navigation_view_main);
        layout_header = findViewById(R.id.layout_header);
        search_edit_text = findViewById(R.id.search_edit_text);
        navigation_view_main.setOnNavigationItemSelectedListener(item -> {
            onOptionsItemSelected(item);
            return true;
        });

        replaceFragment(new NewsFeedFragment());
        navigation_view_main.getMenu().findItem(R.id.home).setChecked(true);
        search_edit_text.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu từ file XML
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home){
            if(currentFragment != FRAGMENT_HOME){
                replaceFragment(new NewsFeedFragment());
                currentFragment = FRAGMENT_HOME;
                layout_header.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.profile) {
            if(currentFragment != FRAGMENT_PROFILE){
                replaceFragment(new ProfileFragment());
                currentFragment = FRAGMENT_PROFILE;
                layout_header.setVisibility(View.GONE);

            }
        } else if (id == R.id.messenger) {
            if(currentFragment != FRAGMENT_MESSENGER){
                replaceFragment(new MessengerFragment());
                currentFragment = FRAGMENT_MESSENGER;
                layout_header.setVisibility(View.GONE);

            }
        } else if (id == R.id.notification) {
            if(currentFragment != FRAGMENT_NOTIFICATION){
                replaceFragment(new NotificationFragment());
                currentFragment = FRAGMENT_NOTIFICATION;
                layout_header.setVisibility(View.GONE);
            }
        } else if (id == R.id.account){
            if(currentFragment != FRAGMENT_ACCOUNT){
                replaceFragment(new AccountFragment());
                currentFragment = FRAGMENT_ACCOUNT;
                layout_header.setVisibility(View.GONE);
            }
        }
        return true;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.news,fragment);
        transaction.commit();
    }
}