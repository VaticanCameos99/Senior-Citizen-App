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
import sun.bob.mcalendarview.MCalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

        //Get user Account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
            name = acct.getDisplayName();
            emailid = acct.getEmail();

            fref = fdb.collection("List").document(emailid);    //Document Refernce

            Toast.makeText(this.getActivity(), emailid, Toast.LENGTH_LONG).show();


        //Get email Id and Name -->(Send bundle) --> Save in AddMedicine
        mList = v.findViewById(R.id.medList);
        calendarView = (MCalendarView) v.findViewById(R.id.calendar);
        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, final DateData date) {
                final Calendar calendar = Calendar.getInstance();
                fcref = fdb.collection("List").document(emailid).collection("Medicine List");
                fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int cDate = calendar.get(Calendar.DATE); //get current Date
                        int cDay = calendar.get(Calendar.DAY_OF_WEEK);  //get current day
                        int difference;
                        difference = date.getDay() - cDate;

                        Integer newDay = (cDay + difference)% 7;
                        if (newDay == 0)
                            newDay = 7;

                        ArrayList<Medicine> medList = new ArrayList<>();

                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            Medicine med = ds.toObject(Medicine.class);
                            List<Integer> days = med.getDays();
                            if(days.contains(newDay)){
                                medList.add(med);
                            }
                        }

                        //display contents of medList in ListView
                        Toast.makeText(getActivity(), "" + medList.size(), Toast.LENGTH_LONG).show();
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
        fcref = fdb.collection("List").document(emailid).collection("Medicine List");
        fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot ds : queryDocumentSnapshots){
                    //each documentSnapshot represents one Medicine Object from "Medicine List" Collection
                    Medicine medicine = ds.toObject(Medicine.class);
                    List<Integer> selectedDays = medicine.getDays();

                    final Calendar calendar = Calendar.getInstance();
                    calendarView = (MCalendarView) getActivity().findViewById(R.id.calendar);

                    for (int j = 0; j < selectedDays.size(); j++){
                        int k = selectedDays.get(j); //get required Day of week
                        int cDay = calendar.get(Calendar.DATE); //get current Date
                        int day = calendar.get(Calendar.DAY_OF_WEEK); //get current day
                        int nextDate;

                        if(day < k){
                            nextDate = cDay + k - 1;    //for nextDate day in this week
                        }
                        else {
                            nextDate = cDay + (7 - day + k); //for nextDate day in next week
                        }

                        for(int i = nextDate; i < calendar.getMaximum(Calendar.DAY_OF_MONTH); i = i+7){
                            calendarView.markDate(
                                    new DateData(2019, 10, i).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN))
                            );
                        }
                    }
                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
            if(resultCode == RESULT_CODE) {
                String medname = data.getStringExtra(MEDNAME);
                Toast.makeText(getActivity(), medname, Toast.LENGTH_LONG).show();

                final Calendar calendar = Calendar.getInstance();
                calendarView = (MCalendarView) getActivity().findViewById(R.id.calendar);
                fref = fdb.collection("List").document(emailid).collection("Medicine List").document(medname);
                fref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Medicine medicine = documentSnapshot.toObject(Medicine.class);
                            List<Integer> selectedDays = medicine.getDays();

                            //to mark all dates on calendar of the last entry.
//                            for (int j = 0; j < selectedDays.size(); j++){
//                                int k = selectedDays.get(j); //get required Day of week
//
//                                int cDay = calendar.get(Calendar.DATE); //get current Date
//                                int day = calendar.get(Calendar.DAY_OF_WEEK); //get current day
//                                int nextDate;
//                                if(day < k){
//                                    nextDate = cDay + k - 1;    //for nextDate day in this week
//                                }
//                                else {
//                                    nextDate = cDay + (7 - day + k); //for nextDate day in next week
//                                }
//                                for(int i = nextDate; i < calendar.getMaximum(Calendar.DAY_OF_MONTH); i = i+7){
//                                    calendarView.markDate(
//                                            new DateData(2019, 10, i).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN))
//                                    );
//                                }
//
//                            }

                            //get date Range
//                            ArrayList<Date> selectedDates = medicine.getSelecteddays();
//                            //get first Date
//                            SimpleDateFormat df = new SimpleDateFormat("dd");
//
//                            //get today's date
//                            Calendar calendar = Calendar.getInstance();
//                            //case 1: today is 10th first date is 11 ==> dif = 1 ==> fDay = 6
//                            //case 2: today is 10 firstday is 9 ==> dif = 1
//                            //case 3: same today and first date
//                            //case 4: today 19 first Day 15 ==> dif = 4
//
//                            List<Integer> Days = medicine.getDays();
//
//                            SimpleDateFormat dfmonth = new SimpleDateFormat("MM");
//                            int i = 0;
//                            for(Date date : selectedDates){
//                                int dateDay = getDay(date);
//                                if (Days.get(i) == dateDay) {
//                                    int month = Integer.parseInt(dfmonth.format(date));
//                                    int day = Integer.parseInt(df.format(date));
//                                    calendarView.markDate(
//                                            new DateData(2019, month, day).setMarkStyle(new MarkStyle(MarkStyle.RIGHTSIDEBAR, Color.rgb(52, 152, 187))));
//                                }
//                                i++;
//                                if(i == Days.size()){
//                                    i = 0;
//                                }
//                            }
                        }
                    }
                });

            }
        }
    }

    public int getDay(Date d){
        String DaysOfWeek[] = {"Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"};

        SimpleDateFormat dfday = new SimpleDateFormat("EEEE");
        String day = dfday.format(d);
        for(int i = 0; i < DaysOfWeek.length; i++){
            if(DaysOfWeek[i] == day){
                return i+1;
            }
        }
        return 8;
    }
}

