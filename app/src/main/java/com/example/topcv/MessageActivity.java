package com.example.topcv;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.api.ApiMessageService;
import com.example.topcv.adapter.MessengerShowAdapter;
import com.example.topcv.model.Message;
import com.example.topcv.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessageActivity extends AppCompatActivity {

    private ImageButton back_button;
    private ImageButton messenger_send_button;

    private MessengerShowAdapter messengerShowAdapter;

    private List<Message> messageList;

    private Disposable disposable;

    private EditText input_message_edittext;

    private int userIdApplicant;
    private int userIdRecruiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();

        setClick();
    }

    private void setClick(){

        back_button.setOnClickListener(view -> finish());

        messenger_send_button.setOnClickListener(v -> sendMessage());
    }

    private void setWidget(){
        back_button = findViewById(R.id.back_button);
        RecyclerView messageShowRecyclerView = findViewById(R.id.MessageShowRecyclerView);
        messenger_send_button = findViewById(R.id.messenger_send_button);
        input_message_edittext = findViewById(R.id.input_message_edittext);
        TextView friend_name = findViewById(R.id.friend_name);

        userIdApplicant = getIntent().getIntExtra("userIdApplicant", 0);
        String recruiterName = getIntent().getStringExtra("recruiterName");
        userIdRecruiter = getIntent().getIntExtra("userIdRecruiter", 0);

        friend_name.setText(recruiterName);

        if (userIdApplicant != 0) {
            getAPIData(userIdApplicant,userIdRecruiter);
        }

        messageList = new ArrayList<>();
        messengerShowAdapter = new MessengerShowAdapter(messageList, userIdApplicant, userIdRecruiter);
        messageShowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageShowRecyclerView.setAdapter(messengerShowAdapter);
    }

    private void sendMessage() {
        String messageContent = input_message_edittext.getText().toString().trim();
        String currentTime = DateTimeUtils.getCurrentTime();

        if (!messageContent.isEmpty()) {
            Message newMessage = new Message(
                    0,
                    userIdApplicant,
                    userIdRecruiter,
                    messageContent,
                    false,
                    currentTime
            );

            ApiMessageService.apiMessageService.postMessage(newMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull Message messageResponse) {
                            input_message_edittext.setText("");
                            getAPIData(userIdApplicant,userIdRecruiter);
                            Toast.makeText(MessageActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(MessageActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toast.makeText(MessageActivity.this, "Message content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAPIData(int MainID, int SubID) {
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(MainID, SubID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@NonNull List<Message> messages) {
                        messageList.clear();
                        messageList.addAll(messages);
                        messengerShowAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e("MessageActivity","Call API error");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("MessageActivity","Call API successful");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
