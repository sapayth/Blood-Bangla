package com.sapayth.bloodbangla;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sapayth.bloodbangla.Adapters.BloodDonorAdapter;
import com.sapayth.bloodbangla.Model.Donor;
import com.sapayth.bloodbangla.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DonateBloodActivity extends AppCompatActivity {

    private RecyclerView mDonateRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Donor> mDonorList;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);

        if (isNetworkAvailable()) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Loading Data");
            mDialog.setCancelable(false);
            mDialog.setInverseBackgroundForced(false);
            mDialog.show();

            mDonateRecyclerView = findViewById(R.id.itemRecyclerView);
            mDonateRecyclerView.setHasFixedSize(true);
            mDonateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mDonorList = new ArrayList<>();

            Util.FirebaseUtils.donorListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDonorList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Donor donor = data.getValue(Donor.class);
                        mDonorList.add(donor);
                    }

                    mAdapter = new BloodDonorAdapter(DonateBloodActivity.this, mDonorList);
                    mDonateRecyclerView.setAdapter(mAdapter);
                    mDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Turn on internet to view list!", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
