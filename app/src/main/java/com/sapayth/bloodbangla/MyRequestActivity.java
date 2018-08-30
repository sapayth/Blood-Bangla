package com.sapayth.bloodbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sapayth.bloodbangla.Adapters.MyRequestsAdapter;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Model.Seeker;
import com.sapayth.bloodbangla.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MyRequestActivity extends AppCompatActivity {
    private RecyclerView myRequestRecyclerView;
    private RecyclerView.Adapter adapter;

    private ProgressDialog mDialog;

    private Person mCurrentPerson = null;
    private List<Seeker> mMyRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading Data");
        mDialog.setCancelable(false);
        mDialog.setInverseBackgroundForced(false);
        mDialog.show();

        myRequestRecyclerView = findViewById(R.id.itemRecyclerView);
        myRequestRecyclerView.setHasFixedSize(true);
        myRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyRequestList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentPerson = (Person) intent.getSerializableExtra("Person");

            if (mCurrentPerson.getSeekingListKeyId() != null) {
                DatabaseReference myReqRef = Util.FirebaseUtils.seekingListRef.child(mCurrentPerson.getSeekingListKeyId());
                myReqRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Seeker seeker = dataSnapshot.getValue(Seeker.class);
                        mMyRequestList.add(seeker);

                        adapter = new MyRequestsAdapter(MyRequestActivity.this, mMyRequestList);
                        myRequestRecyclerView.setAdapter(adapter);
                        mDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}