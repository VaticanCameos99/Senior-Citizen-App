package com.example.SeniorCitizenCare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMedicine extends AppCompatActivity {

    Button time;
    EditText MedicineName;
    LinearTimePickerDialog dialog;
    boolean showTutorial = true;
    WeekdaysPicker widget;
    String t, username, emailid;


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

    public void Save(View view){
        final String name = MedicineName.getText().toString();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        List<Integer> selectedDays = widget.getSelectedDays();
        //for Firestore
          if(!TextUtils.isEmpty(name)){
           Medicine medicine = new Medicine(t, selectedDays);

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
}
