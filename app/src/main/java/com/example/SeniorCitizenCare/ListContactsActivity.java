package com.example.SeniorCitizenCare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ListContactsActivity extends AppCompatActivity {

    public RecyclerView recyclerContactsView;
    public ArrayList<ContactClassSmall> mList,temp,listFull;
    public MyAdapterContactSmallClass adapterContactSmallClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emergency_contacts);

        populateList();
        buildRecyclerView();

    }

    public void populateList(){
        mList = new ArrayList<>();
        listFull = new ArrayList<>();

        mList.add(new ContactClassSmall(R.drawable.ic_person, "Satan", "666"));
//        temp = getContacts();
//
//        for(int j=0; j<temp.size(); j++){
//            list.add(new ContactClass((R.drawable.ic_person), temp.get(j).mName, temp.get(j).mNumber,null));
//        }

        listFull = new ArrayList<>(mList);
    }

    public void buildRecyclerView(){
        recyclerContactsView = (RecyclerView) findViewById(R.id.contactsRecyclerView);
        recyclerContactsView.setLayoutManager(new LinearLayoutManager(this));

        adapterContactSmallClass = new MyAdapterContactSmallClass(mList);
        recyclerContactsView.setAdapter(adapterContactSmallClass);

//        adapterContactSmallClass.setOnItemClickListener(new MyAdapterContactClass.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                currPos = position;
//
//                ContactRelationDialog dialog = new ContactRelationDialog();
//                dialog.show(getSupportFragmentManager(), "dialog");
//            }
//        });
    }
}
