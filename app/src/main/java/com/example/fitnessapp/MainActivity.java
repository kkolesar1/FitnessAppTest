package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{

    //int[][][] weight; // hold the weights for each exercise on each day [week][day][exercise]
    private int[] weekImages;
    private String[] weeks;
    private Spinner mSpinner;
    private ImageButton imageButton;
    int workoutWeek = 0;
    private boolean isUserInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSpinner = findViewById(R.id.spinner);
        weekImages = new int[]{R.drawable.week1, R.drawable.week2, R.drawable.week3, R.drawable.week4, R.drawable.week5, R.drawable.week6, R.drawable.week7, R.drawable.week8, R.drawable.week9, R.drawable.week10};
        weeks = new String[]{"Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10"};



        CustomAdapter mCustomAdapter = new CustomAdapter(MainActivity.this, weekImages); // create instance of custom adapter for image spinner
        mSpinner.setAdapter(mCustomAdapter);

        //do stuff when spinner is clicked
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isUserInteracting) {
                    Toast.makeText(MainActivity.this, weeks[i], Toast.LENGTH_SHORT).show(); //output msg when item is selected
                    // update the selected week
                    updateWeek(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // when no new item selected from spinner, change nothing
            }
        });

        // loop through image buttons and set the listeners
        for(int daySelector = 0; daySelector < 7; daySelector+=1) {
            imageButton = findViewById(getResources().getIdentifier("button" + daySelector, "id", this.getPackageName()));
            final int day = daySelector;
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openWorkout(day);
                }
            });
        }
    }

    private void updateWeek(int selectedWeek){
        workoutWeek = selectedWeek;
    }

    // open workout day based on button clicked by sending int extra with the intent
    public void openWorkout(int workoutDay){
        Intent intent = new Intent(this, workout.class);
        intent.putExtra("workoutDay", workoutDay);
        intent.putExtra("workoutWeek", workoutWeek);
        startActivity(intent);
    }

    // needed to display toast msg on selecting something in spinner
    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        isUserInteracting = true;
    }
}




