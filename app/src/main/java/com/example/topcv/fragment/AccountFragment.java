package com.example.topcv.fragment;

import android.app.Activity;
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
import com.example.topcv.api.ApiUserService;
import com.example.topcv.model.Applicant;
import com.example.topcv.model.User;
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

    private TextView name;
    private TextView number;
    private TextView job;
    private TextView location;
    private TextView experience;
    private Button edit_experience; // Nút Edit cho jobDesire
    private Button edit_job; // Nút Edit cho workingLocationDesire
    private Button edit_location; // Nút Edit cho workingExperience


    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int id_User;
    private Applicant applicants;
    private int applicant_id;

    private LayoutInflater inflater;
    private View dialogView;

    // Khởi tạo EditText trong Dialog
    private EditText editTextJobDesire;

    private Uri backgroundImageUri;
    private Uri avatarImageUri;

    private String username;
    private String password;

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
        getUserById(id_User);

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
                                applicant_id = applicant.getId();
                                // Cập nhật các TextView tại đây
                                name.setText(applicants.getApplicantName());
                                number.setText(applicants.getPhoneNumber());
                                job.setText(applicants.getJobDesire());
                                location.setText(applicants.getWorkingLocationDesire());
                                experience.setText(applicants.getWorkingExperience());
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

    private void updateApplicantInfo(String experience, String job, String location){
        String experienced = experience;
        String work = job;
        String place = location;
        Applicant applicant = new Applicant();
        applicant.setWorkingExperience(experienced);
        applicant.setJobDesire(work);
        applicant.setWorkingLocationDesire(place);
        applicant.setApplicantName(name.getText().toString());
        applicant.setPhoneNumber(number.getText().toString());
        applicant.setiD_User(id_User);
        applicant.setIs_Registered(true);
        ApiApplicantService.ApiApplicantService.updateApplicantById(applicant_id, applicant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Xử lý thành công
                    Log.d("AccountFragment", "Updated successfully");
                    Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    // Xử lý lỗi
                    Log.e("AccountFragment", "Failed to update: " + throwable.getMessage());
                    Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }

    private void setWidget(View view) {
        applicants = new Applicant();
        job = view.findViewById(R.id.job); // Thay đổi ID nếu cần
        location = view.findViewById(R.id.location); // Thay đổi ID nếu cần
        experience = view.findViewById(R.id.experience); // Thay đổi ID nếu cần
        name = view.findViewById(R.id.name);
        about_application_button = view.findViewById(R.id.about_application_button);
        term_of_services_button = view.findViewById(R.id.term_of_services_button);
        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        change_background_button = view.findViewById(R.id.change_background_button);
        background = view.findViewById(R.id.background);
        avatar = view.findViewById(R.id.avatar);
        change_avatar = view.findViewById(R.id.change_avatar);
        edit_experience = view.findViewById(R.id.edit_experience); // Nút Edit cho jobDesire
        edit_job = view.findViewById(R.id.edit_job); // Nút Edit cho workingLocationDesire
        edit_location = view.findViewById(R.id.edit_location); // Nút Edit cho workingExperience
        number = view.findViewById(R.id.number);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_edit, null);
        editTextJobDesire = dialogView.findViewById(R.id.et_job_name);
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

        edit_experience.setOnClickListener(view -> {

            // Lấy giá trị hiện tại từ jobDesireEditText và đặt vào EditText trong Dialog
            editTextJobDesire.setText(experience.getText().toString());
            // Khởi tạo Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);

            // Tạo Dialog
            AlertDialog dialog = builder.create();

            // Xử lý sự kiện cho nút OK
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v -> {
                // Hiển thị AlertDialog để xác nhận thay đổi
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn cập nhật thông tin này không?")
                        .setPositiveButton("Có", (dialog1, which) -> {
                            updateApplicantInfo(editTextJobDesire.getText().toString(), job.getText().toString(),location.getText().toString());

                            // Đóng Dialog thay đổi
                            dialog.dismiss();
                        })
                        .setNegativeButton("Không", (dialog1, which) -> {
                            // Nếu không đồng ý, chỉ cần đóng dialog mà không làm gì
                            dialog1.dismiss();
                        })
                        .show();
            });

            // Xử lý sự kiện cho nút Cancel
            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                // Đóng Dialog mà không làm gì
                dialog.dismiss();
            });

            // Hiển thị Dialog
            dialog.show();
        });

        edit_job.setOnClickListener(v -> {
            // Khởi tạo LayoutInflater và Dialog
            // Lấy giá trị hiện tại từ jobDesireEditText và đặt vào EditText trong Dialog
            editTextJobDesire.setText(job.getText().toString());

            // Khởi tạo Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);

            // Tạo Dialog
            AlertDialog dialog = builder.create();

            // Xử lý sự kiện cho nút OK
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v1 -> {
                // Hiển thị AlertDialog để xác nhận thay đổi
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn cập nhật thông tin này không?")
                        .setPositiveButton("Có", (dialog1, which) -> {
                            updateApplicantInfo( experience.getText().toString(),editTextJobDesire.getText().toString(),location.getText().toString());

                            // Đóng Dialog thay đổi
                            dialog.dismiss();
                        })
                        .setNegativeButton("Không", (dialog1, which) -> {
                            // Nếu không đồng ý, chỉ cần đóng dialog mà không làm gì
                            dialog1.dismiss();
                        })
                        .show();
            });

            // Xử lý sự kiện cho nút Cancel
            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                // Đóng Dialog mà không làm gì
                dialog.dismiss();
            });

            // Hiển thị Dialog
            dialog.show();
        });

        edit_location.setOnClickListener(view -> {
            // Khởi tạo EditText trong Dialog

            // Lấy giá trị hiện tại từ jobDesireEditText và đặt vào EditText trong Dialog
            editTextJobDesire.setText(location.getText().toString());

            // Khởi tạo Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);

            // Tạo Dialog
            AlertDialog dialog = builder.create();

            // Xử lý sự kiện cho nút OK
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v -> {
                // Hiển thị AlertDialog để xác nhận thay đổi
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn cập nhật thông tin này không?")
                        .setPositiveButton("Có", (dialog1, which) -> {
                            updateApplicantInfo(experience.getText().toString(),job.getText().toString(),editTextJobDesire.getText().toString());

                            // Đóng Dialog thay đổi
                            dialog.dismiss();
                        })
                        .setNegativeButton("Không", (dialog1, which) -> {
                            // Nếu không đồng ý, chỉ cần đóng dialog mà không làm gì
                            dialog1.dismiss();
                        })
                        .show();
            });

            // Xử lý sự kiện cho nút Cancel
            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                // Đóng Dialog mà không làm gì
                dialog.dismiss();
            });

            // Hiển thị Dialog
            dialog.show();
        });
    }

    private void initImagePicker() {
        imagePickerLauncherBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Nhận URI của ảnh đã chọn
                        backgroundImageUri = result.getData().getData();

                        // Cập nhật hình ảnh cho ImageView background
                        background.setImageURI(backgroundImageUri);
                    } else {
                        Toast.makeText(getContext(), "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        avatarImageUri = result.getData().getData();
                        avatar.setImageURI(avatarImageUri);
                    } else {
                        Toast.makeText(getContext(), "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void getUserById(int userId){
        ApiUserService.apiUserService.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            if (user != null) {
                                username = user.getUsername();
                                Log.e("username","username" + username);
                                password = user.getPassword();
                                Log.e("password","password" + password);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Giải phóng các tài nguyên
    }
}
