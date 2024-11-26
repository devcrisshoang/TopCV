package com.example.topcv.fragment;

import android.annotation.SuppressLint;
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
import com.example.topcv.api.ApiMessageService;
import com.example.topcv.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerFragment extends Fragment {

    private MessengerAdapter messageAdapter;

    private final List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);

        setWidget(view);

        return view;
    }

    private void setWidget(View view){
        int id_User = getArguments().getInt("user_id", 0);
        Log.e("MessageFragment", "User ID: "+ id_User);
        RecyclerView messageRecyclerView = view.findViewById(R.id.MessageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessengerAdapter(userList, getContext(), id_User);
        messageRecyclerView.setAdapter(messageAdapter);
        getChatPartners(id_User);
    }

    @SuppressLint({"CheckResult", "NotifyDataSetChanged"})
    private void getChatPartners(int userId) {
        ApiMessageService.apiMessageService.getAllChatPartnersByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> {
                            userList.clear();
                            userList.addAll(users);

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
