package com.example.topcv.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.adapter.MessengerAdapter;
import com.example.topcv.model.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessengerFragment extends Fragment {
    private RecyclerView messageRecyclerView;
    private MessengerAdapter messageAdapter;
    private List<Message> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        messageRecyclerView = view.findViewById(R.id.MessageRecyclerView);

        // Dữ liệu mẫu cho danh sách tin nhắn
        messageList = new ArrayList<>();
        messageList.add(new Message(1, 101, 202, "Hello!", false, "10:10 PM"));
        messageList.add(new Message(2, 102, 202, "How are you?", false, "10:10 PM"));
        messageList.add(new Message(3, 103, 202, "Good morning!", false, "10:10 PM"));

        // Thiết lập RecyclerView
        messageAdapter = new MessengerAdapter(messageList, getContext());
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageRecyclerView.setAdapter(messageAdapter);

        return view;
    }
}
