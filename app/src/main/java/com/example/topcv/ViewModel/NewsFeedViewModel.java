package com.example.topcv.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.topcv.model.Job;
import com.example.topcv.model.Company;
import com.example.topcv.model.Article;

import java.util.List;

public class NewsFeedViewModel extends ViewModel {

    private final MutableLiveData<List<Job>> workList = new MutableLiveData<>();
    private final MutableLiveData<List<Job>> bestJobList = new MutableLiveData<>();
    private final MutableLiveData<List<Job>> interestingJobList = new MutableLiveData<>();
    private final MutableLiveData<List<Company>> companyList = new MutableLiveData<>();
    private final MutableLiveData<List<Article>> articleList = new MutableLiveData<>();

    // Getters for LiveData
    public LiveData<List<Job>> getWorkList() {
        return workList;
    }

    public LiveData<List<Job>> getBestJobList() {
        return bestJobList;
    }

    public LiveData<List<Job>> getInterestingJobList() {
        return interestingJobList;
    }

    public LiveData<List<Company>> getCompanyList() {
        return companyList;
    }

    public LiveData<List<Article>> getArticleList() {
        return articleList;
    }

    // Setters for updating LiveData
    public void setWorkList(List<Job> jobs) {
        workList.setValue(jobs);
    }

    public void setBestJobList(List<Job> jobs) {
        bestJobList.setValue(jobs);
    }

    public void setInterestingJobList(List<Job> jobs) {
        interestingJobList.setValue(jobs);
    }

    public void setCompanyList(List<Company> companies) {
        companyList.setValue(companies);
    }

    public void setArticleList(List<Article> articles) {
        articleList.setValue(articles);
    }
}

