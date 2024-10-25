package com.example.topcv.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.adapter.ArticleAdapter;
import com.example.topcv.adapter.CompanyTopAdapter;
import com.example.topcv.adapter.TheBestJobAdapter;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.model.Article;
import com.example.topcv.model.Company;
import com.example.topcv.model.Job;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {

    private ArticleAdapter articleAdapter;
    private CompanyTopAdapter companyTopAdapter;
    private TheBestJobAdapter theBestJobAdapter;
    private WorkAdapter workAdapter;

    private RecyclerView recyclerview_thesuitablejob;
    private RecyclerView recyclerview_thebestjob;
    private RecyclerView recyclerview_theinterestingjob;
    private RecyclerView recyclerview_thetopcompanies;
    private RecyclerView recyclerview_thearticle;

    private Button view_all_suitable_job;
    private Button view_all_best_job;
    private Button view_all_interesting_job;
    private Button view_all_top_company;
    private Button view_all_article;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        setWidget(view);
        TheSuitableJob();
        TheBestJob();
        TheInterestingJob();
        TheTopCompany();
        TheArticle();
        return view;
    }
    private void setWidget(View view){
        //adapter
        articleAdapter = new ArticleAdapter();
        companyTopAdapter = new CompanyTopAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        workAdapter = new WorkAdapter();
        //recyclerview
        recyclerview_thesuitablejob = view.findViewById(R.id.recyclerview_thesuitablejob);
        recyclerview_thebestjob = view.findViewById(R.id.recyclerview_thebestjob);
        recyclerview_theinterestingjob = view.findViewById(R.id.recyclerview_theinterestingjob);
        recyclerview_thetopcompanies = view.findViewById(R.id.recyclerview_thetopcompanies);
        recyclerview_thearticle = view.findViewById(R.id.recyclerview_thearticle);
        //button
        view_all_suitable_job = view.findViewById(R.id.view_all_suitable_job);
        view_all_best_job = view.findViewById(R.id.view_all_suitable_job);
        view_all_interesting_job = view.findViewById(R.id.view_all_suitable_job);
        view_all_top_company = view.findViewById(R.id.view_all_suitable_job);
        view_all_article = view.findViewById(R.id.view_all_suitable_job);
    }
    private void TheSuitableJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_thesuitablejob.setLayoutManager(linearLayoutManager);
        recyclerview_thesuitablejob.setFocusable(false);
        recyclerview_thesuitablejob.setNestedScrollingEnabled(false);
        workAdapter.setData(getListWork());
        recyclerview_thesuitablejob.setAdapter(workAdapter);
    }
    private void TheBestJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_thebestjob.setLayoutManager(linearLayoutManager);
        recyclerview_thebestjob.setFocusable(false);
        recyclerview_thebestjob.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(getListJob());
        recyclerview_thebestjob.setAdapter(theBestJobAdapter);
    }
    private void TheInterestingJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_theinterestingjob.setLayoutManager(linearLayoutManager);
        recyclerview_theinterestingjob.setFocusable(false);
        recyclerview_theinterestingjob.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(getListJob());
        recyclerview_theinterestingjob.setAdapter(theBestJobAdapter);
    }
    private void TheTopCompany(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerview_thetopcompanies.setLayoutManager(gridLayoutManager);
        recyclerview_thetopcompanies.setFocusable(false);
        recyclerview_thetopcompanies.setNestedScrollingEnabled(false);
        companyTopAdapter.setData(getListCompany());
        recyclerview_thetopcompanies.setAdapter(companyTopAdapter);
    }
    private void TheArticle(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_thearticle.setLayoutManager(linearLayoutManager);
        recyclerview_thearticle.setFocusable(false);
        recyclerview_thearticle.setNestedScrollingEnabled(false);
        articleAdapter.setData(getListArticle());
        recyclerview_thearticle.setAdapter(articleAdapter);
    }
    private List<Job> getListWork(){
        List<Job> list = new ArrayList<>();
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi","intern", "$300",30,false));
        return list;
    }
    private List<Job> getListJob(){
        List<Job> list = new ArrayList<>();
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi", "$300"));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi", "$300"));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi", "$300"));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi", "$300"));
        list.add(new Job(R.drawable.fpt_ic,"Mobile","FPT","Hanoi", "$300"));
        return list;
    }
    private List<Company> getListCompany(){
        List<Company> list = new ArrayList<>();
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        list.add(new Company("FPT",R.drawable.fpt_ic, "Technology"));
        return list;
    }
    private List<Article> getListArticle(){
        List<Article> list = new ArrayList<>();
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        list.add(new Article("FPT", "Catching up with the latest market trends and emerging technologies, FPT has developed the Made by FPT ecosystem of services, products, solutions, and platforms, which helps bring sustainable",R.drawable.fpt_ic));
        return list;
    }
}
