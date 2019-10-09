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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    Button datepicker2;
    ArrayList<Date> dates;
    int[] selectedTimings = new int[7];


    //database reference objects
    DatabaseReference  databaseMedicines;

    //firestore database
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        //Retrieving Bundle information
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("name");
        emailid = bundle.getString("emailid");


        datepicker2 = (Button) findViewById(R.id.DatePicker2);
        datepicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        databaseMedicines = FirebaseDatabase.getInstance().getReference("Medicines");

        MedicineName = findViewById(R.id.MedicineName);
        time = findViewById(R.id.time);

        widget = (WeekdaysPicker) findViewById(R.id.weekdays);


         dialog = LinearTimePickerDialog.Builder.with(this)
                 .setShowTutorial(showTutorial)
                 .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
             @Override
             public void onPositive(DialogInterface dialog, int hour, int minutes) {
                 Toast.makeText(AddMedicine.this, "" + hour + ":" + minutes, Toast.LENGTH_SHORT).show();
                 t = "" + hour + ":" + minutes;

                 showTutorial = false;
             }

             @Override
             public void onNegative(DialogInterface dialog) {

             }
         }).build();

         time.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.show();
             }
         });

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

    public void Save(View view){
        final String name = MedicineName.getText().toString();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        List<Integer> selectedDays = widget.getSelectedDays();
        ArrayList<Integer> timings = new ArrayList<>();
        for(int i : selectedTimings){
            timings.add(i);
        }
        //for Firestore
          if(!TextUtils.isEmpty(name)){
           Medicine medicine = new Medicine(name, dates, selectedDays,  timings);

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
        }
    }

    @Override
    public void applyTexts(ArrayList<Date> dates) {
        this.dates = dates;
        Toast.makeText(this, "" + this.dates.size(), Toast.LENGTH_LONG).show();
    }
}
