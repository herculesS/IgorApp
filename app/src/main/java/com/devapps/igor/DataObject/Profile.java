package com.devapps.igor.DataObject;

import com.devapps.igor.Util.Utils;

import java.io.Serializable;

public class Profile implements Serializable {

    private String id;
    private String name;
    private String email;
    private Utils.Sex sex;
    private int birthDay;
    private int birthMonth;
    private int birthYear;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utils.Sex getSex() {
        return sex;
    }

    public void setSex(Utils.Sex sex) {
        this.sex = sex;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
