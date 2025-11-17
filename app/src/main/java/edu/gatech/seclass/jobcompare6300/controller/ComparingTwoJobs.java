package edu.gatech.seclass.jobcompare6300.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;
import edu.gatech.seclass.jobcompare6300.util.Calculator;

import java.util.ArrayList;
import java.util.Comparator;

public class ComparingTwoJobs extends AppCompatActivity {

    private TableLayout comparisonTable;
    private Button mainMenuButton, anotherComparisonButton;
    private ArrayList<Integer> selectedJobs;
    private int userid;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparing_two_jobs);

        comparisonTable = findViewById(R.id.comparison_table);
        mainMenuButton = findViewById(R.id.btn_main_menu);
        anotherComparisonButton = findViewById(R.id.btn_perform_another_comparison);

        Intent intent = getIntent();
        selectedJobs = intent.getIntegerArrayListExtra("selectedJobs");
        userid = intent.getIntExtra("user_id", -1);
        boolean comparing_current = intent.getBooleanExtra("compare_current", false);
        populateComparisonTable(selectedJobs, comparing_current);

        mainMenuButton.setOnClickListener(v -> {
            Intent mainMenuIntent = new Intent(ComparingTwoJobs.this, MainMenu.class);
            startActivity(mainMenuIntent);
        });

        anotherComparisonButton.setOnClickListener(v -> {
            Intent compareIntent = new Intent(ComparingTwoJobs.this, CompareJobOffers.class);
            compareIntent.putExtra("user_id", userid);
            startActivity(compareIntent);
        });
    }

    private void populateComparisonTable(ArrayList<Integer> jobTags, boolean comparing_current) {
        // Define the row headers
        String[] rowHeaders = {
                "Title",
                "Company",
                "Location",
                "AYS",
                "AYB",
                "Retirement",
                "Restricted Stock Unit",
                "Personalized Learning & Development",
                "Family Planning Assistance"
        };

        String[] job1Details = new String[9];
        String[] job2Details = new String[9];

        // Clear existing rows, but keep the header row (the first one)
        if (comparisonTable.getChildCount() > 1) {
            comparisonTable.removeViews(1, comparisonTable.getChildCount() - 1);
        }

        databaseHelper.open();
        ArrayList<Job> jobs = new ArrayList<>();
        if (comparing_current) {
            jobs.add(databaseHelper.getCurrentJob(userid));
            Job job2Selected = databaseHelper.getJobOffer(userid, jobTags.get(0));
            if (job2Selected != null) {
                jobs.add(job2Selected);
            } else {
                jobs.add(databaseHelper.getJobOffer(userid, jobTags.get(1)));
            }
        } else {
            jobs.add(databaseHelper.getJobOffer(userid, jobTags.get(0)));
            jobs.add(databaseHelper.getJobOffer(userid, jobTags.get(1)));
        }

        ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(userid);
        jobs.sort(new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                Calculator calculator = new Calculator(comparisonSettings);
                Location location1 = databaseHelper.getLocation(job1.getLocationId());
                Location location2 = databaseHelper.getLocation(job2.getLocationId());
                return Double.compare(calculator.calculateJobScore(job2, location2),
                        calculator.calculateJobScore(job1, location1));
            }
        });
        Calculator calculator = new Calculator(comparisonSettings);
        Location location = databaseHelper.getLocation(jobs.get(0).getLocationId());
        job1Details[0] = jobs.get(0).getTitle();
        job1Details[1] = jobs.get(0).getCompany();
        job1Details[2] = location.getCity() + ", " + location.getState();
        job1Details[3] = String.valueOf(calculator.calculateAYS(jobs.get(0), location));
        job1Details[4] = String.valueOf(calculator.calculateAYB(jobs.get(0), location));
        job1Details[5] = String.valueOf(jobs.get(0).getRetirementPercentage());
        job1Details[6] = String.valueOf(jobs.get(0).getRestrictedStock());
        job1Details[7] = String.valueOf(jobs.get(0).getLearningDevBudget());
        job1Details[8] = String.valueOf(jobs.get(0).getFamilyAssistancePlanning());

        location = databaseHelper.getLocation(jobs.get(1).getLocationId());
        job2Details[0] = jobs.get(1).getTitle();
        job2Details[1] = jobs.get(1).getCompany();
        job2Details[2] = location.getCity() + ", " + location.getState();
        job2Details[3] = String.valueOf(calculator.calculateAYS(jobs.get(1), location));
        job2Details[4] = String.valueOf(calculator.calculateAYB(jobs.get(1), location));
        job2Details[5] = String.valueOf(jobs.get(1).getRetirementPercentage());
        job2Details[6] = String.valueOf(jobs.get(1).getRestrictedStock());
        job2Details[7] = String.valueOf(jobs.get(1).getLearningDevBudget());
        job2Details[8] = String.valueOf(jobs.get(1).getFamilyAssistancePlanning());

        databaseHelper.close();

        // Update the Job1 and Job2 headers to reflect their title and company
        TextView job1Header = findViewById(R.id.job1_header);
        TextView job2Header = findViewById(R.id.job2_header);

        job1Header.setText(jobs.get(0).getTitle());
        job2Header.setText(jobs.get(1).getTitle());


        for (int i = 0; i < job1Details.length; i++) {
            TableRow row = new TableRow(this);

            // Create and add the row header
            TextView headerText = new TextView(this);
            headerText.setText(rowHeaders[i]);
            headerText.setPadding(16, 16, 16, 16);
            headerText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            // Create and add the Job1 details
            TextView job1Text = new TextView(this);
            job1Text.setText(job1Details[i]);
            job1Text.setPadding(16, 16, 16, 16);
            job1Text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            // Create and add the Job2 details
            TextView job2Text = new TextView(this);
            job2Text.setText(job2Details[i]);
            job2Text.setPadding(16, 16, 16, 16);
            job2Text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            // Add header, Job1, and Job2 details to the row
            row.addView(headerText);
            row.addView(job1Text);
            row.addView(job2Text);

            // Add the row to the table
            comparisonTable.addView(row);
        }
    }

}
