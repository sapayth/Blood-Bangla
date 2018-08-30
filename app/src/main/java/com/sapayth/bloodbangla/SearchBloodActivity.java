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
import com.sapayth.bloodbangla.Adapters.BloodSeekerAdapter;
import com.sapayth.bloodbangla.Model.Seeker;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchBloodActivity extends AppCompatActivity {

    private RecyclerView seekerRecyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressDialog mDialog;

    private List<Seeker> seekerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);

        if (isNetworkAvailable()) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Loading Data");
            mDialog.setCancelable(false);
            mDialog.setInverseBackgroundForced(false);
            mDialog.show();

            seekerRecyclerView = findViewById(R.id.itemRecyclerView);
            seekerRecyclerView.setHasFixedSize(true);
            seekerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            seekerList = new ArrayList<>();

            FirebaseUtils.seekingListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    seekerList.clear();
                    long todayUnix = System.currentTimeMillis() / 1000L;

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Seeker seeker = data.getValue(Seeker.class);
                        if((seeker.getBloodNeededDate() / 1000L) > todayUnix) {
                            seekerList.add(seeker);
                        }
                    }

                    Collections.sort(seekerList, new Comparator<Seeker>() {

                        @Override
                        public int compare(Seeker lhs, Seeker rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            return rhs.getBloodNeededDate() > lhs.getBloodNeededDate() ? -1 : (rhs.getBloodNeededDate() < lhs.getBloodNeededDate()) ? 1 : 0;
                        }
                    });

                    adapter = new BloodSeekerAdapter(SearchBloodActivity.this, seekerList);
                    seekerRecyclerView.setAdapter(adapter);
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
