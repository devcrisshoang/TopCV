package com.example.topcv.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Job implements Parcelable {
    private int id;
    private int imageId;
    private String jobName;
    private String companyName;
    private String location;
    private String experience;
    private String salary;
    private int remainingTime;
    private boolean isCheck;

    // Constructor
    public Job(int imageId, String jobName, String companyName, String location, String experience, String salary, int remainingTime, boolean isCheck) {
        this.imageId = imageId;
        this.jobName = jobName;
        this.companyName = companyName;
        this.location = location;
        this.experience = experience;
        this.salary = salary;
        this.remainingTime = remainingTime;
        this.isCheck = isCheck;
    }

    public Job(int imageId, String jobName, String companyName, String location, String salary) {
        this.imageId = imageId;
        this.jobName = jobName;
        this.companyName = companyName;
        this.location = location;
        this.salary = salary;
    }

    // Constructor nhận đối tượng Parcel
    protected Job(Parcel in) {
        imageId = in.readInt();
        jobName = in.readString();
        companyName = in.readString();
        location = in.readString();
        experience = in.readString();
        salary = in.readString();
        remainingTime = in.readInt();
        isCheck = in.readByte() != 0; // Convert byte to boolean
    }

    // Ghi dữ liệu vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(jobName);
        dest.writeString(companyName);
        dest.writeString(location);
        dest.writeString(experience);
        dest.writeString(salary);
        dest.writeInt(remainingTime);
        dest.writeByte((byte) (isCheck ? 1 : 0)); // Convert boolean to byte
    }

    // Phương thức mô tả nội dung
    @Override
    public int describeContents() {
        return 0;
    }

    // Đối tượng CREATOR để tạo đối tượng từ Parcel
    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
