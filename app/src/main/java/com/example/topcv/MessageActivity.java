package com.example.topcv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.api.ApiMessageService;
import com.example.topcv.adapter.MessengerShowAdapter;
import com.example.topcv.model.Message;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Khởi tạo View
        back_button = findViewById(R.id.back_button);
        MessageShowRecyclerView = findViewById(R.id.MessageShowRecyclerView);
        messenger_send_button = findViewById(R.id.messenger_send_button);
        input_message_edittext = findViewById(R.id.input_message_edittext);

        // Khởi tạo danh sách và adapter trống ban đầu
        messageList = new ArrayList<>();

        // Đóng Activity khi nhấn nút quay lại
        back_button.setOnClickListener(view -> finish());

        // Gọi API để lấy dữ liệu
        getAPIData();
        messengerShowAdapter = new MessengerShowAdapter(messageList, MessageActivity.this);

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
    // Hàm gửi tin nhắn
    private void sendMessage() {
        String messageContent = input_message_edittext.getText().toString().trim();

        if (!messageContent.isEmpty()) {
            // Tạo đối tượng Message
            Message newMessage = new Message(
                    0,  // ID tạm thời (server sẽ tự sinh)
                    9,  // sender_ID là 6
                    10,  // receiver_ID là 7
                    messageContent,
                    false,  // status giả định là "sent"
                    "2024-10-19T12:00:00"  // send_Time giả định, bạn có thể dùng thời gian hiện tại
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
                            // Tin nhắn được gửi thành công, thêm vào danh sách và cập nhật giao diện
                            messageList.add(messageResponse);
                            messengerShowAdapter.notifyDataSetChanged();

                            // Xóa nội dung trong input sau khi gửi
                            input_message_edittext.setText("");
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
    public void getAPIData() {
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

                        // Cập nhật danh sách và thông báo adapter rằng dữ liệu đã thay đổi
                        messageList.clear();  // Xóa dữ liệu cũ
                        messageList.addAll(messages);  // Thêm dữ liệu mới
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
