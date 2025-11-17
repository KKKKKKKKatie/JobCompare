package edu.gatech.seclass.jobcompare6300.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;
import edu.gatech.seclass.jobcompare6300.util.Calculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompareJobOffers extends AppCompatActivity {

    private ListView jobOfferListView;
    private Button compareButton, cancelButton;
    private Map<String, Integer> allJobs;
    private ArrayAdapter<String> adapter;

    private boolean enableCompare = true;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_job_offers);

        jobOfferListView = findViewById(R.id.job_offer_list);
        compareButton = findViewById(R.id.btn_compare_jobs);
        cancelButton = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();

        // Retrieve the data from the intent
        int userId = intent.getIntExtra("user_id", -1);

        allJobs = new LinkedHashMap<>();

        databaseHelper.open();
        ArrayList<Job> jobs = databaseHelper.getAllUserJobs(userId);
        enableCompare = true;
        if(jobs.size() <= 0) {
            Toast.makeText(CompareJobOffers.this, "There are no jobs to compare", Toast.LENGTH_SHORT).show();
            enableCompare = false;
        }
        if(enableCompare) {
            ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(userId);
            Calculator calculator = new Calculator(comparisonSettings);
            jobs.sort((job1, job2) -> {
                // Fetch locations for both jobs
                Location location1 = databaseHelper.getLocation(job1.getLocationId());
                Location location2 = databaseHelper.getLocation(job2.getLocationId());

                // Calculate scores for both jobs
                double score1 = calculator.calculateJobScore(job1, location1);
                double score2 = calculator.calculateJobScore(job2, location2);

                // Print out the scores for debugging
//                System.out.println(job1.getJobType() + " (ID: " + job1.getJobId() + ") Score: " + score1);
//                System.out.println(job2.getJobType() + " (ID: " + job2.getJobId() + ") Score: " + score2);

                // Compare scores in descending order (higher score first)
                return Double.compare(score2, score1);
            });

            // Print job IDs and their final scores after sorting
            for (Job job : jobs) {
                Location location = databaseHelper.getLocation(job.getLocationId());
                double score = calculator.calculateJobScore(job, location);
                // System.out.println(job.getJobType() + " (ID: " + job.getJobId() + ") Final Score: " + score);
            }

            // Now fill the allJobs Map with the sorted jobs in the same order as sorted `jobs`
            for (Job job : jobs) {
                StringBuilder current_job_identifier = new StringBuilder();
                String key;
                if (job.getJobType() == Job.JobType.CURRENT) {
                    current_job_identifier.append("*CURRENT JOB* ");
                    key = "current-" + job.getJobId();
                } else {
                    key = "offer-" + job.getJobId();
                }
                // Populate allJobs with the same order as `jobs`
                allJobs.put(current_job_identifier + job.getTitle() + " at " + job.getCompany(), job.getJobId());
            }
        }


       databaseHelper.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice,
                new ArrayList<>(allJobs.keySet()));
        jobOfferListView.setAdapter(adapter);
        jobOfferListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AtomicBoolean comparing_current = new AtomicBoolean(false);


        compareButton.setOnClickListener(v -> {
            if(enableCompare && jobs.size() >= 2) {
                ArrayList<Job> selectJobObjects = new ArrayList<>();
                int listViewCount = jobOfferListView.getCount();

                // Iterate through the ListView and jobTags map
                for (int i = 0; i < listViewCount; i++) {
                    if (jobOfferListView.isItemChecked(i)) {
                        String jobDisplayText = (String) jobOfferListView.getItemAtPosition(i);
                        Integer jobId = allJobs.get(jobDisplayText);
                        databaseHelper.open();
                        if (jobDisplayText.contains("*CURRENT JOB*")) {
                            comparing_current.set(true);

                            selectJobObjects.add(databaseHelper.getCurrentJob(userId));
                        } else {
                            selectJobObjects.add(databaseHelper.getJobOffer(userId, jobId));
                        }
                        databaseHelper.close();

                    }
                }
                if (selectJobObjects.size() == 2) {
                    ArrayList<Integer> selectedJobs = new ArrayList<>();
                    if ((comparing_current.get())) {
                        for (Job job : selectJobObjects) {
                            if (job.getJobType() != Job.JobType.CURRENT) {
                                selectedJobs.add(job.getJobId());
                            }
                        }
                    } else {
                        selectedJobs.add(selectJobObjects.get(0).getJobId());
                        selectedJobs.add(selectJobObjects.get(1).getJobId());
                    }

    //                if (selectJobObjects.size() == 2) {
                        // Pass the selected jobs to the ComparingTwoJobs activity
                        Intent selectedJobIntent = new Intent(CompareJobOffers.this, ComparingTwoJobs.class);
                        selectedJobIntent.putExtra("selectedJobs", selectedJobs);
                        selectedJobIntent.putExtra("user_id", userId);
                        // System.out.println("comparing_current after selection is " +
                        // comparing_current.get());
                        selectedJobIntent.putExtra("compare_current", comparing_current.get());
                        startActivity(selectedJobIntent);
                } else {
                    Toast.makeText(CompareJobOffers.this, "Please select two jobs to compare", Toast.LENGTH_SHORT).show();
                }
            } // end of enable compare
            else if(jobs.size() == 0){
                Toast.makeText(CompareJobOffers.this, "There are no jobs to compare", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CompareJobOffers.this, "There is only 1 job", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener( v-> {
            Intent mainMenuIntent = new Intent(CompareJobOffers.this, MainMenu.class);
            mainMenuIntent.putExtra("user_id", userId);
            startActivity(mainMenuIntent);

        });
    }
}
