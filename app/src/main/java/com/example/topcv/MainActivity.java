package com.example.topcv;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout_header;

    private EditText search_edit_text;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
    private static final int FRAGMENT_MESSENGER = 4;
    private int currentFragment = FRAGMENT_HOME;

    private ImageButton homeButton;
    private ImageButton profileButton;
    private ImageButton messengerButton;
    private ImageButton notificationButton;
    private ImageButton accountButton;

    private TextView Home_textview;
    private TextView Profile_Textview;
    private TextView Messenger_Textview;
    private TextView Notification_Textview;
    private TextView Account_Textview;

    private String applicantName;
    private String phoneNumber;

    private int id_User;

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

        setWidget();

        openNewsFeedFragment(id_User);

        setDefaultColorButton();

        setClick();
    }

    private void setDefaultColorButton(){
        setImageButtonColor(this, homeButton, R.color.green_color);
        setImageButtonColor(this, profileButton, R.color.black);
        setImageButtonColor(this, messengerButton, R.color.black);
        setImageButtonColor(this, notificationButton, R.color.black);
        setImageButtonColor(this, accountButton, R.color.black);
    }

    private void setWidget(){
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

        applicantName = getIntent().getStringExtra("applicantName");
        id_User = getIntent().getIntExtra("user_id", 0);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
    }

    private void setClick(){
        homeButton.setOnClickListener(view -> selectFragment(FRAGMENT_HOME, new NewsFeedFragment(), layout_header, View.VISIBLE));
        profileButton.setOnClickListener(view -> selectFragment(FRAGMENT_PROFILE, new ProfileFragment(), layout_header, View.GONE));
        messengerButton.setOnClickListener(view -> selectFragment(FRAGMENT_MESSENGER, new MessengerFragment(), layout_header, View.GONE));
        notificationButton.setOnClickListener(view -> selectFragment(FRAGMENT_NOTIFICATION, new NotificationFragment(), layout_header, View.GONE));
        accountButton.setOnClickListener(view -> selectFragment(FRAGMENT_ACCOUNT, new AccountFragment(), layout_header, View.GONE));
        search_edit_text.setOnClickListener(view -> {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
        });
    }

    private void openNewsFeedFragment(int userId) {
        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        newsFeedFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.news, newsFeedFragment)
                .commit();
    }

    private void selectFragment(int fragmentCode, Fragment fragment, LinearLayout layoutHeader, int headerVisibility) {
        if (currentFragment != fragmentCode) {
            if (fragment instanceof AccountFragment || fragment instanceof ProfileFragment || fragment instanceof MessengerFragment || fragment instanceof NotificationFragment || fragment instanceof NewsFeedFragment) {
                Bundle bundle = new Bundle();
                bundle.putString("applicantName", applicantName);
                bundle.putInt("user_id", id_User);
                bundle.putString("phoneNumber", phoneNumber);
                fragment.setArguments(bundle);
            }
            replaceFragment(fragment);
            currentFragment = fragmentCode;
            layoutHeader.setVisibility(headerVisibility);
            resetButtonColors();
            setImageButtonColor(this, getButtonForFragment(fragmentCode), R.color.green_color);
            getTextViewForFragment(fragmentCode).setTextColor(getResources().getColor(R.color.green_color));
        }
    }

    private ImageButton getButtonForFragment(int fragmentCode) {
        switch (fragmentCode) {
            case FRAGMENT_PROFILE:
                return profileButton;
            case FRAGMENT_MESSENGER:
                return messengerButton;
            case FRAGMENT_NOTIFICATION:
                return notificationButton;
            case FRAGMENT_ACCOUNT:
                return accountButton;
            default:
                return homeButton;
        }
    }

    private TextView getTextViewForFragment(int fragmentCode) {
        switch (fragmentCode) {
            case FRAGMENT_PROFILE:
                return Profile_Textview;
            case FRAGMENT_MESSENGER:
                return Messenger_Textview;
            case FRAGMENT_NOTIFICATION:
                return Notification_Textview;
            case FRAGMENT_ACCOUNT:
                return Account_Textview;
            default:
                return Home_textview;
        }
    }

    private void resetButtonColors() {
        setImageButtonColor(this, homeButton, R.color.black);
        setImageButtonColor(this, profileButton, R.color.black);
        setImageButtonColor(this, messengerButton, R.color.black);
        setImageButtonColor(this, notificationButton, R.color.black);
        setImageButtonColor(this, accountButton, R.color.black);

        Home_textview.setTextColor(getResources().getColor(R.color.black));
        Profile_Textview.setTextColor(getResources().getColor(R.color.black));
        Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
        Notification_Textview.setTextColor(getResources().getColor(R.color.black));
        Account_Textview.setTextColor(getResources().getColor(R.color.black));
    }

    public static void setImageButtonColor(Context context, ImageButton button, int colorResId) {
        int color = ContextCompat.getColor(context, colorResId);
        button.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.news, fragment);
        transaction.commit();
    }
}
