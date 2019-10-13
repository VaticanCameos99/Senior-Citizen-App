package com.example.SeniorCitizenCare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
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

import android.content.Context;

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
    CardView toadysList, exercises, diet, yourMedList;

    private MyAdapterYogaClass yogaAdapter;
    private ArrayList<YogaClass> yogaList;


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

            toadysList = v.findViewById(R.id.Todayslist);
            exercises = v.findViewById(R.id.exercises);
            diet = v.findViewById(R.id.diet);
            yourMedList = v.findViewById(R.id.YourMeds);

            toadysList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUI();
                }
            });

            exercises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUIForExercise();
                }
            });

            diet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Diet();
                }
            });

            yourMedList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TotalMedList();
                }
            });
        }
        return v;
    }

    public void TotalMedList(){

    }

    public void Diet(){
        Intent intent = new Intent(getContext(), DietActivity.class);
        startActivity(intent);
    }

    public void updateUIForExercise(){
        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addYoga();
        yogaAdapter = new MyAdapterYogaClass(yogaList);
        recyclerView.setAdapter(yogaAdapter);

        yogaAdapter.setOnItemClickListener(new MyAdapterYogaClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(yogaList.get(position).getmLink()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    TextView info;
    final static int RQS_1 = 1;
    private void setAlarm(Context context , Calendar targetCal) {

//        info.setText("\n\n***\n"
//                + "Alarm is set@ " + targetCal.getTime() + "\n"
//                + "***\n");

        Intent intent = new Intent(context , AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
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
                Context context = getContext();
                final ArrayList<Medicine> medList = new ArrayList<>();
                for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                    Medicine med = ds.toObject(Medicine.class);
                    List<Integer> days = med.getDays();
                    if(days.contains(cDay)){
                        medList.add(med);
                    }
                    //add to list here
                }

                ArrayList<Integer> selectedTime;
                Calendar cal = Calendar.getInstance();
                for(Medicine mt : medList) {
                    selectedTime = mt.getSelectedtimings();
                    if(selectedTime.get(0) == 1) {            //before breakfast
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 7 ,  00);
                    }
                    else if(selectedTime.get(1) == 1) {     //after breakfast
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 9 ,  00);
                    }
                    else if(selectedTime.get(2) == 1) {     //before lunch
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 11 ,  30);
                    }
                    else if(selectedTime.get(3) == 1) {     //after lunch
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 13 ,  30);
                    }
                    else if(selectedTime.get(4) == 1) {     //afternoon
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 16 ,  30);
                    }
                    else if(selectedTime.get(5) == 1) {    //before dinner
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 19 ,  30);
                    }
                    else if(selectedTime.get(6) == 1) {    //after dinner
                        cal.set(Calendar.YEAR , Calendar.MONTH , Calendar.DAY_OF_MONTH , 22 ,  00);
                    }
                }
                setAlarm(context , cal);

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

    public void addYoga(){
        yogaList = new ArrayList<>();
        yogaList.add(new YogaClass(R.drawable.ic_person, "Vinyasa Yoga",
                "Vinyasa yoga is popular and is taught at most studios and gyms. “Vinyasa” means linking breath with movement.",
                "https://www.youtube.com/watch?v=9kOCY0KNByw"));
        yogaList.add(new YogaClass(R.drawable.ic_person, "Ashtanga Yoga",
                "Ashtanga means “eight limbs” and encompasses a yogic lifestyle",
                "https://www.youtube.com/watch?v=OAg0oNHVjXQ"));

    }

}
