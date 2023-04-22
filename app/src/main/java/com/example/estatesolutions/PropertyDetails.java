package com.example.estatesolutions;

public class PropertyDetails {
    private String userId;
    private String name;
    private String price;
    private String model;
    private String area;
    private String dimension;
    private String floor;
    private String docks;
    private String Address;
    private String Extras;
    private String frontViewImageLink,rearViewImageLink,rightViewImageLink,leftViewImageLink,terraceViewImageLink,innerView1ImageLink,innerView2ImageLink,innerView3ImageLink;
    PropertyDetails(){};
    public PropertyDetails(String userId, String name, String price, String model, String area, String dimension, String floor, String docks, String address, String extras) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.model = model;
        this.area = area;
        this.dimension = dimension;
        this.floor = floor;
        this.docks = docks;
        Address = address;
        Extras = extras;
        this.frontViewImageLink = null;
        this.rearViewImageLink = null;
        this.rightViewImageLink = null;
        this.leftViewImageLink = null;
        this.terraceViewImageLink = null;
        this.innerView1ImageLink = null;
        this.innerView2ImageLink = null;
        this.innerView3ImageLink = null;
    }

    public String getFrontViewImageLink() {
        return frontViewImageLink;
    }

    public void setFrontViewImageLink(String frontViewImageLink) {
        this.frontViewImageLink = frontViewImageLink;
    }

    public String getRearViewImageLink() {
        return rearViewImageLink;
    }

    public void setRearViewImageLink(String rearViewImageLink) {
        this.rearViewImageLink = rearViewImageLink;
    }

    public String getRightViewImageLink() {
        return rightViewImageLink;
    }

    public void setRightViewImageLink(String rightViewImageLink) {
        this.rightViewImageLink = rightViewImageLink;
    }

    public String getLeftViewImageLink() {
        return leftViewImageLink;
    }

    public void setLeftViewImageLink(String leftViewImageLink) {
        this.leftViewImageLink = leftViewImageLink;
    }

    public String getTerraceViewImageLink() {
        return terraceViewImageLink;
    }

    public void setTerraceViewImageLink(String terraceViewImageLink) {
        this.terraceViewImageLink = terraceViewImageLink;
    }

    public String getInnerView1ImageLink() {
        return innerView1ImageLink;
    }

    public void setInnerView1ImageLink(String innerView1ImageLink) {
        this.innerView1ImageLink = innerView1ImageLink;
    }

    public String getInnerView2ImageLink() {
        return innerView2ImageLink;
    }

    public void setInnerView2ImageLink(String innerView2ImageLink) {
        this.innerView2ImageLink = innerView2ImageLink;
    }

    public String getInnerView3ImageLink() {
        return innerView3ImageLink;
    }

    public void setInnerView3ImageLink(String innerView3ImageLink) {
        this.innerView3ImageLink = innerView3ImageLink;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDocks() {
        return docks;
    }

    public void setDocks(String docks) {
        this.docks = docks;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getExtras() {
        return Extras;
    }

    public void setExtras(String extras) {
        Extras = extras;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
