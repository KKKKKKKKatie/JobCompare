package edu.gatech.seclass.jobcompare6300.controller;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.User;

public class MainMenu extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button updateCurrentJob = findViewById(R.id.btn_update_current_job);
        Button addJobOffer = findViewById(R.id.btn_add_job_offer);
        Button adjustComparisonSettings = findViewById(R.id.btn_adjust_comparison_settings);
        Button compareJobOffers = findViewById(R.id.btn_compare_job_offers);

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();
        SQLiteDatabase database = databaseHelper.getDatabase();
        User user = databaseHelper.getSingularUser();
        databaseHelper.close();

        updateCurrentJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to UpdateCurrentJob
                Intent intent = new Intent(MainMenu.this, UpdateCurrentJob.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent); // Start the new Activity
            }
        });

        addJobOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to AddAJobOffer
                Intent intent = new Intent(MainMenu.this, AddAJobOffer.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent); // Start the new Activity
            }
        });

        adjustComparisonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to AdjustComparisonSettings
                Intent intent = new Intent(MainMenu.this, AdjustComparisonSettings.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent); // Start the new Activity
            }
        });

        compareJobOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to CompareJobOffers
                Intent intent = new Intent(MainMenu.this, CompareJobOffers.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent); // Start the new Activity
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}