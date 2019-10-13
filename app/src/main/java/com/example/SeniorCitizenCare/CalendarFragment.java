package com.example.SeniorCitizenCare;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sun.bob.mcalendarview.MCalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;




public class CalendarFragment extends Fragment {

    private Button addMedicine;
    private MCalendarView calendarView;
    String name, emailid;
    ListView mList;

    //Firebase Database reference object
    DatabaseReference databaseMedicines;
    RecyclerView recyclerView;
    myAdapterClass adapter;

    //for firestore
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private DocumentReference fref;
    private CollectionReference fcref;
    public static final String  MEDNAME = "medname";
    public static final int RESULT_CODE = 2;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        recyclerView = v.findViewById(R.id.CalendarFragmentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));   //TODO : Check this
        //Get user Account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
            name = acct.getDisplayName();
            emailid = acct.getEmail();

            fref = fdb.collection("List").document(emailid);    //Document Refernce

            Toast.makeText(this.getActivity(), emailid, Toast.LENGTH_LONG).show();


        //Get email Id and Name -->(Send bundle) --> Save in AddMedicine
        calendarView = (MCalendarView) v.findViewById(R.id.calendar);
        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, final DateData date) {
                final Calendar calendar = Calendar.getInstance();
                fcref = fdb.collection("List").document(emailid).collection("Medicine List");
                fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //Toast.makeText(getActivity(), "On Success", Toast.LENGTH_LONG).show();
                        final ArrayList<Medicine> medList = new ArrayList<>();

                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            Medicine med = ds.toObject(Medicine.class);
                            List<Integer> days = med.getDays();
                            ArrayList<Date> selectedDates = med.getSelecteddates();
                            SimpleDateFormat sdfdate = new SimpleDateFormat("dd");
                            SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                            //SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");

                            for(Date sdate: selectedDates){
                                int sdatemonth = Integer.parseInt(sdfMonth.format(sdate));
                                int sdatedate = Integer.parseInt(sdfdate.format(sdate));
                                int sdateday = getDay(sdate);
                                //remove days here
                                if(sdatemonth == date.getMonth() && sdatedate == date.getDay() && days.contains(sdateday)){
                                    medList.add(med);
                                }
                            }
                        }

                        //display contents of medList in ListView
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
        });

        markDates();

        addMedicine = v.findViewById(R.id.AddMedicine);

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to AddMedicine Activity.
                Intent intent = new Intent(getActivity(), AddMedicine.class);
                //Sending name and emailId to be saved
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("emailid", emailid);
                intent.putExtras(bundle);
                intent.putExtra("Activity", "CalendarFragment");
                startActivityForResult(intent, 2);
            }
        });

        }
        return v;
    }

    //Mark previously stored Dates
    public void markDates(){
        updateCalendar();
    }


    public void onActivityResult(int requestCode, int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
            if(resultCode == RESULT_CODE) {
                String medname = data.getStringExtra(MEDNAME);
                Toast.makeText(getActivity(), medname, Toast.LENGTH_LONG).show();

                final Calendar calendar = Calendar.getInstance();
                updateCalendar();
            }
        }
    }

    public void updateCalendar(){
        fcref = fdb.collection("List").document(emailid).collection("Medicine List");
        fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Medicine medicine = documentSnapshot.toObject(Medicine.class);
                    ArrayList<Date> selectedDates = medicine.getSelecteddates();
                    List<Integer> days = medicine.getDays();

                    SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
                    SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                    int i = 0;
                    for(Date date : selectedDates){
                        int dateDayint = getDay(date);
                        if(dateDayint == days.get(i)){
                            i++;
                            if(i == days.size())
                                i = 0;

                            int day = Integer.parseInt(sdfDay.format(date));
                            int month = Integer.parseInt(sdfMonth.format(date));
                            calendarView = (MCalendarView) getActivity().findViewById(R.id.calendar);
                            calendarView.markDate(
                                    new DateData(2019, month, day).setMarkStyle(new MarkStyle(MarkStyle.RIGHTSIDEBAR, Color.GREEN))
                            );
                        }
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failure rendering data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static int getDay(Date d){
        String DaysOfWeek[] = {"Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"};

        SimpleDateFormat sdfday = new SimpleDateFormat("EE");
        String dday = sdfday.format(d);
        for(int i = 0; i < DaysOfWeek.length; i++){
            String dayow = DaysOfWeek[i];
            if(dayow.equals(dday)){
                return i+1;
            }
        }
        return 8;
    }
}

