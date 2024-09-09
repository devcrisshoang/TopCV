package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.CompanyInformationsActivity;
import com.example.topcv.adapter.MixedAdapter;
import com.example.topcv.model.Category;
import com.example.topcv.model.Jobs;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {
    private RecyclerView recyclerViewMixed;
    private MixedAdapter mixedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        recyclerViewMixed = view.findViewById(R.id.recyclerViewCategoryMix);

        List<Object> mixedList = new ArrayList<>();
        mixedList.addAll(getListCategory());

        // Tạo OnItemClickListener
        MixedAdapter.OnItemClickListener onItemClickListener = job -> {
            Intent intent = new Intent(requireContext(), CompanyInformationsActivity.class);
            intent.putExtra("JOB_DETAILS", job);
            startActivity(intent);
        };

        // Khởi tạo MixedAdapter với Context, List<Object>, và OnItemClickListener
        mixedAdapter = new MixedAdapter(mixedList, onItemClickListener);

        // Sử dụng LinearLayoutManager để hiện tất cả các items
        recyclerViewMixed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMixed.setAdapter(mixedAdapter);

        return view;
    }

    private List<Category> getListCategory() {
        List<Category> categories = new ArrayList<>();

        List<Jobs> jobs1 = new ArrayList<>();
        List<Jobs> jobs2 = new ArrayList<>();
        jobs1.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs1.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs1.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,false));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.fpt_ic,"Mobile Intern","FPT","Hanoi","No experience","$300",60,true));
        jobs2.add(new Jobs(R.drawable.viettel_ic,"Mobile Intern","Viettel","Hanoi","No experience","$300",60,true));

        categories.add(new Category("The most suitable jobs", jobs1));
        categories.add(new Category("The best jobs", jobs2));
        categories.add(new Category("Attractive jobs", jobs2));
        categories.add(new Category("Top company", jobs2));
        categories.add(new Category("Article", jobs2));
        return categories;
    }
}
