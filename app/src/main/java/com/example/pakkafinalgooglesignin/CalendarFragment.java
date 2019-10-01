package com.example.pakkafinalgooglesignin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


//ISSUE 1: try to extend SQLiteOpenHelper
public class CalendarFragment extends Fragment {

    private mySQLiteOpenHelperClass SQLHandler;
    private EditText editText;
    private Button save;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = v.findViewById(R.id.simpleCalendarView);
        save = v.findViewById(R.id.Save);
        editText = v.findViewById(R.id.EventContent);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDatabase(v);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
             @Override
             public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 //returns the selected date's day, year and month
                 //used for creating a unique tree
                 selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                 ReadDatabase(view);
             }
         });

        try{
            SQLHandler = new mySQLiteOpenHelperClass(this.getActivity(), "CalendarDB", null, 1);
            sqLiteDatabase = SQLHandler.getWritableDatabase();
        //    sqLiteDatabase.execSQL("CREATE TABLE EventCalendar(Date TEXT, Event TEXT)");
        }
        catch (Exception e){
//            e.printStackTrace();
        }


        return v;
    }

    public void InsertDatabase(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", selectedDate);
        contentValues.put("Event", editText.getText().toString());
        sqLiteDatabase.insert("EventCalendar", null, contentValues);
    }

    public void ReadDatabase(View view){
        String query = "Select Event from EventCalendar where Date= " + selectedDate;
        try{
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            editText.setText(cursor.getString(0));
        }
        catch (Exception e){
            //e.printStackTrace();
            editText.setText("");
        }
    }
}

