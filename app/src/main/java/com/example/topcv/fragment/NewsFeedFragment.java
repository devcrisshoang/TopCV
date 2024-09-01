package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.adapter.CategoryAdapter;
import com.example.topcv.model.Category;
import com.example.topcv.model.Jobs;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {
    private RecyclerView recyclerViewCategoryMix;
    private CategoryAdapter categoryAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        recyclerViewCategoryMix = view.findViewById(R.id.recyclerViewCategoryMix);
        // Initialize your category list here

        categoryAdapter = new CategoryAdapter(requireContext()); // Use requireContext() here
        categoryAdapter.setData(getListCategory());

        recyclerViewCategoryMix.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategoryMix.setAdapter(categoryAdapter);

        return view;
    }
    private List<Category> getListCategory(){
        List<Category> categories = new ArrayList<>();

        List<Jobs> jobs1 = new ArrayList<>();
        List<Jobs> jobs2 = new ArrayList<>();
        jobs1.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs1.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,true));
        categories.add(new Category("Suggest jobs",jobs1));
        categories.add(new Category("Suggest jobs1",jobs2));
        categories.add(new Category("Suggest jobs2",jobs2));
        categories.add(new Category("Suggest jobs3",jobs2));
        categories.add(new Category("Suggest jobs4",jobs2));
        return categories;
    }

}
