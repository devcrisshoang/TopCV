package com.example.topcv;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;
import java.util.Objects;

public class CreateCvActivity extends AppCompatActivity {
    private Button add_new_cv_button;
    private ImageButton information_back_button;
    private ImageView camera_imageview, user_avatar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_cv);

        // Đặt padding cho các view để tránh overlap với system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        add_new_cv_button = findViewById(R.id.add_new_cv_button);
        information_back_button = findViewById(R.id.information_back_button);
        add_new_cv_button.setOnClickListener(v -> showStyleDialog());
        information_back_button.setOnClickListener(view -> {
            finish();
        });
        camera_imageview = findViewById(R.id.camera_imageview);
        user_avatar = findViewById(R.id.user_avatar);
        camera_imageview.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    private void showStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose CV Style")
                .setItems(new String[]{"Basic", "Professional"}, (dialog, which) -> {
                    if (which == 0) {
                        // Handle Basic CV
                        createBasicCV();
                    } else if (which == 1) {
                        // Handle Professional CV
                        createProfessionalCV();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            user_avatar.setImageURI(uri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void createBasicCV() {
        // Code to create a Basic CV
    }

    private void createProfessionalCV() {
        // Code to create a Professional CV
    }
}
