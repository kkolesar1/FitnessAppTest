package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class workout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        int workoutDay = getIntent().getIntExtra("workoutDay", 0);
        int workoutWeek = getIntent().getIntExtra("workoutWeek", 0);
        String[][][] weight = new String[10][7][]; // hold the weights for each exercise on each day [week][day][exercise]
        int[] numOfExercisesPerDay = {7, 9, 7, 8, 5, 8, 5};
        Button reset;


        final File storageDirectory;
        storageDirectory = getFilesDir();
        final File saveData = new File(storageDirectory, "saveData.csv");
        reset = findViewById(R.id.reset);

        Log.i("workoutDay", "Contents of workoutDay are " + workoutDay);
        //Initialize jagged array size
        for (int i = 0; i < weight.length; i += 1){
            //Toast.makeText(workout.this, "executing loop", Toast.LENGTH_SHORT).show();
            for (int j = 0; j < weight[i].length; j += 1){
                weight[i][j] = new String[numOfExercisesPerDay[j]];
                //Log.i("weightUpdate", "Contents of weight at " + i + j + " are " + weight[i][j]);
            }
        }

        // temporary delete button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.delete();
            }
        });

        // Display Banner and background image based on the day
        ImageView cl = findViewById(R.id.bgImage);
        int currentDay = getResources().getIdentifier("day" + workoutDay + "w", "drawable", this.getPackageName());
        Log.i("wdupdate", "Setting Background " + currentDay);
        cl.setBackgroundResource(setBackground(workoutDay));



        // load saved data
        loadData(weight, saveData);

        // display the workouts based on the day and load

        //

    }

    private void writeNewArray(String[][][] weight){
        for (int week = 0; week < 10; week += 1) {
            for (int day = 0; day < 7; day += 1) {
                for (int exercise = 0; exercise < weight[week][day].length; exercise += 1) {
                    // set all array values to 0
                    weight[week][day][exercise] = "0";
                    //Log.i("weightUpdate", "Contents of weight at " + week + day + exercise + " are " + weight[week][day][exercise]);
                }
            }
        }
    }

    private int setBackground(int day){
        String bgImgName = "day" + (day + 1) + "w";
        int resID = getResources().getIdentifier(bgImgName, "drawable", this.getPackageName());
        return resID;
    }
    private void updateArray (int week, int day, String[][][] weight, int exercise, String value){
        weight[week][day][exercise] = value;
    }

    private void loadData (String[][][] weight, File saveData){
        if(!saveData.exists()){
            try {
                saveData.createNewFile();
                // create empty weight sheet
                writeNewArray(weight);
                //printTest(weight);
            } catch (IOException e) {
                Log.e("IOException", "Unable to create save file");
            }
        } else { // if the file already exists, load any saved data in it.
            try (Scanner scanner = new Scanner(saveData)){
                Log.i("test", "Loading Saved File");
                String row;
                for (int week = 0; week < 10; week += 1) {
                    for (int day = 0; day < 7; day += 1) {
                        if(scanner.hasNextLine()){
                            row = scanner.nextLine();
                            weight[week][day] = row.split(",");
                            //printTest(weight);
                            //Log.i("weightUpdate", "Contents of weight at " + week + day + exercise + " are " + weight[week][day][exercise]);
                            //exercise += 1;
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("IOException", "Unable to create File");
            }
        }
        saveToFile(weight, saveData);
    }

    private void saveToFile(String[][][] weight, File saveFile){
        FileWriter writer;
        Log.i("Save", "Saving to File");
        // save the weight array to csv file
        try {
            writer = new FileWriter(saveFile); // this will overwrite any file with same name
            for(int week = 0; week < weight.length; week += 1){
                for (int day = 0; day < weight[week].length; day += 1){
                    Log.i("savetest", "string being appended at " + week + day + " is " + String.join(",", weight[week][day]));
                    writer.append(String.join(",", weight[week][day]));
                    writer.append("\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("IOException", "Unable to write to File");
        }
    }

    // Test function to determine if load and save work correctly
    private void printTest(String[][][] weight){
        Log.i("test", "Testing Print");
        for (int week = 0; week < 10; week += 1) {
            for (int day = 0; day < 7; day += 1) {
                for (int exercise = 0; exercise < weight[week][day].length; exercise += 1) {
                    // set all array values to 0
                    Log.i("weightPrint", "printTest weight at " + week + day + exercise + " are " + weight[week][day][exercise]);
                    //weight[week][day][exercise] = "0";
                }
            }
        }
    }

    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = findViewById (hotspotId);
        Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        return bitmap.getPixel(x, y);
    }
}

