package com.example.mybmicalculator;

public class model {
    private String id,user_name,sex,dates;
    private Double weight,height,bmi;
    private String date;

    public model(){

    }

    public model(String id, String user_name, String sex, Double weight, Double height, Double bmi, String date) {
        this.id = id;
        this.user_name = user_name;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.date = date;
    }

    public model(String user_name, String sex, Double weight, Double height, Double bmi, String date) {
        this.user_name = user_name;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
