package kr.ac.daejin.restaurantnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class PlaceData implements Parcelable {
    private String category = "";
    private String city = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String managementNum = "";
    private String name = "";
    private String newAddress = "";
    private String oldAddress = "";
    private long postNum = 0;
    private String status = "";
    private String tel = "";
    private boolean isJoin = false;


    public PlaceData(String category, String city, double latitude, double longitude, String managementNum, String name, String newAddress, String oldAddress, long postNum, String status, String tel, boolean isJoin) {
        this.category = category;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managementNum = managementNum;
        this.name = name;
        this.newAddress = newAddress;
        this.oldAddress = oldAddress;
        this.postNum = postNum;
        this.status = status;
        this.tel = tel;
        this.isJoin = isJoin;
    }

    public PlaceData(){

    }

    protected PlaceData(Parcel in) {
        category = in.readString();
        city = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        managementNum = in.readString();
        name = in.readString();
        newAddress = in.readString();
        oldAddress = in.readString();
        postNum = in.readLong();
        status = in.readString();
        tel = in.readString();
    }

    public static final Creator<PlaceData> CREATOR = new Creator<PlaceData>() {
        @Override
        public PlaceData createFromParcel(Parcel in) {
            return new PlaceData(in);
        }

        @Override
        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(managementNum);
        dest.writeString(name);
        dest.writeString(newAddress);
        dest.writeString(oldAddress);
        dest.writeLong(postNum);
        dest.writeString(status);
        dest.writeString(tel);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("city", city);
        result.put("managementNum", managementNum);
        result.put("newAddress", newAddress);
        result.put("oldAddress", oldAddress);
        result.put("postNum", postNum);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("status", status);
        result.put("tel", tel);
        result.put("category", category);

        return result;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getManagementNum() {
        return managementNum;
    }

    public void setManagementNum(String managementNum) {
        this.managementNum = managementNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public long getPostNum() {
        return postNum;
    }

    public void setPostNum(long postNum) {
        this.postNum = postNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }
}
