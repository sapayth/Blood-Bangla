package com.sapayth.bloodbangla.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Person implements Serializable {
    private String name;
    private String location;
    private String contactNo;
    private String secondaryContactNo;
    private String bloodGroup;
    private String gender;
    private long dateOfBirth;
    private boolean availableToDonate;
    private String uId;
    private String personKeyId;
    private String donorListKeyId;
    private String seekingListKeyId;

    // empty constructor for firebase
    public Person() {
    }

    public String getSeekingListKeyId() {
        return seekingListKeyId;
    }

    public void setSeekingListKeyId(String seekingListKeyId) {
        this.seekingListKeyId = seekingListKeyId;
    }

    public Person(String name, String personKeyId, String uId) {
        this.name = name;
        this.personKeyId = personKeyId;
        this.uId = uId;

        this.location = "Unspecified";
        this.contactNo = "Unspecified";
        this.secondaryContactNo = "Unspecified";
        this.bloodGroup = "Unspecified";
        this.gender =  "Unspecified";
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonKeyId() {
        return personKeyId;
    }

    public void setPersonKeyId(String personKeyId) {
        this.personKeyId = personKeyId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }


    public boolean isAvailableToDonate() {
        return availableToDonate;
    }

    public void setAvailableToDonate(boolean availableToDonate) {
        this.availableToDonate = availableToDonate;
    }

    public String getDateOfBirthStr() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = new Date(dateOfBirth);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));
        return dateFormat.format(date);
    }

    public String getDonorListKeyId() {
        return donorListKeyId;
    }

    public void setDonorListKeyId(String donorListKeyId) {
        this.donorListKeyId = donorListKeyId;
    }

    public String getStringAge() {
        if (dateOfBirth == 0) {
            return "Unspecified";
        } else {
            int years = 0;
            int months = 0;
            int days = 0;
            //create calendar object for birth day
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(dateOfBirth);
            //create calendar object for current day
            long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);

            //Get difference between years
            years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            int currMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthDay.get(Calendar.MONTH) + 1;
            //Get difference between months
            months = currMonth - birthMonth;
            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0) {
                years--;
                months = 12 - birthMonth + currMonth;
                if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                    months--;
            } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                years--;
                months = 11;
            }
            //Calculate the days
            if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
                days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
            else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            } else {
                days = 0;
                if (months == 12)
                {
                    years++;
                    months = 0;
                }
            }
            //Create new Age object
            return years + " Years, " + months + " Months, " + days + " Days";
        }
    }
}