package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CreateCvActivity;
import com.example.topcv.R;
import com.example.topcv.adapter.ProfileAdapter;
import com.example.topcv.model.CV;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private Button create_cv_button;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        create_cv_button = view.findViewById(R.id.create_cv_button);
        recyclerView = view.findViewById(R.id.recyclerView);

        create_cv_button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CreateCvActivity.class);
            startActivity(intent);
        });

        List<CV> appItems = fetchAppData();

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        // Truyền context vào adapter
        adapter = new ProfileAdapter(getContext(), appItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<CV> fetchAppData() {
        List<CV> appItems = new ArrayList<>();
        appItems.add(new CV(1, "Khanh", "khanh@", "dh", "3 nam", "english", "ielts", "intern", R.drawable.account_ic));
        appItems.add(new CV(1, "Khanh", "khanh@", "dh", "3 nam", "english", "ielts", "intern", R.drawable.account_ic));
        return appItems;
    }
}
