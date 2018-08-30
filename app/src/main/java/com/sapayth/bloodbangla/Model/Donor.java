package com.sapayth.bloodbangla.Model;

public class Donor extends Person {
    private String bloodGroup;

    public Donor(String name, String personKeyId, String uId, String bloodGroup) {
        super(name, personKeyId, uId);
        this.bloodGroup = bloodGroup;
    }

    // empty constructor for firebase
    public Donor() {
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}