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
import com.example.topcv.adapter.NotificationAdapter;
import com.example.topcv.model.Notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationFragment extends Fragment {
    private RecyclerView NotificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        NotificationRecyclerView = view.findViewById(R.id.NotificationRecyclerView);

        // Khởi tạo danh sách thông báo
        notificationList = new ArrayList<>();
        notificationList.add(new Notification(1, "The employer has just viewed your application information.", Calendar.getInstance()));
        notificationList.add(new Notification(2, "Data science recruitment, Google corporation just viewed your recruitment information.", Calendar.getInstance()));
        notificationList.add(new Notification(3, "Your profile has been viewed by several recruiters.", Calendar.getInstance()));
        notificationList.add(new Notification(4, "You have received a new job offer!", Calendar.getInstance()));

        // Thiết lập RecyclerView
        NotificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        NotificationRecyclerView.setAdapter(notificationAdapter);

        return view;
    }
}
