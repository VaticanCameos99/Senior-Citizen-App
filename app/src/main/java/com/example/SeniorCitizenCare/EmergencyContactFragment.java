package com.example.SeniorCitizenCare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmergencyContactFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ContactClass> list;
    MyAdapterContactClass adapter;

    private EditText editText;
    FloatingActionButton fab;

    private String emailId;
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);

        //Google Account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        emailId = acct.getEmail();

        //Hide the Search bar
        editText = v.findViewById(R.id.mySearch);
        editText.setVisibility(v.GONE);

        recyclerView = v.findViewById(R.id.emergencyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        getContacts();

        adapter = new MyAdapterContactClass(list);
        recyclerView.setAdapter(adapter);

        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListContacts.class);
                startActivityForResult(intent,1);
            }
        });

        adapter.setOnItemClickListener(new MyAdapterContactClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String number = list.get(position).mNumber;

                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                }else{
                    String dial = "tel:" + number;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }

            @Override
            public void onDeleteClick(final int position) {
                documentReference = fdb.collection("List").document(emailId)
                        .collection("Contact List").document(list.get(position).getmName());

                documentReference.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                Log.i("Contact", "Failed to Delete");                    }
                });

                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        return  v;
    }

    //Get from dB
    public void getContacts(){
        collectionReference = fdb.collection("List").document(emailId).collection("Contact List");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qds : queryDocumentSnapshots){
                    ContactClass contact = qds.toObject(ContactClass.class);

                    ContactClass obj = new ContactClass(contact.getmImageResource(),
                            contact.getmName(),contact.getmNumber(),contact.getmRelation());
                    list.add(obj);
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                int contactImage = data.getIntExtra("contactImage",R.drawable.ic_person);
                String contactName = data.getStringExtra("contactName");
                String contactNumber = data.getStringExtra("contactNumber");
                String contactRelation = data.getStringExtra("contactRelation");

                ContactClass obj = new ContactClass(contactImage,contactName,contactNumber,contactRelation);
                list.add(new ContactClass(contactImage,contactName,contactNumber,contactRelation));
                fdb.collection("List").document(emailId).collection("Contact List").document(contactName).set(obj);

                adapter.notifyDataSetChanged();

            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getContext(), "Please Select a Contact", Toast.LENGTH_LONG).show();
            }
        }
    }
}
