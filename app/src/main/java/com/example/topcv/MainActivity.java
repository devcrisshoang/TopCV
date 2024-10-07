package com.example.topcv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private LinearLayout layout_header;
    private EditText search_edit_text;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
    private static final int FRAGMENT_MESSENGER = 4;
    private int currentFragment = FRAGMENT_HOME;
    private ImageButton homeButton, profileButton, messengerButton, notificationButton, accountButton;
    private TextView Home_textview,Profile_Textview,Messenger_Textview,Notification_Textview,Account_Textview;
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
        homeButton = findViewById(R.id.Home);
        profileButton = findViewById(R.id.Profile);
        messengerButton = findViewById(R.id.Messenger);
        notificationButton = findViewById(R.id.Notification);
        accountButton = findViewById(R.id.Account);
        layout_header = findViewById(R.id.layout_header);
        search_edit_text = findViewById(R.id.search_edit_text);
        Home_textview = findViewById(R.id.Home_textview);
        Profile_Textview = findViewById(R.id.Profile_Textview);
        Messenger_Textview = findViewById(R.id.Messenger_Textview);
        Notification_Textview = findViewById(R.id.Notification_Textview);
        Account_Textview = findViewById(R.id.Account_Textview);

        replaceFragment(new NewsFeedFragment());
        setImageButtonColor(this,homeButton, R.color.green_color);
        setImageButtonColor(this,profileButton, R.color.black);
        setImageButtonColor(this,messengerButton, R.color.black);
        setImageButtonColor(this,notificationButton, R.color.black);
        setImageButtonColor(this,accountButton, R.color.black);

        homeButton.setOnClickListener(view -> {
            setImageButtonColor(this,homeButton, R.color.green_color);
            setImageButtonColor(this,profileButton, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Home_textview.setTextColor(getResources().getColor(R.color.green_color));
            Profile_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));

            if(currentFragment != FRAGMENT_HOME){
                replaceFragment(new NewsFeedFragment());
                currentFragment = FRAGMENT_HOME;
                layout_header.setVisibility(View.VISIBLE);
            }
        });
        profileButton.setOnClickListener(view -> {
            setImageButtonColor(this,profileButton, R.color.green_color);
            setImageButtonColor(this,homeButton, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Home_textview.setTextColor(getResources().getColor(R.color.black));
            Profile_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_PROFILE){
                replaceFragment(new ProfileFragment());
                currentFragment = FRAGMENT_PROFILE;
                layout_header.setVisibility(View.GONE);

            }
        });
        messengerButton.setOnClickListener(v -> {
            setImageButtonColor(this,messengerButton, R.color.green_color);
            setImageButtonColor(this,homeButton, R.color.black);
            setImageButtonColor(this,profileButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Home_textview.setTextColor(getResources().getColor(R.color.black));
            Profile_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_MESSENGER){
                replaceFragment(new MessengerFragment());
                currentFragment = FRAGMENT_MESSENGER;
                layout_header.setVisibility(View.GONE);

            }
        });
        notificationButton.setOnClickListener(v -> {
            setImageButtonColor(this,notificationButton, R.color.green_color);
            setImageButtonColor(this,homeButton, R.color.black);
            setImageButtonColor(this,profileButton, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Home_textview.setTextColor(getResources().getColor(R.color.black));
            Profile_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_NOTIFICATION){
                replaceFragment(new NotificationFragment());
                currentFragment = FRAGMENT_NOTIFICATION;
                layout_header.setVisibility(View.GONE);
            }
        });
        accountButton.setOnClickListener(v -> {
            setImageButtonColor(this,accountButton, R.color.green_color);
            setImageButtonColor(this,homeButton, R.color.black);
            setImageButtonColor(this,profileButton, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            Home_textview.setTextColor(getResources().getColor(R.color.black));
            Profile_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.green_color));
            if(currentFragment != FRAGMENT_ACCOUNT){
                replaceFragment(new AccountFragment());
                currentFragment = FRAGMENT_ACCOUNT;
                layout_header.setVisibility(View.GONE);
            }
        });
        search_edit_text.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
        // Set listener cho các nút
//        setButtonListeners();
//        selectMenuItem(homeButton);
    }
    public static void setImageButtonColor(Context context, ImageButton button, int colorResId) {
        int color = ContextCompat.getColor(context, colorResId); // Lấy màu từ resources
        button.setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }

//    private void setButtonListeners() {
//        homeButton.setOnClickListener(v -> {
//            selectMenuItem(homeButton);
//            if(currentFragment != FRAGMENT_HOME){
//                replaceFragment(new NewsFeedFragment());
//                currentFragment = FRAGMENT_HOME;
//                layout_header.setVisibility(View.VISIBLE);
//            }
//        });
//
//        profileButton.setOnClickListener(v -> {
//            selectMenuItem(profileButton);
//            if(currentFragment != FRAGMENT_PROFILE){
//                replaceFragment(new ProfileFragment());
//                currentFragment = FRAGMENT_PROFILE;
//                layout_header.setVisibility(View.GONE);
//
//            }
//        });
//
//        messengerButton.setOnClickListener(v -> {
//            selectMenuItem(messengerButton);
//            if(currentFragment != FRAGMENT_MESSENGER){
//                replaceFragment(new MessengerFragment());
//                currentFragment = FRAGMENT_MESSENGER;
//                layout_header.setVisibility(View.GONE);
//
//            }
//        });
//
//        notificationButton.setOnClickListener(v -> {
//            selectMenuItem(notificationButton);
//            if(currentFragment != FRAGMENT_NOTIFICATION){
//                replaceFragment(new NotificationFragment());
//                currentFragment = FRAGMENT_NOTIFICATION;
//                layout_header.setVisibility(View.GONE);
//            }
//        });
//
//        accountButton.setOnClickListener(v -> {
//            selectMenuItem(accountButton);
//            if(currentFragment != FRAGMENT_ACCOUNT){
//                replaceFragment(new AccountFragment());
//                currentFragment = FRAGMENT_ACCOUNT;
//                layout_header.setVisibility(View.GONE);
//            }
//        });
//    }

//    private void selectMenuItem(LinearLayout selectedButton) {
//        // Đặt lại màu mặc định cho tất cả các nút
//        resetMenuColors();
//
//        // Đặt màu xanh cho nút được chọn
//        selectedButton.getChildAt(0).setBackgroundResource(R.color.green_color); // ImageButton
//        ((TextView) selectedButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.green_color)); // TextView
//    }

//    private void resetMenuColors() {
//        // Đặt lại màu nền cho tất cả các nút về mặc định
//        homeButton.getChildAt(0).setBackgroundResource(R.color.white);
//        ((TextView) homeButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.black));
//
//        profileButton.getChildAt(0).setBackgroundResource(R.color.white);
//        ((TextView) profileButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.black));
//
//        messengerButton.getChildAt(0).setBackgroundResource(R.color.white);
//        ((TextView) messengerButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.black));
//
//        notificationButton.getChildAt(0).setBackgroundResource(R.color.white);
//        ((TextView) notificationButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.black));
//
//        accountButton.getChildAt(0).setBackgroundResource(R.color.white);
//        ((TextView) accountButton.getChildAt(1)).setTextColor(getResources().getColor(R.color.black));
//    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.news,fragment);
        transaction.commit();
    }
}