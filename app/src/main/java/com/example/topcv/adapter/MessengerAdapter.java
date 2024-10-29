package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.MessageActivity;
import com.example.topcv.R;
import com.example.topcv.model.Message;
import com.example.topcv.model.User;
import com.example.topcv.utils.DateTimeUtils;

import java.util.List;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.MessengerViewHolder> {

    private List<User> userList;
    private List<List<Message>> allMessagesList;
    private Context context;

    public MessengerAdapter(List<User> userList, List<List<Message>> allMessagesList, Context context) {
        this.userList = userList;
        this.allMessagesList = allMessagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_messenger, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {
        User user = userList.get(position);
        List<Message> messages = allMessagesList.get(position);

        // Hiển thị tên người dùng và tin nhắn mới nhất
        holder.sender_name.setText(user.getUsername());

        if (!messages.isEmpty()) {
            holder.message.setText(messages.get(messages.size() - 1).getContent());

            String time = messages.get(messages.size() - 1).getSend_Time();
            // Kiểm tra xem thời gian có null không
            if (time != null && !time.isEmpty()) {
                String formattedTime = DateTimeUtils.formatTimeAgo(time);
                holder.send_time.setText(formattedTime);
            } else {
                holder.send_time.setText("No time available");
            }
        } else {
            holder.message.setText("No messages");
            holder.send_time.setText("No time available");
        }

        // Thiết lập sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để mở MessageActivity
            Intent intent = new Intent(context, MessageActivity.class);

            // Truyền userId của người dùng vào Intent
            intent.putExtra("userId", user.getId());  // Truyền ID người dùng

            // Bắt đầu MessageActivity
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder để quản lý từng phần tử trong RecyclerView
    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        public TextView sender_name;
        public TextView message;
        public TextView send_time;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            sender_name = itemView.findViewById(R.id.sender_name);
            send_time = itemView.findViewById(R.id.send_time);
        }
    }
}
