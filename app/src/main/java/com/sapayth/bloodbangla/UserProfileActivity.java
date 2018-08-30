package com.sapayth.bloodbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

public class UserProfileActivity extends AppCompatActivity {

    private TextView nameTV, emailTV, bloodGroupTV, ageTV, locationTV, genderTV, contactNoTV, secondContactNoTV;
    private ProgressDialog mDialog;
    private Button mRequestPageButton;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading");
        mDialog.setCancelable(false);
        mDialog.setInverseBackgroundForced(false);
        mDialog.show();

        nameTV = findViewById(R.id.nameTextView);
        emailTV = findViewById(R.id.emailTextView);
        bloodGroupTV = findViewById(R.id.bloodGroupTextView);
        ageTV = findViewById(R.id.ageTextView);
        locationTV = findViewById(R.id.locationTextView);
        genderTV = findViewById(R.id.genderTextView);
        contactNoTV = findViewById(R.id.contactNoTextView);
        secondContactNoTV = findViewById(R.id.secondaryContactNoTextView);
        mRequestPageButton = findViewById(R.id.requestPageButton);

        FirebaseUtils.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (FirebaseUtils.firebaseUser != null){
            mUserId = FirebaseUtils.firebaseUser.getUid();

            FirebaseUtils.userListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showUserInfo(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else if (FirebaseUtils.currentPerson != null) {
            showUserInfo();
        } else {
            Toast.makeText(this, "User not found. Please log in first", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }

    private void showUserInfo() {
        if (FirebaseUtils.currentPerson != null) {
            nameTV.setText(FirebaseUtils.currentPerson.getName());
            emailTV.setText(FirebaseUtils.firebaseUser.getEmail());
            bloodGroupTV.setText(FirebaseUtils.currentPerson.getBloodGroup());
            ageTV.setText(FirebaseUtils.currentPerson.getStringAge());
            locationTV.setText(FirebaseUtils.currentPerson.getLocation());
            genderTV.setText(FirebaseUtils.currentPerson.getGender());
            contactNoTV.setText(FirebaseUtils.currentPerson.getContactNo());
            secondContactNoTV.setText(FirebaseUtils.currentPerson.getSecondaryContactNo());

            if (FirebaseUtils.currentPerson.getSeekingListKeyId() == null) {
                mRequestPageButton.setVisibility(View.GONE);
            } else {
                mRequestPageButton.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(UserProfileActivity.this,
                    "User not found. Please log in first",
                    Toast.LENGTH_SHORT).show();
        }
        mDialog.dismiss();
    }

    private void showUserInfo(DataSnapshot dataSnapshot) {
        for (DataSnapshot postData : dataSnapshot.getChildren()) {
            Person person = postData.getValue(Person.class);
            if (person.getuId().equals(mUserId)) {
                FirebaseUtils.userReferance = FirebaseUtils.userListRef.child(person.getPersonKeyId());
                FirebaseUtils.currentPerson = person;
            }
        }
        if (FirebaseUtils.currentPerson != null) {
            nameTV.setText(FirebaseUtils.currentPerson.getName());
            emailTV.setText(FirebaseUtils.firebaseUser.getEmail());
            bloodGroupTV.setText(FirebaseUtils.currentPerson.getBloodGroup());
            ageTV.setText(FirebaseUtils.currentPerson.getStringAge());
            locationTV.setText(FirebaseUtils.currentPerson.getLocation());
            genderTV.setText(FirebaseUtils.currentPerson.getGender());
            contactNoTV.setText(FirebaseUtils.currentPerson.getContactNo());
            secondContactNoTV.setText(FirebaseUtils.currentPerson.getSecondaryContactNo());

            if (FirebaseUtils.currentPerson.getSeekingListKeyId() == null) {
                mRequestPageButton.setVisibility(View.GONE);
            } else {
                mRequestPageButton.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(UserProfileActivity.this,
                    "User not found. Please log in first",
                    Toast.LENGTH_SHORT).show();
        }
        mDialog.dismiss();
    }

    public void openUpdatePage(View view) {
        Intent updateIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
        updateIntent.putExtra("Person", FirebaseUtils.currentPerson);
        startActivity(updateIntent);
    }

    public void openMyRequestPage(View view) {
        Intent myRequestIntent = new Intent(UserProfileActivity.this, MyRequestActivity.class);
        myRequestIntent.putExtra("Person", FirebaseUtils.currentPerson);
        startActivity(myRequestIntent);
    }
}