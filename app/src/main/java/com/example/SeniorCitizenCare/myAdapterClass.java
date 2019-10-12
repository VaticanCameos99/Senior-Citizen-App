package com.example.SeniorCitizenCare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myAdapterClass extends RecyclerView.Adapter <myAdapterClass.MyViewHolder>{

    ArrayList<Medicine> mList;
    OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }




    public myAdapterClass(ArrayList<Medicine> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medicine medicine = mList.get(position);

        holder.MedicineName.setText(medicine.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView MedicineName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MedicineName = itemView.findViewById(R.id.MedicineName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
