package com.example.SeniorCitizenCare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListContacts extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ContactClass> list,temp;
    MyAdapterContactClass adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emergency_contacts);

        fab = findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.emergencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.hide();

        list = new ArrayList<>();


        list.add(new ContactClass((R.drawable.ic_person), "Dwight Schrute", "9999", null));

        adapter = new MyAdapterContactClass(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyAdapterContactClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getBaseContext() ,list.get(position).mName, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("contactImage", list.get(position).mImageResource);
                resultIntent.putExtra("contactName", list.get(position).mName);
                resultIntent.putExtra("contactNumber", list.get(position).mNumber);
                resultIntent.putExtra("contactRelation", list.get(position).mRelation);


                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}
