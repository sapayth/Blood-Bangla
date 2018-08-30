package com.sapayth.bloodbangla.Model;

import android.widget.Toast;

import com.sapayth.bloodbangla.RequestBloodActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Seeker implements Serializable {
    private String name;
    private long bloodNeededDate;
    private String bloodGroup;
    private String address;
    private String contactNo;
    private String secondaryContactNo;
    private String keyId;
    private String comment;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public String neededDateStr() {
        Date date = new Date(bloodNeededDate);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));
        return dateFormatter.format(date);
    }

    // empty constructor for firebase
    public Seeker() {}

    public Seeker(String name, long bloodNeededDate, String bloodGroup, String address, String contactNo, String secondaryContactNo) {
        this.name = name;
        this.bloodNeededDate = bloodNeededDate;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.contactNo = contactNo;
        this.secondaryContactNo = secondaryContactNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBloodNeededDate() {
        return bloodNeededDate;
    }

    public void setBloodNeededDate(long bloodNeededDate) {
        this.bloodNeededDate = bloodNeededDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getSecondaryContactNo() {
        return secondaryContactNo;
    }

    public void setSecondaryContactNo(String secondaryContactNo) {
        this.secondaryContactNo = secondaryContactNo;
    }
}
