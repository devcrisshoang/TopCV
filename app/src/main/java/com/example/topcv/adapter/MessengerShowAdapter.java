package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.MessageActivity;
import com.example.topcv.R;
import com.example.topcv.model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessengerShowAdapter extends RecyclerView.Adapter<MessengerShowAdapter.MessengerViewHolder> {

    private List<Message> messageList;
    private Context context;

    public MessengerShowAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_message, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {
        Message message = messageList.get(position);
        // Thêm log để kiểm tra nội dung của message
        Log.d("MessengerAdapter", "Message content: " + message.getContent() + ", Sender ID: " + message.getSender_ID());

        // Các logic cũ của bạn
        // Xử lý hiển thị dựa trên Sender_ID
        if (message.getSender_ID() == 1) {
            // Nếu Sender_ID = 1, hiển thị tin nhắn của người dùng
            holder.me.setText(message.getContent());
            holder.other_people.setVisibility(View.GONE); // Ẩn tin nhắn của người khác
        } else if (message.getSender_ID() == 2) {
            // Nếu Sender_ID = 2, hiển thị tin nhắn của người khác
            holder.other_people.setText(message.getContent());
            holder.me.setVisibility(View.GONE); // Ẩn tin nhắn của mình
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder để quản lý từng phần tử trong RecyclerView
    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        public TextView other_people;
        public TextView me;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            other_people = itemView.findViewById(R.id.other_people);
            me = itemView.findViewById(R.id.me);
        }
    }
}
