package com.example.SeniorCitizenCare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterYogaClass extends RecyclerView.Adapter<MyAdapterYogaClass.MyViewHolder> {

    ArrayList<YogaClass> mList;

    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public MyAdapterYogaClass(ArrayList<YogaClass> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_yoga, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        YogaClass yoga = mList.get(position);

        holder.mYogaTitle.setText(yoga.getmName());
        holder.mYogaDescription.setText(yoga.getmDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mYogaTitle;
        TextView mYogaDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mYogaTitle = itemView.findViewById(R.id.yogaTitle);
            mYogaDescription = itemView.findViewById(R.id.yogaDescription);

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
}
