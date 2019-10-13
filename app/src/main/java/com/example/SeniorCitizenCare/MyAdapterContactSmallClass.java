package com.example.SeniorCitizenCare;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterContactSmallClass extends RecyclerView.Adapter<MyAdapterContactSmallClass.MyViewHolder> {

    ArrayList<ContactClassSmall> mList;
    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public MyAdapterContactSmallClass(ArrayList<ContactClassSmall> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContactClassSmall contact = mList.get(position);

        holder.mContactImageView.setImageResource(contact.getmImageResource());
        holder.mContactName.setText(contact.getmName());
        holder.mContactNumber.setText(contact.getmNumber());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mContactImageView;
        TextView mContactName;
        TextView mContactNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mContactImageView = itemView.findViewById(R.id.imageContact);
            mContactName = itemView.findViewById(R.id.textContactName);
            mContactNumber = itemView.findViewById(R.id.textContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void filterList(ArrayList<ContactClassSmall> filteredList){
        mList = filteredList;
        notifyDataSetChanged();
    }
}
