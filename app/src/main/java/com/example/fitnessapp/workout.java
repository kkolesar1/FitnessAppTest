package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class workout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int workoutDay = getIntent().getIntExtra("workoutDay", 0);
        int workoutWeek = getIntent().getIntExtra("workoutWeek", 0);
        setContentView(R.layout.activity_workout);

        //set the background image with the workout? hot spots?
        Toast.makeText(workout.this, "Workout day " + workoutDay + " launched", Toast.LENGTH_SHORT).show();
    }
}
