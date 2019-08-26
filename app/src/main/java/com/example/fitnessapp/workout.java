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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        final int workoutDay = getIntent().getIntExtra("workoutDay", 0);
        final int workoutWeek = getIntent().getIntExtra("workoutWeek", 0);
        final String[][][] weight = new String[10][7][]; // hold the weights for each exercise on each day [week][day][exercise]
        int[] numOfExercisesPerDay = {7, 9, 7, 8, 5, 8, 5};
        int[] idStore = new int[numOfExercisesPerDay[workoutDay]];

        final File storageDirectory;
        storageDirectory = getFilesDir();
        final File saveData = new File(storageDirectory, "saveData.csv");

        //Initialize jagged array size
        for (int i = 0; i < weight.length; i += 1){
            for (int j = 0; j < weight[i].length; j += 1){
                weight[i][j] = new String[numOfExercisesPerDay[j]];
            }
        }

        // Display Banner and background image based on the day
        ImageView cl = findViewById(R.id.bgImage);
        cl.setBackgroundResource(setBackground(workoutDay));



        // load saved data
        loadData(weight, saveData);

        // insert the inputboxes based on day
        for (int i = 0; i < numOfExercisesPerDay[workoutDay]; i += 1){
            final EditText myEditText = new EditText(this);
            float dpFactor = getResources().getDisplayMetrics().density; // this allows pixel to DP conversion
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams((int) (50 * dpFactor), (int) (50 * dpFactor));

            // set the params for each num input
            if (i == 0) {
                rp.setMargins((int) (345 * dpFactor), (int) (250 * dpFactor), 0, 0);
            } else {
                rp.addRule(RelativeLayout.BELOW, idStore[i - 1]);
                rp.setMargins((int) (345 * dpFactor), (int) (8 * dpFactor), 0, 0);
            }
            myEditText.setLayoutParams(rp);

            // add the num inputs
            RelativeLayout workoutLayout = findViewById(R.id.workoutLayout);
            workoutLayout.addView(myEditText);

            // generate usable ids for the numinputs
            idStore[i] = View.generateViewId();
            myEditText.setId(idStore[i]);

            //update the boxes with data loaded from file if applicable
            if(weight[workoutWeek][workoutDay][i] != "0"){
                myEditText.setText(weight[workoutWeek][workoutDay][i]);
            }

            final int temp = i;
            //set listeners to save updates when user enters weights
            myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        updateArray(workoutWeek, workoutDay, weight, temp, myEditText.getText().toString(), saveData);
                    }
                }
            });
        }
    }

    private void writeNewArray(String[][][] weight){
        for (int week = 0; week < 10; week += 1) {
            for (int day = 0; day < 7; day += 1) {
                for (int exercise = 0; exercise < weight[week][day].length; exercise += 1) {
                    // set all array values to 0
                    weight[week][day][exercise] = "0";
                }
            }
        }
    }

    private int setBackground(int day){
        String bgImgName = "day" + (day + 1) + "w";
        int resID = getResources().getIdentifier(bgImgName, "drawable", this.getPackageName());
        return resID;
    }

    private void updateArray (int week, int day, String[][][] weight, int exercise, String value, File saveFile){
        weight[week][day][exercise] = value;
        saveToFile(weight, saveFile);
    }

    private void loadData (String[][][] weight, File saveData){
        if(!saveData.exists()){
            try {
                saveData.createNewFile();
                // create empty weight sheet
                writeNewArray(weight);
                saveToFile(weight, saveData);
            } catch (IOException e) {
                Log.e("IOException", "Unable to create save file");
            }
        } else { // if the file already exists, load any saved data in it.
            try (Scanner scanner = new Scanner(saveData)){
                String row;
                for (int week = 0; week < 10; week += 1) {
                    for (int day = 0; day < 7; day += 1) {
                        if(scanner.hasNextLine()){
                            row = scanner.nextLine();
                            weight[week][day] = row.split(",");
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("IOException", "Unable to read file");
            }
        }
    }

    private void saveToFile(String[][][] weight, File saveFile){
        FileWriter writer;
        // save the weight array to csv file
        try {
            writer = new FileWriter(saveFile); // this will overwrite any file with same name
            for(int week = 0; week < weight.length; week += 1){
                for (int day = 0; day < weight[week].length; day += 1){
                    writer.append(String.join(",", weight[week][day]));
                    writer.append("\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("IOException", "Unable to write to file");
        }
    }
}

