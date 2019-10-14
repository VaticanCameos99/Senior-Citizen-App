package com.example.SeniorCitizenCare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
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

    TextView medicine, linkText;
    TextView afternoonText, eveningText, morningText;

    RecyclerView recyclerView, recyclerViewAfternoon, recyclerViewEvening;
    myAdapterClass adapter, adapterAfternoon, adapterEvening;
    GoogleSignInAccount acct;
    View v;
    CardView toadysList, exercises, diet, yourMedList;
    TextView exerciseGrid, allMedsGrid, dietGrid, todayGrid;
    ImageView morningImage, afternoonImage, nightImage;

    private MyAdapterYogaClass yogaAdapter;
    private ArrayList<YogaClass> yogaList;
    
    private MyAdapterDietClass adapterDietClass;
    private ArrayList<String> dietList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_daily_meds, container, false);
        morningText = v.findViewById(R.id.MorningText);
        afternoonText = v.findViewById(R.id.AfternoonText);
        eveningText = v.findViewById(R.id.EveningText);
        linkText = v.findViewById(R.id.linkText);
        morningImage = v.findViewById(R.id.morningImage);
        afternoonImage = v.findViewById(R.id.afternoonImage);
        nightImage = v.findViewById(R.id.nightImage);

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

            updateUI();

            toadysList = v.findViewById(R.id.Todayslist);
            exercises = v.findViewById(R.id.exercises);
            diet = v.findViewById(R.id.diet);
            yourMedList = v.findViewById(R.id.YourMeds);
            todayGrid = v.findViewById(R.id.gridToday);
            exerciseGrid = v.findViewById(R.id.gridExercise);
            dietGrid = v.findViewById(R.id.gridDiet);
            allMedsGrid = v.findViewById(R.id.gridAllMeds);

            toadysList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUI();
                    todayGrid.setBackgroundResource(R.drawable.todaygridclicked);
                    exerciseGrid.setBackgroundResource(R.drawable.exercisegrid);
                    dietGrid.setBackgroundResource(R.drawable.dietgrid);
                    allMedsGrid.setBackgroundResource(R.drawable.allmedsgrid);
                }
            });

            exercises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUIForExercise();
                    exerciseGrid.setBackgroundResource(R.drawable.exercisegridclicked);
                    todayGrid.setBackgroundResource(R.drawable.todaygrid);
                    dietGrid.setBackgroundResource(R.drawable.dietgrid);
                    allMedsGrid.setBackgroundResource(R.drawable.allmedsgrid);
                }
            });

            diet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Diet();
                    todayGrid.setBackgroundResource(R.drawable.todaygrid);
                    exerciseGrid.setBackgroundResource(R.drawable.exercisegrid);
                    dietGrid.setBackgroundResource(R.drawable.dietgridclicked);
                    allMedsGrid.setBackgroundResource(R.drawable.allmedsgrid);
                }
            });

            yourMedList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TotalMedList();
                    allMedsGrid.setBackgroundResource(R.drawable.allmedsgridclicked);
                    todayGrid.setBackgroundResource(R.drawable.todaygrid);
                    exerciseGrid.setBackgroundResource(R.drawable.exercisegrid);
                    dietGrid.setBackgroundResource(R.drawable.dietgrid);
                }
            });
        }
        return v;
    }

    public void TotalMedList(){
        hideRecycler();
        emailid = acct.getEmail();
        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        linkText.setText("All Medicines");


        final ArrayList<Medicine> totalMedList = new ArrayList<>();
        fcref = db.collection("List").document(emailid).collection("Medicine List");
        fcref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                    Medicine med = ds.toObject(Medicine.class);
                    totalMedList.add(med);
                }

                ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300 + 20*totalMedList.size(), getResources().getDisplayMetrics());
                params.height = h;
                recyclerView.setLayoutParams(params);

                adapter = new myAdapterClass(totalMedList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new myAdapterClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), " " + totalMedList.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddMedicine.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("medname", totalMedList.get(position).getName());
                        intent.putExtras(bundle);
                        intent.putExtra("Activity", "DailyMedsFragment");
                        startActivityForResult(intent, 3);
                    }
                });
            }
        });
    }

    public void Diet(){
        //Hide Unnecessary Recycler Views
        hideRecycler();

        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 420, getResources().getDisplayMetrics());
        params.height = h;
        recyclerView.setLayoutParams(params);
        linkText.setText("Diet");

        dietList = new ArrayList<>();
        dietList.add("Diet for Diabetes");
        dietList.add("Diet for Heart Issues");
        dietList.add("Diet for Weight Gain");
        dietList.add("Diet for Weight Loss");
        dietList.add("Diet for Diarrhea");
        dietList.add("Diet for Vomiting");
        dietList.add("Diet for Acidity");

        adapterDietClass = new MyAdapterDietClass(dietList);
        recyclerView.setAdapter(adapterDietClass);

        adapterDietClass.setOnItemClickListener(new MyAdapterDietClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String dietName = dietList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);

                if(dietName=="Diet for Diabetes")
                    intent.setData(Uri.parse("https://www.healthline.com/nutrition/16-best-foods-for-diabetics#section1"));

                if(dietName=="Diet for Heart Issues")
                    intent.setData(Uri.parse("https://www.healthline.com/nutrition/heart-healthy-foods"));

                if(dietName=="Diet for Weight Gain")
                    intent.setData(Uri.parse("https://www.healthline.com/nutrition/18-foods-to-gain-weight"));

                if(dietName=="Diet for Weight Loss")
                    intent.setData(Uri.parse("https://www.healthline.com/nutrition/20-most-weight-loss-friendly-foods"));

                if(dietName=="Diet for Diarrhea")
                    intent.setData(Uri.parse("https://www.healthline.com/health/what-to-eat-when-you-have-diarrhea"));

                if(dietName=="Diet for Vomiting")
                    intent.setData(Uri.parse("https://www.livestrong.com/article/330220-the-best-foods-to-eat-after-throwing-up/"));

                if(dietName=="Diet for Acidity")
                    intent.setData(Uri.parse("https://www.healthline.com/health/gerd/diet-nutrition"));

                startActivity(intent);
            }
        });

    }

    public void updateUIForExercise(){

        //Hide Unnecessary Recycler Views
        hideRecycler();
        linkText.setText("Exercise");

        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000, getResources().getDisplayMetrics());
        params.height = h;
        recyclerView.setLayoutParams(params);

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

    final static int RQS_1 = 1;
    private void setAlarm(Context context , ArrayList<Calendar> targetCal) {

//        info.setText("\n\n***\n"
//                + "Alarm is set@ " + targetCal.getTime() + "\n"
//                + "***\n");

        Intent intent = new Intent(context , AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(Calendar ct : targetCal) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, ct.getTimeInMillis(), pendingIntent);
        }
    }

    public void updateUI(){
        recyclerView = v.findViewById(R.id.DailyMedsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));   //TODO : Check this
        recyclerViewAfternoon = v.findViewById(R.id.DailyMedsAfternoonRecycler);
        recyclerViewAfternoon.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEvening = v.findViewById(R.id.DailyMedsEveningRecycler);
        recyclerViewEvening.setLayoutManager(new LinearLayoutManager(getContext()));

        showRecycler();
        linkText.setText("");

        emailid = acct.getEmail();

        fcref = db.collection("List").document(emailid).collection("Medicine List");


        //Get today's day of the week
        final Calendar calendar = Calendar.getInstance();
        final int cDay = calendar.get(Calendar.DAY_OF_WEEK);  //get current day
        final ArrayList<Medicine> morning = new ArrayList<>();
        final ArrayList<Medicine> afternoon = new ArrayList<>();
        final ArrayList<Medicine> evening = new ArrayList<>();

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
                for(Medicine mt : medList) {
                    selectedTime = mt.getSelectedtimings();
                    if(selectedTime.get(0) == 1) {      //before breakfast
                        morning.add(mt);
                    }
                    if(selectedTime.get(1) == 1) {     //after breakfast
                        morning.add(mt);
                    }
                    if(selectedTime.get(2) == 1) {     //before lunch
                        morning.add(mt);
                    }
                    if(selectedTime.get(3) == 1) {     //after lunch
                        afternoon.add(mt);
                    }
                    if(selectedTime.get(4) == 1) {     //afternoon
                        afternoon.add(mt);
                    }
                    if(selectedTime.get(5) == 1) {    //before dinner
                        evening.add(mt);
                    }
                    if(selectedTime.get(6) == 1) {    //after dinner
                        evening.add(mt);
                    }
                }

                final ArrayList<Medicine> finalMedList = new ArrayList<>();
                finalMedList.addAll(morning);
                finalMedList.addAll(afternoon);
                finalMedList.addAll(evening);
                adapter = new myAdapterClass(finalMedList);
                recyclerView.setAdapter(adapter);
                ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10+ 60*morning.size(), getResources().getDisplayMetrics());
                params.height = h;
                recyclerView.setLayoutParams(params);

                recyclerViewAfternoon.setAdapter(adapterAfternoon);
                ViewGroup.LayoutParams paramsAfternoon = recyclerViewAfternoon.getLayoutParams();
                int hA = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10+60*afternoon.size(), getResources().getDisplayMetrics());
                paramsAfternoon.height = hA;
                recyclerViewAfternoon.setLayoutParams(paramsAfternoon);

                recyclerViewEvening.setAdapter(adapterEvening);
                ViewGroup.LayoutParams paramsEvening = recyclerViewEvening.getLayoutParams();
                int hE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10+60*evening.size(), getResources().getDisplayMetrics());
                paramsEvening.height = hE;
                recyclerViewEvening.setLayoutParams(paramsEvening);

                adapter.setOnItemClickListener(new myAdapterClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), " " + morning.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddMedicine.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("medname", morning.get(position).getName());
                        intent.putExtras(bundle);
                        intent.putExtra("Activity", "DailyMedsFragment");
                        startActivityForResult(intent, 3);
                    }
                });

                adapterAfternoon.setOnItemClickListener(new myAdapterClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), " " + afternoon.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddMedicine.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("medname", afternoon.get(position).getName());
                        intent.putExtras(bundle);
                        intent.putExtra("Activity", "DailyMedsFragment");
                        startActivityForResult(intent, 3);
                    }
                });

                adapterEvening.setOnItemClickListener(new myAdapterClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), " " + evening.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddMedicine.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("medname", evening.get(position).getName());
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
        yogaList.add(new YogaClass(0, "Iyengar Yoga",
                "The emphasis on this practice is alignment in the asanas" +
                        " using breath control through pranayama and the use of props (bolsters, blankets, blocks and straps.)"
                , "https://www.youtube.com/watch?v=5KrDlrmsRrU"));
        yogaList.add(new YogaClass(0 ,"Bikram Yoga",
                "Class consists of the same twenty-six yoga postures and two breathing exercises." +
                        " It is ninety minutes long and done in a room that is 105 degrees Fahrenheit with 40% humidity." +
                        " The room is bright and the students face mirrors to check proper posture and alignment. ",
                "https://www.youtube.com/watch?v=yFtDSWWfYmI"));
        yogaList.add(new YogaClass(0 ,"Jivamukti Yoga",
                "Class incorporates Sanskrit chanting, Pranayama, and movement (Asanas), with a theme or lesson for each class." +
                        " This is a good blend of spiritual and physical exercise.",
                "https://www.youtube.com/watch?v=fB6B9rQjEeU"));
        yogaList.add(new YogaClass(0 ,"Power Yoga",
                "Power yoga is a more active approach to the traditional Hatha yoga poses." +
                        " The Ashtanga yoga poses are performed more quickly and with added core exercises and upper body work.",
                "https://www.youtube.com/watch?v=Ue0f6VHRiFE"));
        yogaList.add(new YogaClass(0 ,"Sivananda Yoga",
                "This is a yoga system based on the five yogic principals: " +
                        "proper breathing, relaxation, diet, exercise, and positive thinking." +
                        " The asana practice is usually twelve basic postures or variations of the Asanas, with Sun Salutations and Savasana.",
                "https://www.youtube.com/watch?v=AN0fVx8gi8w"));
        yogaList.add(new YogaClass(0 ,"Yin Yoga",
                "Yin yoga is a meditative practice that allows your body to become" +
                        " comfortable in a pose without doing any work (strength)." +
                        " It focuses on lengthening the connective tissues within the body.",
                "https://www.youtube.com/watch?v=3YOYyQ8cb5c"));
    }

    //Hide Unnecessary Recycler Views
    public void hideRecycler(){
        morningText.setVisibility(View.GONE);
        morningImage.setVisibility(View.GONE);

        afternoonText = v.findViewById(R.id.AfternoonText);
        afternoonText.setVisibility(View.GONE);
        afternoonImage.setVisibility(View.GONE);

        eveningText = v.findViewById(R.id.EveningText);
        eveningText.setVisibility(View.GONE);
        nightImage.setVisibility(View.GONE);

        recyclerViewAfternoon = v.findViewById(R.id.DailyMedsAfternoonRecycler);
        recyclerViewAfternoon.setVisibility(View.GONE);

        recyclerViewEvening = v.findViewById(R.id.DailyMedsEveningRecycler);
        recyclerViewEvening.setVisibility(View.GONE);

    }

    public void showRecycler(){
        morningText.setVisibility(View.VISIBLE);
        morningImage.setVisibility(View.VISIBLE);

        afternoonText = v.findViewById(R.id.AfternoonText);
        afternoonText.setVisibility(View.VISIBLE);
        afternoonImage.setVisibility(View.VISIBLE);

        eveningText = v.findViewById(R.id.EveningText);
        eveningText.setVisibility(View.VISIBLE);
        nightImage.setVisibility(View.VISIBLE);

        recyclerViewAfternoon = v.findViewById(R.id.DailyMedsAfternoonRecycler);
        recyclerViewAfternoon.setVisibility(View.VISIBLE);

        recyclerViewEvening = v.findViewById(R.id.DailyMedsEveningRecycler);
        recyclerViewEvening.setVisibility(View.VISIBLE);
    }

}
