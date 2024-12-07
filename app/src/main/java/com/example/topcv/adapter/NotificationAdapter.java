package com.example.topcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.model.Notification;
import com.example.topcv.utils.DateTimeUtils;
import java.util.List;
import java.util.Stack;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notificationStack; // Sử dụng Stack, nhưng xử lý như List
    private Context context;

    public NotificationAdapter(Stack<Notification> notificationStack, Context context) {
        this.notificationStack = notificationStack;
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
        Notification notification = notificationStack.get(position); // Truy cập phần tử từ Stack
        String timeString = notification.getTime();
        String formattedTime = (timeString != null) ? DateTimeUtils.formatTimeAgo(timeString) : "No time available";
        holder.textViewTime.setText(formattedTime);
        holder.textViewContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notificationStack.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContent;
        public TextView textViewTime;
        public ImageView company_logo;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            textViewTime = itemView.findViewById(R.id.TimeOfNotification);
            company_logo = itemView.findViewById(R.id.company_logo);
        }
    }
}
