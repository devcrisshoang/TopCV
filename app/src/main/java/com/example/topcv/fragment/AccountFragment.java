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
    private TextView textView25;

    private EditText jobDesireEditText;
    private EditText workingLocationDesireEditText;
    private EditText workingExperienceEditText;
    private Button editButton1; // Nút Edit cho jobDesire
    private Button editButton2; // Nút Edit cho workingLocationDesire
    private Button editButton3; // Nút Edit cho workingExperience
    private int id_User; // Biến này cần được gán giá trị từ đâu đó

    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ApiApplicantService apiApplicantService;

    private String applicantName;
    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        apiApplicantService = ApiApplicantService.apiApplicantService;

        jobDesireEditText = view.findViewById(R.id.textView3); // Thay đổi ID nếu cần
        workingLocationDesireEditText = view.findViewById(R.id.textView6); // Thay đổi ID nếu cần
        workingExperienceEditText = view.findViewById(R.id.textView8); // Thay đổi ID nếu cần

        // Lấy `applicantId` và `applicantName` từ `Bundle`
        if (getArguments() != null) {
            id_User = getArguments().getInt("user_id", -1);
            applicantName = getArguments().getString("applicantName");
            phoneNumber = getArguments().getString("phoneNumber");
        }

        // Khởi tạo thành phần giao diện UI
        initUI(view);

        // Hiển thị tên ứng viên trong TextView
        if (applicantName != null && !applicantName.isEmpty()) {
            Log.d("AccountFragment", "Tên ứng viên: " + applicantName);
            textView25.setText(applicantName); // Đảm bảo textView25 đã được khởi tạo trong `initUI`
        } else {
            Log.e("AccountFragment", "applicantName là null hoặc rỗng.");
        }

        // Lấy thông tin ứng viên từ API
        loadApplicantInfo();

        // Initialize ActivityResultLaunchers for image picking
        initImagePicker();

        // Set up listeners for buttons
        initListeners();

        return view;
    }

    private void initUI(View view) {
        textView25 = view.findViewById(R.id.textView25);
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

        // Listener cho các nút Edit
        editButton1.setOnClickListener(view -> saveChanges("jobDesire"));
        editButton2.setOnClickListener(view -> saveChanges("workingLocationDesire"));
        editButton3.setOnClickListener(view -> saveChanges("workingExperience"));
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

    private void saveChanges(String field) {
        // Lấy thông tin từ EditText
        String jobDesire = jobDesireEditText.getText().toString().trim();
        String workingLocationDesire = workingLocationDesireEditText.getText().toString().trim();
        String workingExperience = workingExperienceEditText.getText().toString().trim();

        // Tạo đối tượng Applicant với thông tin cần thiết
        Applicant applicant = new Applicant();
        applicant.setId_User(id_User); // ID ứng viên
        applicant.setApplicant_Name(applicantName); // Giữ nguyên tên ứng viên
        applicant.setPhone_Number(phoneNumber);
        // Giữ nguyên số điện thoại
        applicant.setJob_Desire(jobDesire); // Chỉ gán nếu có thay đổi
        applicant.setWorking_Location_Desire(workingLocationDesire); // Chỉ gán nếu có thay đổi
        applicant.setWorking_Experience(workingExperience); // Chỉ gán nếu có thay đổi

        // Gửi yêu cầu cập nhật đến server
        compositeDisposable.add(
                apiApplicantService.updateApplicant(id_User, applicant)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            Toast.makeText(getContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                            Log.e("AccountFragment", "Lỗi khi cập nhật: " + throwable.getMessage());
                        })
        );
    }

    private void loadApplicantInfo() {
        compositeDisposable.add(
                apiApplicantService.getApplicantById(id_User)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(applicant -> {
                            // Gán giá trị cho các EditText chỉ khi thông tin ứng viên được tải thành công
                            jobDesireEditText.setText(applicant.getJob_Desire());
                            workingLocationDesireEditText.setText(applicant.getWorking_Location_Desire());
                            workingExperienceEditText.setText(applicant.getWorking_Experience());
                        }, throwable -> {
                            Toast.makeText(getContext(), "Không thể tải thông tin ứng viên", Toast.LENGTH_SHORT).show();
                            Log.e("AccountFragment", "Lỗi khi tải thông tin: " + throwable.getMessage());
                        })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Giải phóng các tài nguyên
    }
}
