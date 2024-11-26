package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.model.Message;
import java.util.List;

public class MessengerShowAdapter extends RecyclerView.Adapter<MessengerShowAdapter.MessengerViewHolder> {

    private final List<Message> messageList;

    private final int MainID;
    private final int SubID;

    public MessengerShowAdapter(List<Message> messageList, int MainID, int SubID) {
        this.messageList = messageList;
        this.MainID = MainID;
        this.SubID = SubID;
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

        holder.me.setVisibility(View.VISIBLE);
        holder.other_people.setVisibility(View.VISIBLE);

        if (message.getSender_ID() == MainID) {
            holder.me.setText(message.getContent());
            holder.other_people.setVisibility(View.GONE);
        } else if (message.getSender_ID() == SubID) {
            holder.other_people.setText(message.getContent());
            holder.me.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

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
