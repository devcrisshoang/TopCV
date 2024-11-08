package com.example.topcv.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.topcv.AboutActicity;
import com.example.topcv.LoginActivity;
import com.example.topcv.PrivatePolicyActivity;
import com.example.topcv.R;
import com.example.topcv.TermOfServiceActivity;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.model.Applicant;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountFragment extends Fragment {
    private Button about_application_button;
    private Button term_of_services_button;
    private Button privacy_policy_button;
    private Button sign_out_button;
    private Button change_background_button;
    private ImageView background;
    private ImageView avatar;
    private ImageView change_avatar;

    private TextView name;
    private TextView number;
    private TextView jobDesireEditText;
    private TextView workingLocationDesireEditText;
    private TextView workingExperienceEditText;
    private Button editButton1; // Nút Edit cho jobDesire
    private Button editButton2; // Nút Edit cho workingLocationDesire
    private Button editButton3; // Nút Edit cho workingExperience


    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int id_User;
    private Applicant applicants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setWidget(view);
        if (getArguments() != null) {
            id_User = getArguments().getInt("user_id", -1);
            Log.e("ID","ID: "+id_User);
        }
        getApplicant(id_User);

        // Initialize ActivityResultLaunchers for image picking
        initImagePicker();

        // Set up listeners for buttons
        initListeners();

        return view;
    }

    private void getApplicant(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                applicants = applicant;
                                // Cập nhật các TextView tại đây
                                name.setText(applicants.getApplicantName());
                                number.setText(applicants.getPhoneNumber());
                                jobDesireEditText.setText(applicants.getJobDesire());
                                workingLocationDesireEditText.setText(applicants.getWorkingLocationDesire());
                                workingExperienceEditText.setText(applicants.getWorkingExperience());
                            } else {
                                Toast.makeText(getContext(), "Applicant null", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load applicant", Toast.LENGTH_SHORT).show();
                        }
                );
    }


    private void setWidget(View view) {
        applicants = new Applicant();
        jobDesireEditText = view.findViewById(R.id.job); // Thay đổi ID nếu cần
        workingLocationDesireEditText = view.findViewById(R.id.location); // Thay đổi ID nếu cần
        workingExperienceEditText = view.findViewById(R.id.experience); // Thay đổi ID nếu cần
        name = view.findViewById(R.id.name);
        about_application_button = view.findViewById(R.id.about_application_button);
        term_of_services_button = view.findViewById(R.id.term_of_services_button);
        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        change_background_button = view.findViewById(R.id.change_background_button);
        background = view.findViewById(R.id.background);
        avatar = view.findViewById(R.id.avatar);
        change_avatar = view.findViewById(R.id.change_avatar);
        editButton1 = view.findViewById(R.id.textView2); // Nút Edit cho jobDesire
        editButton2 = view.findViewById(R.id.textView4); // Nút Edit cho workingLocationDesire
        editButton3 = view.findViewById(R.id.textView9); // Nút Edit cho workingExperience
        number = view.findViewById(R.id.number);
    }

    private void initListeners() {
        about_application_button.setOnClickListener(view -> startActivity(new Intent(getContext(), AboutActicity.class)));
        privacy_policy_button.setOnClickListener(view -> startActivity(new Intent(getContext(), PrivatePolicyActivity.class)));
        term_of_services_button.setOnClickListener(view -> startActivity(new Intent(getContext(), TermOfServiceActivity.class)));

        sign_out_button.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Đăng xuất và chuyển về màn hình đăng nhập
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());

        change_background_button.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickerLauncherBackground.launch(intent);
                        return null;
                    });
        });

        change_avatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);
                        return null;
                    });
        });
    }

    private void initImagePicker() {
        imagePickerLauncherBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        background.setImageURI(uri);
                    } else {
                        Toast.makeText(getContext(), "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        avatar.setImageURI(uri);
                    } else {
                        Toast.makeText(getContext(), "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Giải phóng các tài nguyên
    }
}
