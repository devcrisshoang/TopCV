package com.example.topcv.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.adapter.MessengerAdapter;
import com.example.topcv.api.ApiMessageService;
import com.example.topcv.model.Message;
import com.example.topcv.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerFragment extends Fragment {
    private RecyclerView messageRecyclerView;
    private MessengerAdapter messageAdapter;
    private List<User> userList = new ArrayList<>();  // Danh sách người dùng đã nhắn tin
    private final List<List<Message>> allMessagesList = new ArrayList<>();  // Danh sách chứa các danh sách tin nhắn giữa user 9 và các người dùng khác

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        messageRecyclerView = view.findViewById(R.id.MessageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách người dùng đã nhắn tin với ID 9
        ApiMessageService.apiMessageService.getAllChatPartnersByUserId(9)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    // Sau khi nhận được danh sách người dùng, lưu vào userList
                    userList = users;

                    // Khởi tạo danh sách chứa tin nhắn cho từng người dùng
                    for (int i = 0; i < userList.size(); i++) {
                        allMessagesList.add(new ArrayList<>()); // Khởi tạo một danh sách rỗng cho mỗi người dùng
                    }

                    // Gọi API để lấy tin nhắn giữa người dùng 9 và từng người trong danh sách
                    for (User user : userList) {
                        getMessagesWithUser(user.getId(), userList.indexOf(user)); // Gửi chỉ số để cập nhật đúng danh sách tin nhắn
                    }
                }, throwable -> {
                    // Xử lý lỗi khi gọi API
                    Toast.makeText(getContext(), "Error fetching chat partners", Toast.LENGTH_SHORT).show();
                });

        return view;
    }

    private void getMessagesWithUser(int otherUserId, int index) {
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(9, otherUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    // Thêm danh sách tin nhắn vào danh sách lớn
                    allMessagesList.set(index, messages); // Cập nhật danh sách tin nhắn cho người dùng

                    // Kiểm tra xem đã lấy đủ tin nhắn cho tất cả người dùng hay chưa
                    boolean allMessagesFetched = true;
                    for (List<Message> messageList : allMessagesList) {
                        if (messageList == null || messageList.isEmpty()) {
                            allMessagesFetched = false;
                            break;
                        }
                    }

                    if (allMessagesFetched) {
                        messageAdapter = new MessengerAdapter(userList, allMessagesList, getContext());
                        messageRecyclerView.setAdapter(messageAdapter);
                    }
                }, throwable -> {
                    // Xử lý lỗi khi gọi API
                    Toast.makeText(getContext(), "Error fetching messages", Toast.LENGTH_SHORT).show();
                });
    }
}
