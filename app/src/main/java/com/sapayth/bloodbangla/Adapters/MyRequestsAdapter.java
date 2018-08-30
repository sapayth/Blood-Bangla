package com.sapayth.bloodbangla.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapayth.bloodbangla.Model.Seeker;
import com.sapayth.bloodbangla.MyRequestActivity;
import com.sapayth.bloodbangla.R;
import com.sapayth.bloodbangla.RequestBloodActivity;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.RequestViewHolder> {
    private Context context;
    private List<Seeker> requestList;

    public MyRequestsAdapter(Context context, List<Seeker> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seeker_list_item, parent, false);
        return new MyRequestsAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Seeker currentSeeker = requestList.get(position);

        holder.mBloodGroupTV.setText(currentSeeker.getBloodGroup());
        holder.mNameTv.setText(currentSeeker.getName());
        holder.mAddressTV.setText(currentSeeker.getAddress());
        holder.mDateTV.setText(currentSeeker.neededDateStr());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView mBloodGroupTV, mNameTv, mAddressTV, mDateTV;
        private ImageView mCallButton;


        public RequestViewHolder(final View itemView) {
            super(itemView);

            mBloodGroupTV = itemView.findViewById(R.id.bloodGroupTextView);
            mNameTv = itemView.findViewById(R.id.nameTextView);
            mAddressTV = itemView.findViewById(R.id.addressTextView);
            mDateTV = itemView.findViewById(R.id.dateTextView);
            mCallButton = itemView.findViewById(R.id.callButton);
            mCallButton.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(context, RequestBloodActivity.class);
                    intent.putExtra("Request", requestList.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
