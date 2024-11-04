package com.example.topcv.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiMessageService;
import com.example.topcv.model.Applicant;
import com.example.topcv.model.Message;
import com.example.topcv.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerFragment extends Fragment {

    private RecyclerView messageRecyclerView;
    private MessengerAdapter messageAdapter;
    private List<User> userList = new ArrayList<>(); // Danh sách người dùng đã nhắn tin

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        messageRecyclerView = view.findViewById(R.id.MessageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo adapter ngay lập tức
        messageAdapter = new MessengerAdapter(userList, getContext());
        messageRecyclerView.setAdapter(messageAdapter);

        // Fetch data để hiển thị trong RecyclerView
        int userId = 9; // Giả sử đây là ID của người dùng đã đăng nhập
        getChatPartners(userId);

        return view;
    }

    private void getChatPartners(int userId) {
        ApiMessageService.apiMessageService.getAllChatPartnersByUserId(userId)
                .subscribeOn(Schedulers.io()) // Thực hiện trên thread background
                .observeOn(AndroidSchedulers.mainThread()) // Quan sát trên thread UI
                .subscribe(
                        users -> {
                            userList.clear(); // Xóa danh sách cũ nếu có
                            userList.addAll(users); // Thêm tất cả người dùng vào danh sách

                            // Cập nhật lại RecyclerView sau khi dữ liệu thay đổi
                            if (messageAdapter != null) {
                                messageAdapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerFragment", "Error fetching chat partners: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load chat partners", Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
