package com.sapayth.bloodbangla;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Utils.Util;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);

        // get required firebase instances
        FirebaseUtils.firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUtils.firebaseUser = FirebaseUtils.firebaseAuth.getCurrentUser();

        // Firebase database referances
        FirebaseUtils.rootReferance = FirebaseDatabase.getInstance().getReference();
        FirebaseUtils.userListRef = FirebaseUtils.rootReferance.child(FirebaseUtils.TABLE_NAME_USER);
        FirebaseUtils.seekingListRef = FirebaseUtils.rootReferance.child(FirebaseUtils.TABLE_NAME_SEEKER);
        FirebaseUtils.donorListRef = Util.FirebaseUtils.rootReferance.child(Util.FirebaseUtils.TABLE_NAME_DONOR);

        if (FirebaseUtils.firebaseUser != null) {
            FirebaseUtils.userListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userId = FirebaseUtils.firebaseAuth.getCurrentUser().getUid();
                    for (DataSnapshot postData :  dataSnapshot.getChildren()) {
                        Person person = postData.getValue(Person.class);
                        if (person.getuId().equals(userId)) {
                            // set the userReferance as soon as user logged in
                            FirebaseUtils.userReferance = FirebaseUtils.userListRef.child(person.getPersonKeyId());
                            FirebaseUtils.currentPerson = person;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (!isNetworkAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        }
    }

    public void openDonate(View view) {
        startActivity(new Intent(MainActivity.this, DonateBloodActivity.class));
    }

    public void openSearch(View view) {
        startActivity(new Intent(MainActivity.this, SearchBloodActivity.class));
    }

    public void openRequest(View view) {
        startActivity(new Intent(MainActivity.this, RequestBloodActivity.class));
    }

    public void openAbout(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    private void logoutUser() {
        if (FirebaseUtils.firebaseUser != null) {
            Toast.makeText(this, FirebaseUtils.firebaseUser.getEmail() + " Logout", Toast.LENGTH_SHORT).show();
            FirebaseUtils.firebaseAuth.signOut();
        } else {
            Toast.makeText(this, "You aren't login Yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            menu.findItem(R.id.item_profile).setVisible(true);
            menu.findItem(R.id.item_logout).setVisible(true);
            menu.findItem(R.id.item_login).setVisible(false);
            menu.findItem(R.id.item_registration).setVisible(false);
        } else {
            menu.findItem(R.id.item_profile).setVisible(false);
            menu.findItem(R.id.item_logout).setVisible(false);
            menu.findItem(R.id.item_login).setVisible(true);
            menu.findItem(R.id.item_registration).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.item_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.item_registration:
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                break;
            case R.id.item_profile:
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                break;
            case R.id.item_logout:
                logoutUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}