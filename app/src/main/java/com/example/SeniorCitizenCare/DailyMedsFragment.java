package com.example.SeniorCitizenCare;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DailyMedsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference fcref;

    String emailid;

    TextView medicine;

    RecyclerView recyclerView;
    myAdapterClass adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_meds, container, false);

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

        //get UserEmailID to access document
        //medicine = v.findViewById(R.id.MedName);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
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
                        }
                    });
                }
            });
        }
        return v;
    }

    //Add onclick Listener for list
    /*
    *for modal refer: https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
    * AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
    * alert.setTitle("Do you want to Update or Delete this entry?");
    *
    * alert.setPositiveButton("Delete", new  DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            //delete entry, refer: https://www.youtube.com/watch?v=Bh0h_ZhX-Qg&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=8
        }
    });

    alert.setNegativeButton("Update",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            //Intent to new Updateactivity
            Intent intent = new Intent();

            }
        });

    alert.show();
    * */

}
