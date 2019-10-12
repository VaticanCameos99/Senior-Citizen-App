package com.example.SeniorCitizenCare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DailyMedsFragment extends Fragment {
    final int REQUEST_CODE = 3;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference fcref;

    String emailid;

    TextView medicine;

    RecyclerView recyclerView;
    myAdapterClass adapter;
    GoogleSignInAccount acct;
    View v;
    CardView toadysList, exercises;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_daily_meds, container, false);

        /* WORKFLOW:
        * make fcref to medicine_List
        * get Calendar.DAY_OF_WEEK
        * run loop to get list of all meds for that day
        * add onClick listener and move to new activity.
        * onCLick: open modal ask for delete or update
        *       if delete: go to deletefunc from this activity and perform fref.delete()
        *       else
        *           send request code to new activity,
        *           send medicineName,
        *           accept medname and extract document
        *           display details in editText.
        *           allow user to edit and save
        *           accept new entries and update
        *           check if calendar is getting updated
        * */

        acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
            //updateUI();

            toadysList = v.findViewById(R.id.Todayslist);
            exercises = v.findViewById(R.id.exercises);

            toadysList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUI();
                }
            });

            exercises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUIforExercise();
                }
            });
        }
        return v;
    }

    public void updateUIforExercise(){
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    public void updateUI(){
        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));   //TODO : Check this

        emailid = acct.getEmail();

        fcref = db.collection("List").document(emailid).collection("Medicine List");


        //Get today's day of the week
        final Calendar calendar = Calendar.getInstance();
        final int cDay = calendar.get(Calendar.DAY_OF_WEEK);  //get current day

        fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final ArrayList<Medicine> medList = new ArrayList<>();
                for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                    Medicine med = ds.toObject(Medicine.class);
                    List<Integer> days = med.getDays();
                    if(days.contains(cDay)){
                        medList.add(med);
                    }
                    //add to list here
                }
                adapter = new myAdapterClass(medList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new myAdapterClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), " " + medList.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddMedicine.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("medname", medList.get(position).getName());
                        intent.putExtras(bundle);
                        intent.putExtra("Activity", "DailyMedsFragment");
                        startActivityForResult(intent, 3);
                    }
                });
            }
        });

    }

}
