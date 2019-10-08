package com.example.SeniorCitizenCare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListContacts extends AppCompatActivity implements ContactRelationDialog.DialogListener {

    RecyclerView recyclerView;

    ArrayList<ContactClass> list,temp;
    MyAdapterContactClass adapter;
    FloatingActionButton fab;
    int currPos=-1;

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
                currPos = position;

                ContactRelationDialog dialog = new ContactRelationDialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

    }

    @Override
    public void onClick(String relation) {
        Log.i("Relation", relation + " established");
        Log.i("Pos", Integer.toString(currPos));
        Intent resultIntent = new Intent();
        resultIntent.putExtra("contactImage", list.get(currPos).mImageResource);
        resultIntent.putExtra("contactName", list.get(currPos).mName);
        resultIntent.putExtra("contactNumber", list.get(currPos).mNumber);
        resultIntent.putExtra("contactRelation", relation);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
