package com.sapayth.bloodbangla.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sapayth.bloodbangla.Model.Donor;
import com.sapayth.bloodbangla.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.DonorViewHolder> {

    private Context context;
    private List<Donor> donorList;

    public BloodDonorAdapter(Context context, List<Donor> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_list_item, parent, false);

        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        Donor currentDonor = donorList.get(position);

        final String contactNo = currentDonor.getContactNo();

        holder.mBloodGroupTV.setText(currentDonor.getBloodGroup());
        holder.mNameTV.setText(currentDonor.getName());
        holder.mAddressTV.setText(currentDonor.getLocation());

        holder.mCallButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactNo));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "You need to allow this app to call the phone number", Toast.LENGTH_SHORT).show();
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public class DonorViewHolder extends RecyclerView.ViewHolder {

        private TextView mBloodGroupTV, mNameTV, mAddressTV;
        private ImageView mCallButtonIV;

        public DonorViewHolder(View itemView) {
            super(itemView);

            mBloodGroupTV = itemView.findViewById(R.id.bloodGroupTextView);
            mNameTV = itemView.findViewById(R.id.nameTextView);
            mAddressTV = itemView.findViewById(R.id.addressTextView);
            mCallButtonIV = itemView.findViewById(R.id.callButton);
        }
    }
}
