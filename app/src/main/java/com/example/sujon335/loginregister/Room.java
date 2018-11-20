package com.example.sujon335.loginregister;

/**
 * Created by sujon335 on 2/6/18.
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class Room implements Serializable{
    int placeid;
    String title;
    String room_type;
    String property_type;
    String location;
    double longitude;
    double latitude;
    int totalguests;
    int beds;
    int baths;
    int minstay;
    String contact;
    int price;
    int isavailable;
    int hostid;

    public Room(int placeid, String title, String room_type, String property_type, String location, double longitude, double latitude, int totalguests, int beds, int baths, int minstay, String contact, int price, int isavailable, int hostid) {
        this.placeid = placeid;
        this.title = title;
        this.room_type = room_type;
        this.property_type = property_type;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.totalguests = totalguests;
        this.beds = beds;
        this.baths = baths;
        this.minstay = minstay;
        this.contact = contact;
        this.price = price;
        this.isavailable = isavailable;
        this.hostid = hostid;
    }

    public int getPlaceid() {
        return placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getTotalguests() {
        return totalguests;
    }

    public void setTotalguests(int totalguests) {
        this.totalguests = totalguests;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public int getMinstay() {
        return minstay;
    }

    public void setMinstay(int minstay) {
        this.minstay = minstay;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(int isavailable) {
        this.isavailable = isavailable;
    }

    public int getHostid() {
        return hostid;
    }

    public void setHostid(int hostid) {
        this.hostid = hostid;
    }
}
