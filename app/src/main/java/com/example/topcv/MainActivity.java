package com.example.topcv;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.topcv.fragment.AccountFragment;
import com.example.topcv.fragment.NewsFeedFragment;
import com.example.topcv.fragment.NotificationFragment;
import com.example.topcv.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation_view_main;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
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
        replaceFragment(new NewsFeedFragment());
        navigation_view_main.getMenu().findItem(R.id.home).setChecked(true);
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
            }
        } else if (id == R.id.profile) {
            if(currentFragment != FRAGMENT_PROFILE){
                replaceFragment(new ProfileFragment());
                currentFragment = FRAGMENT_PROFILE;
            }
        } else if (id == R.id.notification) {
            if(currentFragment != FRAGMENT_NOTIFICATION){
                replaceFragment(new NotificationFragment());
                currentFragment = FRAGMENT_NOTIFICATION;
            }
        } else if (id == R.id.account){
            if(currentFragment != FRAGMENT_ACCOUNT){
                replaceFragment(new AccountFragment());
                currentFragment = FRAGMENT_ACCOUNT;
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