package com.example.topcv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Enterprise implements Parcelable {
    private String enterprise_name;
    private List<Company> companies_list;

    // Constructor
    public Enterprise(String enterprise_name, List<Company> jobs_list) {
        this.enterprise_name = enterprise_name;
        this.companies_list = jobs_list;
    }

    // Constructor nhận đối tượng Parcel
    protected Enterprise(Parcel in) {
        enterprise_name = in.readString();
        companies_list = new ArrayList<>();
        in.readList(companies_list, Jobs.class.getClassLoader());
    }

    // Ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enterprise_name);
        dest.writeList(companies_list);
    }

    // Phương thức mô tả nội dung
    @Override
    public int describeContents() {
        return 0;
    }

    // Đối tượng CREATOR để tạo đối tượng từ Parcel
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    // Getter và Setter
    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name_name(String category_name) {
        this.enterprise_name = category_name;
    }

    public List<Company> getEnterprise_list() {
        return companies_list;
    }

    public void setCompanies_list(List<Company> jobs_list) {
        this.companies_list = jobs_list;
    }
}
