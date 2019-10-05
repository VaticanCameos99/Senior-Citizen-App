package com.example.SeniorCitizenCare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import sun.bob.mcalendarview.MCalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CalendarFragment extends Fragment {

    private mySQLiteOpenHelperClass SQLHandler;
    private EditText editText;
    private Button save, addMedicine;
    private MCalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;
    String name, emailid;
    private List<Medicine> medicineList;

    //Firebase Database reference object
    DatabaseReference databaseMedicines;

    //for firestore
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private DocumentReference fref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
            name = acct.getDisplayName();
            emailid = acct.getEmail();

            fref = fdb.collection("List").document(emailid);

            Toast.makeText(this.getActivity(), emailid, Toast.LENGTH_LONG).show();


        //Get email Id and Name -->(Send bundle) --> Save in AddMedicine

        calendarView = (MCalendarView) v.findViewById(R.id.calendar);
        save = v.findViewById(R.id.Save);
        editText = v.findViewById(R.id.EventContent);
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
                startActivityForResult(intent, 2);
            }
        });

        }
        return v;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
          /*  fref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                               Medicine medicine = documentSnapshot.toObject(Medicine.class);
                                String medname = medicine.getName();
                                String time = medicine.getTime();
                                List<Integer> selectedDays = medicine.getDays();
                                Toast.makeText(getActivity(), medname, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                            Log.d(TAG, e.toString());
                        }
                    }); */
        }
    }
}

