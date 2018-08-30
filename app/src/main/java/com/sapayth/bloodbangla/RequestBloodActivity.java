package com.sapayth.bloodbangla;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.sapayth.bloodbangla.Model.Seeker;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestBloodActivity extends AppCompatActivity {

    private EditText mNameET, mAddressET, mContactET, mSecondaryContactET, mCommentET;
    private TextView mDateTextView, mNotLoggedInTV;
    private Spinner mBloodTypeSpinner;
    private ScrollView mRequestBloodSV;
    private Button mRequestButton;

    private Calendar mCalender = Calendar.getInstance();
    private int mDate = mCalender.get(Calendar.DATE);
    private int mMonth = mCalender.get(Calendar.MONTH);
    private int mYear = mCalender.get(Calendar.YEAR);

    private long selectedUnixDate;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private Seeker mCurrentSeeker;

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        FirebaseUtils.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String dayStr = mDate + "/" + (mMonth + 1) + "/" + mYear;

        Date date;
        try {
            date = dateFormatter.parse(dayStr);
            selectedUnixDate = date.getTime();
        } catch (ParseException ex) {
            Toast.makeText(RequestBloodActivity.this, "Date invalid", Toast.LENGTH_SHORT).show();
        }

        mNotLoggedInTV = findViewById(R.id.notLoggedInTextView);
        mRequestBloodSV = findViewById(R.id.requestBloodScrollView);
        mNameET = findViewById(R.id.nameEditText);
        mAddressET = findViewById(R.id.addressEditText);
        mContactET = findViewById(R.id.contactNumberEditText);
        mSecondaryContactET = findViewById(R.id.secondaryContactNoEditText);
        mBloodTypeSpinner = findViewById(R.id.bloodTypeNeededSpinner);
        mDateTextView = findViewById(R.id.dateTextView);
        mCommentET = findViewById(R.id.commentEditText);
        mRequestButton = findViewById(R.id.requestBloodButton);

        if (FirebaseUtils.firebaseUser == null) {
            mNotLoggedInTV.setVisibility(View.VISIBLE);
            mRequestBloodSV.setVisibility(View.GONE);
        } else {
            mNotLoggedInTV.setVisibility(View.GONE);
            mRequestBloodSV.setVisibility(View.VISIBLE);
        }

        mIntent = getIntent();
        if (mIntent != null) {
            // set the calender as blood needed date
            mCurrentSeeker = (Seeker) mIntent.getSerializableExtra("Request");

            if(mCurrentSeeker != null) {
                selectedUnixDate = mCurrentSeeker.getBloodNeededDate();
                mCalender.setTimeInMillis(mCurrentSeeker.getBloodNeededDate());
                mDate = mCalender.get(Calendar.DATE);
                mMonth = mCalender.get(Calendar.MONTH);
                mYear = mCalender.get(Calendar.YEAR);
                mNameET.setText(mCurrentSeeker.getName());
                mAddressET.setText(mCurrentSeeker.getAddress());
                mContactET.setText(mCurrentSeeker.getContactNo());
                mSecondaryContactET.setText(mCurrentSeeker.getSecondaryContactNo());
                mBloodTypeSpinner.setSelection(getBloodGroupIndex());
                mDateTextView.setText(mCurrentSeeker.neededDateStr());
                mCommentET.setText(mCurrentSeeker.getComment());
                mRequestButton.setText(R.string.edit_request);
            }
        }

        mDateTextView.setText(dayStr);
        mRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForBlood();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIntent != null) {
            getMenuInflater().inflate(R.menu.edit_request_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentSeeker == null) {
            MenuItem deleteItem = menu.findItem(R.id.item_delete);
            deleteItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save :
                requestForBlood();
                break;
            case R.id.item_delete :
                deleteRequest();
                break;
        }

        return true;
    }

    private void deleteRequest() {
        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this);
        confirmDeleteDialog.setTitle("Delete request");
        confirmDeleteDialog.setIcon(R.drawable.warning_icon);
        confirmDeleteDialog.setMessage("Do you want to delete this request??");
        confirmDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUtils.seekingListRef.child(mCurrentSeeker.getKeyId()).removeValue();
                FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_SEEKING_LIST_KEY_ID).removeValue().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("delete child exception", e.getMessage());
                    }
                });
                Toast.makeText(RequestBloodActivity.this, "Request Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RequestBloodActivity.this, UserProfileActivity.class));
                finish();
            }
        });
        confirmDeleteDialog.setNeutralButton("Cancel",null);
        confirmDeleteDialog.setNegativeButton("No",null);
        confirmDeleteDialog.show();

    }

    public int getBloodGroupIndex() {
        int index = 0;
        if (mCurrentSeeker != null) {
            String bloodGroupStr = mCurrentSeeker.getBloodGroup();
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

    private void requestForBlood() {
        String name = mNameET.getText().toString();
        String bloodGroup = mBloodTypeSpinner.getSelectedItem().toString();
        String address = mAddressET.getText().toString();
        String contactNo = mContactET.getText().toString();
        String secondaryContactNo = mSecondaryContactET.getText().toString();
        String comment = mCommentET.getText().toString();

        if (TextUtils.isEmpty(name)) {
            // name is empty
            mNameET.setError("Please enter name");
            mNameET.requestFocus();
            // stopping function execution
            return;
        }

        if (TextUtils.isEmpty(address)) {
            // address is empty
            mAddressET.setError("Please enter address");
            mAddressET.requestFocus();
            // stopping function execution
            return;
        }

        if (TextUtils.isEmpty(contactNo)) {
            // address is empty
            mContactET.setError("Please enter contact No");
            mContactET.requestFocus();
            // stopping function execution
            return;
        }

        // if this user already have a seeking
        if (mCurrentSeeker != null && mCurrentSeeker.getKeyId() != null) {
            // get the keyId
            String keyId = mCurrentSeeker.getKeyId();
            // remove the previous seeking reference
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_SEEKING_LIST_KEY_ID).removeValue();
            // add the new seeking reference into users class
            FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_SEEKING_LIST_KEY_ID).setValue(keyId);

            DatabaseReference seekRef = FirebaseUtils.seekingListRef.child(mCurrentSeeker.getKeyId());    // previous seeking reference

            // replace new data into the reference
            seekRef.child(FirebaseUtils.SEEKER_COL_ADDRESS).setValue(address);
            seekRef.child(FirebaseUtils.SEEKER_COL_BLOOD_GROUP).setValue(bloodGroup);
            seekRef.child(FirebaseUtils.SEEKER_COL_BLOOD_NEEDED_DATE).setValue(selectedUnixDate);
            seekRef.child(FirebaseUtils.SEEKER_COL_COMMENT).setValue(comment);
            seekRef.child(FirebaseUtils.SEEKER_COL_CONTACT_NO).setValue(contactNo);
            seekRef.child(FirebaseUtils.SEEKER_COL_KEY_ID).setValue(keyId);
            seekRef.child(FirebaseUtils.SEEKER_COL_NAME).setValue(name);
            seekRef.child(FirebaseUtils.SEEKER_COL_SECOND_CONTACT_NO)
                    .setValue(secondaryContactNo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(RequestBloodActivity.this, SearchBloodActivity.class));
                            Toast.makeText(RequestBloodActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


        } else {
            // get the keyId
            final String keyId = FirebaseUtils.seekingListRef.push().getKey();
            // create a new seeker
            Seeker seeker = new Seeker(name, selectedUnixDate, bloodGroup, address, contactNo, secondaryContactNo);
            seeker.setComment(comment);
            seeker.setKeyId(keyId);

            // add the seeker into seekingList database reference
            FirebaseUtils.seekingListRef.child(keyId).setValue(seeker)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (FirebaseUtils.userReferance != null) {
                                FirebaseUtils.userReferance.child(FirebaseUtils.PERSON_COL_SEEKING_LIST_KEY_ID).setValue(keyId);
                                Toast.makeText(RequestBloodActivity.this, "Request Added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RequestBloodActivity.this, SearchBloodActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RequestBloodActivity.this, "Failed to add Request", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void selectDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dayStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                        mDateTextView.setText(dayStr);
                        Date date;
                        try {
                            date = dateFormatter.parse(dayStr);
                            selectedUnixDate = date.getTime();
                        } catch (ParseException ex) {
                            Toast.makeText(RequestBloodActivity.this, "Date invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                mYear, mMonth, mDate);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}