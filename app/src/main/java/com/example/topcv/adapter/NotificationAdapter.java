package com.example.topcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Lấy Calendar từ notification
        Calendar calendar = notification.getTime();

        // Lấy thời gian hiện tại
        Calendar now = Calendar.getInstance();

        // Tính khoảng cách giữa thời gian của thông báo và thời gian hiện tại
        long timeDifference = now.getTimeInMillis() - calendar.getTimeInMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000; // Một ngày bằng mili giây

        String timeText;

        if (timeDifference < oneDayInMillis) {
            // Nếu thông báo trong ngày, hiển thị dạng giờ:phút
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeText = timeFormat.format(calendar.getTime());
        } else if (timeDifference < 2 * oneDayInMillis) {
            // Nếu thông báo là ngày hôm qua, hiển thị "Ngày hôm qua"
            timeText = "Ngày hôm qua";
        } else {
            // Nếu thông báo đã qua nhiều ngày, hiển thị số ngày trước đó
            int daysAgo = (int) (timeDifference / oneDayInMillis);
            timeText = daysAgo + " ngày trước";
        }

        // Đặt thời gian và nội dung cho TextView
        holder.textViewTime.setText(timeText);
        holder.textViewContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder để quản lý từng phần tử trong RecyclerView
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContent;
        public TextView textViewTime;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            textViewTime = itemView.findViewById(R.id.TimeOfNotification);
        }
    }
}
