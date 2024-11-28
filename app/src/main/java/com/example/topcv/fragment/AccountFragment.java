package com.example.topcv.fragment;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.topcv.AboutActicity;
import com.example.topcv.ChangePasswordActivity;
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
    private Button edit_experience;
    private Button edit_job;
    private Button edit_location;
    private Button change_password_button;

    private ImageView background;
    private ImageView avatar;
    private ImageView change_avatar;

    private TextView name;
    private TextView number;
    private TextView job;
    private TextView location;
    private TextView experience;

    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Applicant applicants;

    private int id_User;
    private int applicant_id;

    private View dialogView;

    private EditText editTextJobDesire;

    private Uri backgroundImageUri;
    private Uri avatarImageUri;

    private String username;
    private String password;
    private String currentAvatarUrl;
    private String currentBackgroundUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        setWidget(view);

        getApplicant(id_User);

        getUserById(id_User);

        initImagePicker();

        initListeners();

        return view;
    }

    @SuppressLint("CheckResult")
    private void getApplicant(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                applicants = applicant;
                                applicant_id = applicant.getId();
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

    @SuppressLint("CheckResult")
    private void updateApplicantInfo(String experience, String job, String location){
        Applicant applicant = new Applicant();
        applicant.setWorkingExperience(experience);
        applicant.setJobDesire(job);
        applicant.setWorkingLocationDesire(location);
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
        job = view.findViewById(R.id.job);
        location = view.findViewById(R.id.location);
        experience = view.findViewById(R.id.experience);
        name = view.findViewById(R.id.name);
        about_application_button = view.findViewById(R.id.about_application_button);
        term_of_services_button = view.findViewById(R.id.term_of_services_button);
        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        change_background_button = view.findViewById(R.id.change_background_button);
        background = view.findViewById(R.id.background);
        avatar = view.findViewById(R.id.avatar);
        change_avatar = view.findViewById(R.id.change_avatar);
        edit_experience = view.findViewById(R.id.edit_experience);
        edit_job = view.findViewById(R.id.edit_job);
        edit_location = view.findViewById(R.id.edit_location);
        number = view.findViewById(R.id.number);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_edit, null);
        editTextJobDesire = dialogView.findViewById(R.id.et_job_name);
        change_password_button = view.findViewById(R.id.change_password_button);

        if (getArguments() != null) {
            id_User = getArguments().getInt("user_id", -1);
            Log.e("ID","ID: "+id_User);
        }
    }

    private void initListeners() {
        about_application_button.setOnClickListener(view -> startActivity(new Intent(getContext(), AboutActicity.class)));
        privacy_policy_button.setOnClickListener(view -> startActivity(new Intent(getContext(), PrivatePolicyActivity.class)));
        term_of_services_button.setOnClickListener(view -> startActivity(new Intent(getContext(), TermOfServiceActivity.class)));

        sign_out_button.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                .setTitle("Confirm logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
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

        change_password_button.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtra("user_id", id_User);
            intent.putExtra("user_id", id_User);
            startActivity(intent);
        });

        edit_experience.setOnClickListener(view -> {

            editTextJobDesire.setText(experience.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to update this information?")
                        .setPositiveButton("Yes", (dialog1, which) -> {
                            updateApplicantInfo(editTextJobDesire.getText().toString(), job.getText().toString(),location.getText().toString());
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .show();
            });

            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                dialog.dismiss();
            });
            dialog.show();
        });

        edit_job.setOnClickListener(v -> {
            editTextJobDesire.setText(job.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v1 -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to update this information?")
                        .setPositiveButton("Yes", (dialog1, which) -> {
                            updateApplicantInfo( experience.getText().toString(),editTextJobDesire.getText().toString(),location.getText().toString());
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .show();
            });

            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                dialog.dismiss();
            });
            dialog.show();
        });

        edit_location.setOnClickListener(view -> {
            editTextJobDesire.setText(location.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to update this information?")
                        .setPositiveButton("Yes", (dialog1, which) -> {
                            updateApplicantInfo(experience.getText().toString(),job.getText().toString(),editTextJobDesire.getText().toString());
                            dialog.dismiss();
                        })
                        .setNegativeButton("Nos", (dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .show();
            });

            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v12 -> {
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void initImagePicker() {
        imagePickerLauncherBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        backgroundImageUri = result.getData().getData();
                        background.setImageURI(backgroundImageUri);
                        updateBackground(backgroundImageUri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        avatarImageUri = result.getData().getData();
                        avatar.setImageURI(avatarImageUri);
                        updateAvatar(avatarImageUri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @SuppressLint("CheckResult")
    private void getUserById(int userId) {
        ApiUserService.apiUserService.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            if (user != null) {
                                username = user.getUsername();
                                password = user.getPassword();
                                Log.e("username", "username: " + username);
                                Log.e("password", "password: " + password);

                                currentAvatarUrl = user.getAvatar();  // Store current avatar URL
                                currentBackgroundUrl = user.getImageBackground();
                                setUserImages(currentAvatarUrl, currentBackgroundUrl);
                            } else {
                                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching user: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load user", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void updateAvatar(Uri avatarUri) {
        String avatarUrl;

        if (avatarUri != null) {
            avatarUrl = avatarUri.toString();
        } else {
            avatarUrl = (currentAvatarUrl != null) ? currentAvatarUrl : "";
        }

        if (avatarUrl.isEmpty()) {
            Log.e("AccountFragment", "Avatar is empty, skipping update.");
            return;
        }

        String backgroundUrl = (backgroundImageUri != null) ? backgroundImageUri.toString() : (currentBackgroundUrl != null) ? currentBackgroundUrl : "";

        User user = new User();
        user.setId(id_User);
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(avatarUrl);
        user.setImageBackground(backgroundUrl);

        ApiUserService.apiUserService.updateUserById(id_User, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            // Thành công
                            Log.d("AccountFragment", "User updated successfully");
                            Toast.makeText(getContext(), "User info updated successfully", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            // Lỗi
                            Log.e("AccountFragment", "Error updating user: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                        }
                );
    }


    @SuppressLint("CheckResult")
    private void updateBackground(Uri backgroundUri) {
        String backgroundUrl;

        if (backgroundUri != null) {
            backgroundUrl = backgroundUri.toString();
        } else {
            backgroundUrl = (currentBackgroundUrl != null) ? currentBackgroundUrl : "";
        }

        if (backgroundUrl.isEmpty()) {
            Log.e("AccountFragment", "Background is empty, skipping update.");
            return;
        }

        String avatarUrl = (avatarImageUri != null) ? avatarImageUri.toString() : (currentAvatarUrl != null) ? currentAvatarUrl : "";

        User user = new User();
        user.setId(id_User);
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(avatarUrl);
        user.setImageBackground(backgroundUrl);

        ApiUserService.apiUserService.updateUserById(id_User, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("AccountFragment", "User updated successfully");
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error updating user: " + throwable.getMessage());
                        }
                );
    }

    private void setUserImages(String avatarUrl, String backgroundUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .override(200, 200)
                    .fitCenter()
                    .placeholder(R.drawable.account_ic)
                    .error(R.drawable.account_ic)
                    .into(avatar);

        }
        if (backgroundUrl != null && !backgroundUrl.isEmpty()) {
            Glide.with(this)
                    .load(backgroundUrl)
                    .override(1080, 720)
                    .fitCenter()
                    .placeholder(R.drawable.google_ic)
                    .error(R.drawable.google_ic)
                    .into(background);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
