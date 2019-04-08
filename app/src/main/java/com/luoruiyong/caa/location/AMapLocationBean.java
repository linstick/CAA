package com.luoruiyong.caa.location;

import com.amap.api.maps.model.Poi;
import com.amap.api.services.geocoder.StreetNumber;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description: 通过经纬度单点查询结果返回结构体
 **/
public class AMapLocationBean {

    int status;
    String info;
    int infocode;
    Regeocode regeocode;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getInfocode() {
        return infocode;
    }

    public void setInfocode(int infocode) {
        this.infocode = infocode;
    }

    public Regeocode getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(Regeocode regeocode) {
        this.regeocode = regeocode;
    }

    @Override
    public String toString() {
        return "AMapLocationBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", infocode=" + infocode +
                ", regeocode=" + regeocode +
                '}';
    }

    public class Regeocode {
        // 道路信息列表
        List<Road> roads;
        // 道路交叉口列表
        List<Roadinters> roadinters;
        // 结构化的地址信息
        String formatted_address;
        // 地址元素
        AddressComponent addressComponent;
        // Aoi信息列表
        List<Aoi> aois;
        // Pio信息列表
        List<Poi> pois;

        public Regeocode() {
        }

        public List<Road> getRoads() {
            return roads;
        }

        public void setRoads(List<Road> roads) {
            this.roads = roads;
        }

        public List<Roadinters> getRoadinters() {
            return roadinters;
        }

        public void setRoadinters(List<Roadinters> roadinters) {
            this.roadinters = roadinters;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public AddressComponent getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponent addressComponent) {
            this.addressComponent = addressComponent;
        }

        public List<Aoi> getAois() {
            return aois;
        }

        public void setAois(List<Aoi> aois) {
            this.aois = aois;
        }

        public List<Poi> getPois() {
            return pois;
        }

        public void setPois(List<Poi> pois) {
            this.pois = pois;
        }

        @Override
        public String toString() {
            return "Regeocode{" +
                    "roads=" + roads +
                    ", roadinters=" + roadinters +
                    ", formatted_address='" + formatted_address + '\'' +
                    ", addressComponent=" + addressComponent +
                    ", aois=" + aois +
                    ", pois=" + pois +
                    '}';
        }

        class Road {
            String id;
            String location;
            String direction;
            String name;
            double distance;

            public Road() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            @Override
            public String toString() {
                return "Road{" +
                        "id='" + id + '\'' +
                        ", location='" + location + '\'' +
                        ", direction='" + direction + '\'' +
                        ", name='" + name + '\'' +
                        ", distance=" + distance +
                        '}';
            }
        }

        class Roadinters {
            String first_name;
            String second_name;
            String first_id;
            String second_id;
            String location;
            double distance;
            String direction;

            public Roadinters() {
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getSecond_name() {
                return second_name;
            }

            public void setSecond_name(String second_name) {
                this.second_name = second_name;
            }

            public String getFirst_id() {
                return first_id;
            }

            public void setFirst_id(String first_id) {
                this.first_id = first_id;
            }

            public String getSecond_id() {
                return second_id;
            }

            public void setSecond_id(String second_id) {
                this.second_id = second_id;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            @Override
            public String toString() {
                return "Roadinters{" +
                        "first_name='" + first_name + '\'' +
                        ", second_name='" + second_name + '\'' +
                        ", first_id='" + first_id + '\'' +
                        ", second_id='" + second_id + '\'' +
                        ", location='" + location + '\'' +
                        ", distance=" + distance +
                        ", direction='" + direction + '\'' +
                        '}';
            }
        }

        class Aoi {
            String id;
            String name;
            String adcode;
            String location;
            String area;
            double distance;
            String type;

            public Aoi() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Aoi{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", adcode='" + adcode + '\'' +
                        ", location='" + location + '\'' +
                        ", area='" + area + '\'' +
                        ", distance=" + distance +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

        class Poi {
            String id;
            String name;
            String type;
            String tel;
            String direction;
            double distance;
            String location;
            String address;
            double poiweight;
            String bussinessarea;

            public Poi() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public double getPoiweight() {
                return poiweight;
            }

            public void setPoiweight(double poiweight) {
                this.poiweight = poiweight;
            }

            public String getBussinessarea() {
                return bussinessarea;
            }

            public void setBussinessarea(String bussinessarea) {
                this.bussinessarea = bussinessarea;
            }

            @Override
            public String toString() {
                return "Poi{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        ", tel=" + tel +
                        ", direction='" + direction + '\'' +
                        ", distance=" + distance +
                        ", location='" + location + '\'' +
                        ", address='" + address + '\'' +
                        ", poiweight=" + poiweight +
                        ", bussinessarea='" + bussinessarea + '\'' +
                        '}';
            }
        }

        public class AddressComponent {
            String contry;
            String province;
            String city;
            String citycode;
            String district;
            String adcode;
            String township;
            String towncode;
            Neighborhood neighborhood;
            Building building;
            StreetNumber streetNumber;
//            List<List<BusinessAreas>> businessAreas;

            public AddressComponent() {
            }

            public String getContry() {
                return contry;
            }

            public void setContry(String contry) {
                this.contry = contry;
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

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }

            public String getTowncode() {
                return towncode;
            }

            public void setTowncode(String towncode) {
                this.towncode = towncode;
            }

            public Neighborhood getNeighborhood() {
                return neighborhood;
            }

            public void setNeighborhood(Neighborhood neighborhood) {
                this.neighborhood = neighborhood;
            }

            public Building getBuilding() {
                return building;
            }

            public void setBuilding(Building building) {
                this.building = building;
            }

            public StreetNumber getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(StreetNumber streetNumber) {
                this.streetNumber = streetNumber;
            }
//
//            public List<List<BusinessAreas>> getBusinessAreas() {
//                return businessAreas;
//            }
//
//            public void setBusinessAreas(List<List<BusinessAreas>> businessAreas) {
//                this.businessAreas = businessAreas;
//            }

            @Override
            public String toString() {
                return "AddressComponent{" +
                        "contry='" + contry + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", citycode='" + citycode + '\'' +
                        ", district='" + district + '\'' +
                        ", adcode='" + adcode + '\'' +
                        ", township='" + township + '\'' +
                        ", towncode='" + towncode + '\'' +
                        ", neighborhood=" + neighborhood +
                        ", building=" + building +
                        ", streetNumber=" + streetNumber +
//                        ", businessAreas=" + businessAreas +
                        '}';
            }
        }

        class Neighborhood {
            String name;
            String type;

            public Neighborhood() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Neighborhood{" +
                        "name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

        class Building {
            String name;
            String type;

            public Building() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Neighborhood{" +
                        "name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

        class StreetNumber {
            String street;
            String number;
            String direction;

            public StreetNumber() {
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            double distance;



            @Override
            public String toString() {
                return "StreetNumber{" +
                        "street=" + street +
                        ", number=" + number +
                        ", direction=" + direction +
                        ", distance=" + distance +
                        '}';
            }
        }

        class BusinessAreas {
            String location;
            String name;
            int id;

            public BusinessAreas() {
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return "BusinessAreas{" +
                        "location='" + location + '\'' +
                        ", name='" + name + '\'' +
                        ", id=" + id +
                        '}';
            }
        }
    }
}
