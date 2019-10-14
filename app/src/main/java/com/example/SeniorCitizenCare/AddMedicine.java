package com.example.SeniorCitizenCare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.savvi.rangedatepicker.CalendarPickerView;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMedicine extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{

    Button time;
    EditText MedicineName;
    LinearTimePickerDialog dialog;
    boolean showTutorial = true;
    WeekdaysPicker widget;
    String t, username, emailid;
    Button datepicker2, delete;
    ArrayList<Date> dates;
    int[] selectedTimings = new int[7];


    //database reference objects
    DatabaseReference  databaseMedicines;
    String act;

    //firestore database
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        widget = (WeekdaysPicker) findViewById(R.id.weekdays);
        MedicineName = findViewById(R.id.MedicineName);
        datepicker2 = (Button) findViewById(R.id.DatePicker2);
        datepicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        delete = findViewById(R.id.delete);

        //Retrieving Bundle information
        Bundle bundle = getIntent().getExtras();
        act = bundle.getString("Activity");
        if(act.equals("CalendarFragment")) {
            username = bundle.getString("name");
            emailid = bundle.getString("emailid");


            databaseMedicines = FirebaseDatabase.getInstance().getReference("Medicines");

//            time = findViewById(R.id.time);
//
//            dialog = LinearTimePickerDialog.Builder.with(this)
//                    .setShowTutorial(showTutorial)
//                    .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(DialogInterface dialog, int hour, int minutes) {
//                            Toast.makeText(AddMedicine.this, "" + hour + ":" + minutes, Toast.LENGTH_SHORT).show();
//                            t = "" + hour + ":" + minutes;
//
//                            showTutorial = false;
//                        }
//
//                        @Override
//                        public void onNegative(DialogInterface dialog) {
//
//                        }
//                    }).build();

//            time.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.show();
//                }
//            });
        }
        else if(act.equals("DailyMedsFragment")){
            delete.setVisibility(View.VISIBLE);
             medName = bundle.getString("medname");
            updateUI(medName);
        }
    }

    String medName;

    public void DeleteMedicine(View view){
        fdref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddMedicine.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    DocumentReference fdref;

    public void updateUI(final String medName){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String email = acct.getEmail();
            fdref = fdb.collection("List").document(email).collection("Medicine List").document(medName);
            fdref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Medicine medicine = documentSnapshot.toObject(Medicine.class);
                    String Name = medicine.getName();
                    MedicineName.setText(Name);
                    selectedDays = (ArrayList<Integer>) medicine.getDays();
                    widget.setSelectedDays(selectedDays);
                    timings = medicine.getSelectedtimings();
                    dates = medicine.getSelecteddates();
                    for(int i = 0; i < timings.size(); i++){
                        if(timings.get(i) == 1){
                            switch (i){
                                case 0:
                                    CheckBox chk = findViewById(R.id.before_breakfast);
                                    chk.setChecked(true);
                                    selectedTimings[0] = 1;
                                    break;
                                case 1:
                                    CheckBox chk1 = findViewById(R.id.after_breakfast);
                                    chk1.setChecked(true);
                                    selectedTimings[1] = 1;
                                    break;
                                case 2:
                                    CheckBox chk2 = findViewById(R.id.before_lunch);
                                    chk2.setChecked(true);
                                    selectedTimings[2] = 1;
                                    break;
                                case 3:
                                    CheckBox chk3 = findViewById(R.id.after_lunch);
                                    chk3.setChecked(true);
                                    selectedTimings[3] = 1;
                                    break;
                                case 4:
                                    CheckBox chk4 = findViewById(R.id.afternoon);
                                    chk4.setChecked(true);
                                    selectedTimings[4] = 1;
                                    break;
                                case 5:
                                    CheckBox chk5 = findViewById(R.id.before_dinner);
                                    chk5.setChecked(true);
                                    selectedTimings[5] = 1;
                                    break;
                                case 6:
                                    CheckBox chk6 = findViewById(R.id.after_dinner);
                                    chk6.setChecked(true);
                                    selectedTimings[6] = 1;
                                    break;
                            }
                        }
                    }
                }
            });
        }

    }

    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void selectTimings(View view){
        boolean checked = ((CheckBox)  view ).isChecked();
        switch (view.getId()){
            case R.id.before_breakfast:
                if(checked)
                    selectedTimings[0] = 1;
                else
                    selectedTimings[0] = 0;
                break;
            case R.id.after_breakfast:
                if(checked)
                    selectedTimings[1] = 1;
                else
                    selectedTimings[1] = 0;
                break;
            case R.id.before_lunch:
                if(checked)
                    selectedTimings[2] = 1;
                else
                    selectedTimings[2] = 0;
                break;
            case R.id.after_lunch:
                if(checked)
                    selectedTimings[3] = 1;
                else
                    selectedTimings[3] = 0;
                break;
            case R.id.afternoon:
                if(checked)
                    selectedTimings[4] = 1;
                else
                    selectedTimings[4] = 0;
                break;
            case R.id.before_dinner:
                if(checked)
                    selectedTimings[5] = 1;
                else
                    selectedTimings[5] = 0;
                break;
            case R.id.after_dinner:
                if(checked)
                    selectedTimings[6] = 1;
                else
                    selectedTimings[6] = 0;
                break;
        }
    }
    ArrayList<Integer> timings = new ArrayList<>();
    List<Integer> selectedDays; // for weekdays

    public void Save(View view){
        final String name = MedicineName.getText().toString();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null) {
            emailid = acct.getEmail();
            if (act.equals("CalendarFragment")) {
                selectedDays = widget.getSelectedDays();
                for (int i = 0; i < selectedTimings.length; i++) {
                    timings.add(0);
                    timings.set(i, selectedTimings[i]);
                }
            } else {
                selectedDays = widget.getSelectedDays();
                for (int i = 0; i < selectedTimings.length; i++) {
                    timings.set(i, selectedTimings[i]);
                }
            }

            ArrayList<Date> date2 = new ArrayList<>();
            //test this
            if(dates!=null) {
                for (Date date : dates) {
                    int dateDayint = CalendarFragment.getDay(date);
                    if (selectedDays.contains(dateDayint)) {
                        date2.add(date);
                    }
                }
            }

            //for Firestore
            if (!TextUtils.isEmpty(name) && dates != null && !selectedDays.isEmpty() && !timings.isEmpty()) {
                Medicine medicine = new Medicine(name, date2, selectedDays, timings);



                fdb.collection("List").document(emailid).collection("Medicine List").document(name).set(medicine)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent data = new Intent();
                                data.putExtra(CalendarFragment.MEDNAME, name);
                                if (getParent() == null) {
                                    setResult(CalendarFragment.RESULT_CODE, data);
                                } else {
                                    getParent().setResult(CalendarFragment.RESULT_CODE, data);
                                }
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMedicine.this, "Failure in Saving", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(AddMedicine.this, "Make sure all entries are specified", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void applyTexts(ArrayList<Date> dates) {
        if(!dates.isEmpty()) {
            this.dates = dates; // getting list of dates from dialog listener
            Toast.makeText(this, "" + this.dates.size(), Toast.LENGTH_LONG).show();
        }
    }
}
