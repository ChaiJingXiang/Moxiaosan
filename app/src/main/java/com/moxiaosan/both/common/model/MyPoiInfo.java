package com.moxiaosan.both.common.model;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/18.
 */
public class MyPoiInfo implements Serializable {
    private static final long serialVersionUID = 7987336376004097898L;
    private String address;  //地址
    private Double latitude;// 纬度
    private Double longitude;// 经度
    private String city;    //城市
    private String key; //关键字

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
