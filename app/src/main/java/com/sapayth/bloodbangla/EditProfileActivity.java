package com.sapayth.bloodbangla;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.sapayth.bloodbangla.Model.Donor;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    private EditText mNameET, mAddressET, mContactNoET, mSecondaryContactET;
    private TextView mDateOfBirthTextView;
    private Spinner mBloodGroupSpinner, mGenderSpinner;
    private Switch mAvailableForDonateSwitch;
    private ProgressDialog mDialog;

    private Person currentPerson = null;

    private Calendar mCalender = Calendar.getInstance();
    private int mDate = mCalender.get(Calendar.DATE);
    private int mMonth = mCalender.get(Calendar.MONTH);
    private int mYear = mCalender.get(Calendar.YEAR);

    private long selectedUnixDate;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading Data");
        mDialog.setCancelable(false);
        mDialog.setInverseBackgroundForced(false);
        mDialog.show();

        mNameET = findViewById(R.id.nameEditText);
        mAddressET = findViewById(R.id.addressEditText);
        mContactNoET = findViewById(R.id.contactNumberEditText);
        mSecondaryContactET = findViewById(R.id.secondaryContactNoEditText);
        mBloodGroupSpinner = findViewById(R.id.bloodTypeSpinner);
        mGenderSpinner = findViewById(R.id.genderSpinner);
        mAvailableForDonateSwitch = findViewById(R.id.availableForDonateSwitch);
        mDateOfBirthTextView = findViewById(R.id.dateTextView);

        Intent intent = getIntent();
        if (intent != null) {
            currentPerson = (Person) intent.getSerializableExtra("Person");

            mNameET.setText(currentPerson.getName());
            mDateOfBirthTextView.setText(currentPerson.getDateOfBirthStr());

            if(currentPerson.getLocation().equals("Unspecified")) {
                mAddressET.setText("");
            } else {
                mAddressET.setText(currentPerson.getLocation());
            }
            if(currentPerson.getContactNo().equals("Unspecified")) {
                mContactNoET.setText("");
            } else {
                mContactNoET.setText(currentPerson.getContactNo());
            }
            if(currentPerson.getSecondaryContactNo().equals("Unspecified")) {
                mSecondaryContactET.setText("");
            } else {
                mSecondaryContactET.setText(currentPerson.getSecondaryContactNo());
            }

            if (currentPerson.getDateOfBirth() != 0) {
                // set the calender as users date of birth
                mCalender.setTimeInMillis(currentPerson.getDateOfBirth());
                mDate = mCalender.get(Calendar.DATE);
                mMonth = mCalender.get(Calendar.MONTH);
                mYear = mCalender.get(Calendar.YEAR);
                selectedUnixDate = currentPerson.getDateOfBirth();
            }

            mBloodGroupSpinner.setSelection(getBloodGroupIndex());
            mGenderSpinner.setSelection(getGenderIndex());

            mDialog.dismiss();
        }

        mAvailableForDonateSwitch.setChecked(currentPerson.isAvailableToDonate());

        if (FirebaseUtils.firebaseUser != null) {
            FirebaseUtils.rootReferance = FirebaseDatabase.getInstance().getReference();
            FirebaseUtils.userListRef = FirebaseUtils.rootReferance.child(FirebaseUtils.TABLE_NAME_USER);
            FirebaseUtils.userReferance = FirebaseUtils.userListRef.child(currentPerson.getPersonKeyId());
        }
    }

    public void updateInfo(View view) {
        if (FirebaseUtils.firebaseUser != null){
            String name = mNameET.getText().toString().trim();
            String location = mAddressET.getText().toString().trim();
            String contactNo = mContactNoET.getText().toString().trim();
            String secondaryContactNo = mSecondaryContactET.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                mNameET.setError("Name Required");
                mNameET.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(location)) {
                mAddressET.setError("Location Required");
                mAddressET.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(contactNo)) {
                mContactNoET.setError("Contact No Required");
                mContactNoET.requestFocus();
                return;
            }

            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_NAME).setValue(name);
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COl_BLOOD_GROUP).setValue(mBloodGroupSpinner.getSelectedItem());
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_GENDER).setValue(mGenderSpinner.getSelectedItem());
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_DOB).setValue(selectedUnixDate);
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_LOCATION).setValue(location);
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_CONTACT).setValue(contactNo);
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_SECONDARY_CONTACT).setValue(secondaryContactNo);

            if (mAvailableForDonateSwitch.isChecked()) {
                // change and update the available status on firebase
                FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_AVAILABLE).setValue(true);

                String keyId = FirebaseUtils.donorListRef.push().getKey();

                // save the donorList KeyId into person class. so that we can easily detect donor info related to this person
                FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_DONOR_LIST_KEY_ID).setValue(keyId);

                // make the donor object
                Donor donor = new Donor(name, currentPerson.getPersonKeyId(), currentPerson.getuId(), mBloodGroupSpinner.getSelectedItem().toString());
                donor.setContactNo(contactNo);
                donor.setSecondaryContactNo(secondaryContactNo);
                donor.setLocation(location);
                donor.setAvailableToDonate(true);
                donor.setDateOfBirth(selectedUnixDate);
                donor.setGender(mGenderSpinner.getSelectedItem().toString());

                // add as a donor in donorList
                FirebaseUtils.donorListRef.child(keyId).setValue(donor);
            } else {
                // change and update the available status on firebase
                FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_AVAILABLE).setValue(false);

                if (currentPerson.getDonorListKeyId() != null) {
                    FirebaseUtils.donorListRef.child(currentPerson.getDonorListKeyId()).removeValue();
                    FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_DONOR_LIST_KEY_ID).setValue(null);
                }
            }

            startActivity(new Intent(this, UserProfileActivity.class));
            finish();
        }
    }

    public void selectDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                        mDateOfBirthTextView.setText(dayStr);
                        Date date;
                        try {
                            date = dateFormatter.parse(dayStr);
                            selectedUnixDate = date.getTime();
                        } catch (ParseException ex) {
                            Toast.makeText(EditProfileActivity.this, "Date invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                mYear, mMonth, mDate);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private int getGenderIndex() {
        int index = 0;
        if (currentPerson != null) {
            String gender = currentPerson.getGender();
            if (gender.equals("Male")) {
                index = 0;
            } else if (gender.equals("Female")) {
                index = 1;
            } else {
                index = 2;
            }
        }
        return index;
    }

    public int getBloodGroupIndex() {
        int index = 0;
        if (currentPerson != null) {
            String bloodGroupStr = currentPerson.getBloodGroup();
            if (bloodGroupStr.equals("AB-")) {
                index = 0;
            } else if (bloodGroupStr.equals("AB+")) {
                index = 1;
            } else if (bloodGroupStr.equals("A-")) {
                index = 2;
            } else if (bloodGroupStr.equals("A+")) {
                index = 3;
            } else if (bloodGroupStr.equals("B-")) {
                index = 4;
            } else if (bloodGroupStr.equals("B+")) {
                index = 5;
            } else if (bloodGroupStr.equals("O-")) {
                index = 6;
            } else if (bloodGroupStr.equals("O+")) {
                index = 7;
            }
        } else {
            index = 0;
        }

        return index;
    }
}