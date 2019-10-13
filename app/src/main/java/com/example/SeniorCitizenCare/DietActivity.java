package com.example.SeniorCitizenCare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

public class DietActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> mList;
    MyAdapterDietClass adapterDietClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        populateList();
        buildRecyclerView();
        onClick();

    }

    public void populateList(){
        mList = new ArrayList<>();

        mList.add("Diet for Diabetes");
        mList.add("Diet for Heart Issues");
        mList.add("Diet for Weight Gain");
        mList.add("Diet for Weight Loss");
        mList.add("Diet for Diarrhea");
        mList.add("Diet for Vomiting");
        mList.add("Diet for Acidity");

    }

    public void buildRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.dietRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterDietClass = new MyAdapterDietClass(mList);
        recyclerView.setAdapter(adapterDietClass);

        adapterDietClass.setOnItemClickListener(new MyAdapterDietClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    public void onClick(){
        adapterDietClass.setOnItemClickListener(new MyAdapterDietClass.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String dietName = mList.get(position);

//                Intent intent = new Intent(DietActivity.this, DietDetailsActivity.class);
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

//                intent.putExtra("dietName", dietName);
                startActivity(intent);
            }
        });
    }
}
