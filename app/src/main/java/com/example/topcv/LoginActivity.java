package com.example.topcv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.topcv.API.ApiUserService;
import com.example.topcv.model.User;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    private EditText usernameInput, passwordInput;
    private Button loginButton, Register_Button;
    private ImageButton facebookButton, googleButton;

    // Sử dụng SharedPreferences để lưu trữ trạng thái đã truy cập InformationActivity
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String HAS_VISITED_INFO_KEY = "hasVisitedInformationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Thiết lập Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo các thành phần UI
        usernameInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        Register_Button = findViewById(R.id.Register_Button);
        facebookButton = findViewById(R.id.btnImage2);
        googleButton = findViewById(R.id.btnImage3);

        // Xử lý nút đăng nhập
        loginButton.setOnClickListener(view -> loginUser());

        // Xử lý nút đăng ký
        Register_Button.setOnClickListener(view -> {
            Intent intent1 = new Intent(LoginActivity.this, SignUpActivity.class);
            intent1.putExtra("isSignUpButtonClicked", true);
            startActivityForResult(intent1, 1); // Thêm mã yêu cầu
        });

        // Xử lý nút đăng nhập bằng Facebook
        facebookButton.setOnClickListener(view -> loginWithFacebook());

        // Xử lý nút đăng nhập bằng Google
        googleButton.setOnClickListener(view -> signInWithGoogle());
    }

    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiUserService.apiUserService.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    boolean isValidUser = false;

                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            isValidUser = true;

                            if (!user.getPassword().equals(password)) {
                                Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent intent;
                            boolean hasVisitedInformationActivity = sharedPreferences.getBoolean(HAS_VISITED_INFO_KEY + "_" + username, false);

                            if (!hasVisitedInformationActivity) {
                                // Nếu là lần đăng nhập đầu tiên cho tài khoản này, chuyển đến InformationActivity
                                intent = new Intent(LoginActivity.this, InformationActivity.class);
                                // Cập nhật SharedPreferences cho tài khoản này
                                sharedPreferences.edit().putBoolean(HAS_VISITED_INFO_KEY + "_" + username, true).apply();
                            } else {
                                // Nếu không, chuyển đến MainActivity
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            }

                            intent.putExtra("user_id", user.getId()); // Truyền ID người dùng
                            startActivity(intent);
                            finish(); // Kết thúc Activity hiện tại
                            return;
                        }
                    }

                    if (!isValidUser) {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối đến server: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, null);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            saveUserData(user); // Gọi phương thức để lưu thông tin người dùng
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Facebook login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                saveUserData(user); // Gọi phương thức để lưu thông tin người dùng
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Google login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(FirebaseUser user) {
        String email = user.getEmail();
        String uid = user.getUid();

        // Tạo một đối tượng User
        User newUser = new User(email, "", 0, 0, 0, uid);
        newUser.setUsername(email); // Sử dụng email làm tên đăng nhập
        newUser.setPassword(""); // Không cần lưu mật khẩu
        newUser.setImage_Background(0); // Hoặc một giá trị mặc định
        newUser.setAvatar(0); // Hoặc một giá trị mặc định

        // Gọi API để lưu thông tin người dùng
        ApiUserService.apiUserService.addUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // Kiểm tra nếu người dùng lần đầu đăng nhập thì chuyển đến InformationActivity
                    Intent intent;
                    boolean hasVisitedInformationActivity = sharedPreferences.getBoolean(HAS_VISITED_INFO_KEY, false);

                    if (!hasVisitedInformationActivity) {
                        // Nếu là lần đăng nhập đầu tiên, chuyển đến InformationActivity
                        intent = new Intent(LoginActivity.this, InformationActivity.class);
                        sharedPreferences.edit().putBoolean(HAS_VISITED_INFO_KEY, true).apply();
                    } else {
                        // Nếu không, chuyển đến MainActivity
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    }

                    startActivity(intent);
                    finish();
                }, throwable -> {
                    Toast.makeText(LoginActivity.this, "Lỗi khi lưu thông tin người dùng: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
