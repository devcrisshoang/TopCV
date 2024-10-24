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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.MessengerViewHolder> {

    private List<Message> messageList;
    private Context context;

    public MessengerAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
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
        Message message = messageList.get(position);
        // Các logic cũ của bạn

        // Thêm sự kiện click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("message_content", message.getContent());
            context.startActivity(intent);
        });

        // Đặt thời gian và nội dung cho TextView
        holder.message.setText(message.getContent());
        holder.send_time.setText("10:00 PM");
        holder.sender_name.setText("Khanh");
    }

    @Override
    public int getItemCount() {
        return messageList.size();
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
