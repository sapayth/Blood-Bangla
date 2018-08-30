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

import com.sapayth.bloodbangla.Model.Seeker;
import com.sapayth.bloodbangla.R;

import java.util.List;

public class BloodSeekerAdapter extends RecyclerView.Adapter<BloodSeekerAdapter.SeekerViewHolder> {

    private Context context;
    private List<Seeker> seekerList;

    public BloodSeekerAdapter(Context context, List<Seeker> seekerList) {
        this.context = context;
        this.seekerList = seekerList;
    }

    @NonNull
    @Override
    public SeekerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seeker_list_item, parent, false);
        return new SeekerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekerViewHolder holder, int position) {
        Seeker currentSeeker = seekerList.get(position);

        final String contactNo = currentSeeker.getContactNo();

        holder.mBloodGroupTV.setText(currentSeeker.getBloodGroup());
        holder.mNameTv.setText(currentSeeker.getName());
        holder.mAddressTV.setText(currentSeeker.getAddress());
        holder.mDateTV.setText(currentSeeker.neededDateStr());

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
        return seekerList.size();
    }

    public class SeekerViewHolder extends RecyclerView.ViewHolder {
        private TextView mBloodGroupTV, mNameTv, mAddressTV, mDateTV;
        private ImageView mCallButtonIV;

        public SeekerViewHolder(View itemView) {
            super(itemView);

            mBloodGroupTV = itemView.findViewById(R.id.bloodGroupTextView);
            mNameTv = itemView.findViewById(R.id.nameTextView);
            mAddressTV = itemView.findViewById(R.id.addressTextView);
            mDateTV = itemView.findViewById(R.id.dateTextView);
            mCallButtonIV = itemView.findViewById(R.id.callButton);
        }
    }
}
