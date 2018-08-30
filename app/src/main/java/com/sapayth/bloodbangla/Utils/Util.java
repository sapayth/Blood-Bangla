package com.sapayth.bloodbangla.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.sapayth.bloodbangla.Model.Donor;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Model.Seeker;

public class Util {
    public static class FirebaseUtils {
        // FirebaseUtils
        public static DatabaseReference rootReferance;
        public static DatabaseReference userListRef;
        public static DatabaseReference userReferance;
        public static DatabaseReference donorReferance;
        public static DatabaseReference seekingListRef;
        public static DatabaseReference donorListRef;
        public static FirebaseAuth firebaseAuth;
        public static FirebaseUser firebaseUser;
        public static Person currentPerson;
        public static Seeker currentSeeker;
        public static Donor currentDonor;

        public static final String TABLE_NAME_USER = "BloodUserList";
        public static final String TABLE_NAME_SEEKER = "SeekingList";
        public static final String TABLE_NAME_DONOR = "BloodDonorList";

        // user node child name
        public static final String PERSON_COL_DOB = "dateOfBirth";
        public static final String PERSON_COL_GENDER = "gender";
        public static final String PERSON_COL_NAME = "name";
        public static final String PERSON_COl_BLOOD_GROUP = "bloodGroup";
        public static final String PERSON_COL_LOCATION = "location";
        public static final String PERSON_COL_CONTACT = "contactNo";
        public static final String PERSON_COL_SECONDARY_CONTACT = "secondaryContactNo";
        public static final String PERSON_COL_AVAILABLE = "availableToDonate";
        public static final String PERSON_COL_DONOR_LIST_KEY_ID = "donorListKeyId";
        public static final String PERSON_COL_SEEKING_LIST_KEY_ID = "seekingListKeyId";

        // seeker node child names
        public static final String SEEKER_COL_NAME = "name";
        public static final String SEEKER_COL_BLOOD_NEEDED_DATE = "bloodNeededDate";
        public static final String SEEKER_COL_BLOOD_GROUP = "bloodGroup";
        public static final String SEEKER_COL_ADDRESS = "address";
        public static final String SEEKER_COL_CONTACT_NO = "contactNo";
        public static final String SEEKER_COL_SECOND_CONTACT_NO = "secondaryContactNo:";
        public static final String SEEKER_COL_KEY_ID = "keyId";
        public static final String SEEKER_COL_COMMENT = "comment";
    }
}

