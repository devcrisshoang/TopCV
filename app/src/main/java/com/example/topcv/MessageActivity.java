package com.example.topcv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.api.ApiApplicantService;
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
    private RecyclerView MessageShowRecyclerView;
    private MessengerShowAdapter messengerShowAdapter;
    private List<Message> messageList;
    private Disposable disposable;
    private ImageButton messenger_send_button;
    private EditText input_message_edittext;
    private int userId;
    private TextView friend_name; // lay ten applicant

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Khởi tạo View
        back_button = findViewById(R.id.back_button);
        MessageShowRecyclerView = findViewById(R.id.MessageShowRecyclerView);
        messenger_send_button = findViewById(R.id.messenger_send_button);
        input_message_edittext = findViewById(R.id.input_message_edittext);
        friend_name = findViewById(R.id.friend_name);

        userId = getIntent().getIntExtra("userId", -1);  // Nhận giá trị userId
        getApplicantName(userId);

        // Kiểm tra nếu userId hợp lệ
        if (userId != -1) {
            getAPIData(userId);
        }

        // Khởi tạo danh sách và adapter trống ban đầu
        messageList = new ArrayList<>();

        // Đóng Activity khi nhấn nút quay lại
        back_button.setOnClickListener(view -> finish());

        // Gọi API để lấy dữ liệu

        messengerShowAdapter = new MessengerShowAdapter(messageList, userId);

        // Gán LayoutManager và Adapter cho RecyclerView
        MessageShowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageShowRecyclerView.setAdapter(messengerShowAdapter);

        // Bổ sung trong onCreate để xử lý sự kiện click nút gửi tin nhắn
        messenger_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    // Method to get applicant name based on the user ID
    private void getApplicantName(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                friend_name.setText(applicant.getApplicantName());
                                Log.d("MessengerAdapter", "Fetched applicant name: " + applicant.getApplicantName());
                            } else {
                                friend_name.setText("Unknown User"); // Hoặc xử lý lỗi nếu không có dữ liệu
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant name: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load applicant name", Toast.LENGTH_SHORT).show();
                        }
                );

    }
    // Hàm gửi tin nhắn
    private void sendMessage() {
        String messageContent = input_message_edittext.getText().toString().trim();
        String currentTime = DateTimeUtils.getCurrentTime();

        if (!messageContent.isEmpty()) {
            // Tạo đối tượng Message mới
            Message newMessage = new Message(
                    0,  // ID tạm thời (server sẽ tự sinh)
                    9,  // sender_ID là 9
                    userId,  // receiver_ID
                    messageContent,
                    false,  // status giả định là "sent"
                    currentTime  // send_Time giả định, dùng thời gian hiện tại
            );

            // Gọi API để gửi tin nhắn
            ApiMessageService.apiMessageService.postMessage(newMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            // Quản lý Disposable nếu cần
                        }

                        @Override
                        public void onNext(@NonNull Message messageResponse) {
                            // Tin nhắn được gửi thành công, xóa nội dung trong input sau khi gửi
                            input_message_edittext.setText("");

                            // Sau khi gửi tin nhắn thành công, gọi lại API để cập nhật toàn bộ tin nhắn
                            getAPIData(userId);
                            Toast.makeText(MessageActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            // Xử lý lỗi khi gửi tin nhắn thất bại
                            e.printStackTrace();
                            Toast.makeText(MessageActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            // Xử lý sau khi quá trình gửi hoàn tất
                        }
                    });
        } else {
            Toast.makeText(MessageActivity.this, "Message content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Thay thế `Single<List<Message>>` bằng kiểu trả về đúng
    public void getAPIData(int userId) {
        ApiMessageService.apiMessageService.getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Message> messages) {
                        // Log để kiểm tra
                        Log.d("MessageActivity", "Received messages: " + messages.toString());

                        // Lọc tin nhắn giữa người dùng có id = 9 và userId được truyền qua Intent
                        List<Message> filteredMessages = new ArrayList<>();
                        for (Message message : messages) {
                            if ((message.getSender_ID() == 9 && message.getReceiver_ID() == userId) ||
                                    (message.getSender_ID() == userId && message.getReceiver_ID() == 9)) {
                                filteredMessages.add(message);
                            }
                        }

                        // Cập nhật danh sách và thông báo adapter rằng dữ liệu đã thay đổi
                        messageList.clear();  // Xóa dữ liệu cũ
                        messageList.addAll(filteredMessages);  // Thêm dữ liệu mới đã được lọc
                        messengerShowAdapter.notifyDataSetChanged();  // Thông báo dữ liệu thay đổi
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MessageActivity.this, "Call API error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MessageActivity.this, "Call API successful", Toast.LENGTH_SHORT).show();
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
