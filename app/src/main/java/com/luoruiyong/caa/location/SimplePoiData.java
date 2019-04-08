package com.luoruiyong.caa.location;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description:
 **/
public class SimplePoiData {
    private String name;
    private String address;
    private String province;
    private String city;
    private String district;

    public SimplePoiData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
