package com.example.topcv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.MessageActivity;
import com.example.topcv.R;
import com.example.topcv.api.ApiMessageService;
import com.example.topcv.api.ApiRecruiterService;
import com.example.topcv.model.Message;
import com.example.topcv.model.User;
import com.example.topcv.utils.DateTimeUtils;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.MessengerViewHolder> {

    private final List<User> userList;

    private final Context context;

    private final int userIdApplicant;
    private int userIdRecruiter;

    private String recruiterName;

    public MessengerAdapter(List<User> userList, Context context, int userIdApplicant) {
        this.userList = userList;
        this.context = context;
        this.userIdApplicant = userIdApplicant;
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
        if (position >= userList.size()) {
            return;
        }

        User user = userList.get(position);
        int userId = user.getId();

        getRecruiterInformation(userId, holder);
        getLatestMessage(userIdApplicant, userId, holder);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("userIdApplicant",userIdApplicant);
            intent.putExtra("recruiterName",recruiterName);
            intent.putExtra("userIdRecruiter",userId);

            context.startActivity(intent);
        });
    }


    @SuppressLint("CheckResult")
    private void getRecruiterInformation(int userId, MessengerViewHolder holder) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                holder.sender_name.setText(recruiter.getRecruiterName());
                                recruiterName = recruiter.getRecruiterName();
                                userIdRecruiter = recruiter.getIdUser();
                                holder.company_logo.setImageResource(R.drawable.recruiter_ic);
                                Log.d("MessengerAdapter", "Fetched applicant name: " + recruiter.getRecruiterName());
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant name: " + throwable.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void getLatestMessage(int currentUserId, int otherUserId, MessengerViewHolder holder) {
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(currentUserId, otherUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messages -> {
                            if (!messages.isEmpty()) {
                                Message latestMessage = messages.get(messages.size() - 1);
                                holder.message.setText(latestMessage.getContent());

                                String time = latestMessage.getSend_Time();
                                if (time != null && !time.isEmpty()) {
                                    String formattedTime = DateTimeUtils.formatTimeAgo(time);
                                    holder.send_time.setText(formattedTime);
                                }
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching messages: " + throwable.getMessage());
                            Toast.makeText(context, "Failed to load messages", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        private TextView sender_name;
        private TextView message;
        private TextView send_time;
        private ImageView company_logo;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            sender_name = itemView.findViewById(R.id.sender_name);
            send_time = itemView.findViewById(R.id.send_time);
            company_logo = itemView.findViewById(R.id.company_logo);
        }
    }
}