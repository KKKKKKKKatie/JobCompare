package edu.gatech.seclass.jobcompare6300.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.BigInteger;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;

public class UpdateCurrentJob extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    boolean has_current_job = false;
    EditText titleInput, companyInput, cityInput, stateInput, costOfLivingInput, yearlySalaryInput, yearlyBonusInput,
            retirementInput, restrictedStockInput, learningDevInput, familyAssistanceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_current_job);

        // edit fields
        titleInput = findViewById(R.id.input_title);
        companyInput = findViewById(R.id.input_company);
        cityInput = findViewById(R.id.input_location_city);
        stateInput = findViewById(R.id.input_location_state);
        costOfLivingInput = findViewById(R.id.input_cost_of_living);
        yearlySalaryInput = findViewById(R.id.input_yearly_salary);
        yearlyBonusInput = findViewById(R.id.input_yearly_bonus);
        retirementInput = findViewById(R.id.input_retirement);
        restrictedStockInput = findViewById(R.id.input_restricted_stock);
        learningDevInput = findViewById(R.id.input_learning_dev);
        familyAssistanceInput = findViewById(R.id.input_fam_assistance);

        // button
        Button saveButton = findViewById(R.id.btn_save);
        Button cancelButton = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();

        // Retrieve the data from the intent
        int userId = intent.getIntExtra("user_id", -1);
        // System.out.println("In update current job. User: " + userId);

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();
        Job job = databaseHelper.getCurrentJob(userId);
        databaseHelper.close();
        if (job == null) {
            has_current_job = false;
            // System.out.println("the user has no current job");
        } else {
            has_current_job = true;
            // System.out.println("the user has a current job");
            titleInput.setText(job.getTitle());
            companyInput.setText(job.getCompany());
            databaseHelper.open();
            Location location = databaseHelper.getLocation(job.getLocationId());
            databaseHelper.close();
            cityInput.setText(location.getCity());
            stateInput.setText(location.getState());
            costOfLivingInput.setText(String.valueOf(location.getCostOfLivingIndex()));
            yearlySalaryInput.setText(String.valueOf(job.getYearlySalary()));
            yearlyBonusInput.setText(String.valueOf(job.getYearlyBonus()));
            retirementInput.setText(String.valueOf(job.getRetirementPercentage()));
            restrictedStockInput.setText(String.valueOf(job.getRestrictedStock()));
            learningDevInput.setText(String.valueOf(job.getLearningDevBudget()));
            familyAssistanceInput.setText(String.valueOf(job.getFamilyAssistancePlanning()));

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean navigate = false;
                // Get all fields values
                if (!validateInputs()) {
                    // System.out.println("invalid inputs");
                    CharSequence charSeq = "Please correct the errors";
                    Toast.makeText(UpdateCurrentJob.this, charSeq, Toast.LENGTH_SHORT).show();
                } else {
                    String title = titleInput.getText().toString();
                    String company = companyInput.getText().toString();
                    String city = cityInput.getText().toString();
                    String state = stateInput.getText().toString();
                    int costOfLiving = Integer.parseInt(costOfLivingInput.getText().toString());
                    double yearlySalary = Double.parseDouble(yearlySalaryInput.getText().toString());
                    double yearlyBonus = Double.parseDouble(yearlyBonusInput.getText().toString());
                    int retirementPercentage = Integer.parseInt(retirementInput.getText().toString());
                    double restrictedStock = Double.parseDouble(restrictedStockInput.getText().toString());
                    double learningDevBudget = Double.parseDouble(learningDevInput.getText().toString());
                    double familyAssistance = Double.parseDouble(familyAssistanceInput.getText().toString());

                    Job current_job = new Job();
                    current_job.setTitle(title);
                    current_job.setCompany(company);
                    current_job.setYearlySalary(yearlySalary);
                    current_job.setYearlyBonus(yearlyBonus);
                    current_job.setRetirementPercentage(retirementPercentage);
                    current_job.setRestrictedStock(restrictedStock);
                    current_job.setLearningDevBudget(learningDevBudget);
                    current_job.setFamilyAssistancePlanning(familyAssistance);
                    current_job.setUserId(userId);
                    current_job.setJobType(Job.JobType.CURRENT);

                    Location jobs_location = new Location();
                    jobs_location.setCity(city);
                    jobs_location.setState(state);
                    jobs_location.setCostOfLivingIndex(costOfLiving);

                    if (has_current_job) {
                        // System.out.println("updating user's current job");
                        databaseHelper.open();
                        int return_val = (int) databaseHelper.updateCurrentJob(current_job, jobs_location, userId);
                        databaseHelper.close();
                        if (return_val == -1) {
                            CharSequence charSeq = "Failed to update current job";
                            Toast.makeText(UpdateCurrentJob.this, charSeq, Toast.LENGTH_SHORT).show();
                        } else {
                            navigate = true;
                        }
                    } else {
                        // System.out.println("inserting a user's current job");
                        databaseHelper.open();
                        int return_val = (int) databaseHelper.insertCurrentJob(current_job, jobs_location, userId);
                        databaseHelper.close();
                        if (return_val == -1) {
                            CharSequence charSeq = "Failed to update current job";
                            Toast.makeText(UpdateCurrentJob.this, charSeq, Toast.LENGTH_SHORT).show();
                        } else {
                            navigate = true;
                        }
                    }
                }
                if (navigate) {
                    Intent intent = new Intent(UpdateCurrentJob.this, MainMenu.class);
                    startActivity(intent); // Start the new Activity
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to MainMenu
                Intent intent = new Intent(UpdateCurrentJob.this, MainMenu.class);
                startActivity(intent); // Start the new Activity
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // validate data
    public boolean validateInputs() {
        boolean isValid = true;

        // Validate title, company, city, and state (should not be empty)
        String title = titleInput.getText().toString();
        String company = companyInput.getText().toString();
        if (title.isEmpty()) {
            titleInput.setError("Title is required");
            isValid = false;
        }
        if (company.isEmpty()) {
            companyInput.setError("Company is required");
            isValid = false;
        }

        String city = cityInput.getText().toString(); // ensure city is not empty
        if (city.isEmpty()) {
            cityInput.setError("City is required");
            isValid = false;
        }

        String state = stateInput.getText().toString(); // ensure state is not empty
        if (state.isEmpty()) {
            stateInput.setError("State is required");
            isValid = false;
        }

        int livingCost;
        try {
            livingCost = Integer.parseInt(costOfLivingInput.getText().toString());
            if (livingCost < 0) {
                costOfLivingInput.setError("Living cost must greater than or equal to 0");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            // Check if the input was not a number or out of bounds for an integer

            if (costOfLivingInput.getText().toString().matches(".*\\D.*")) {
                // If it contains non-digit characters
                costOfLivingInput.setError("Cost of living must be a valid number.");
            } else {
                try {
                    // Try to parse as BigInteger to check if it's out of bounds for int
                    new BigInteger(costOfLivingInput.getText().toString());
                    // If we get here, it's out of bounds for int, but still a valid number
                    costOfLivingInput.setError("Cost of living is too large. Please enter a smaller number.");
                } catch (NumberFormatException nfe) {
                    // This handles any unexpected issue where BigInteger cannot parse the input
                    costOfLivingInput.setError("Cost of living must be a valid number.");
                }


            }
            isValid = false;
        }

        // Validate yearly_salary
        double yearlySalary = 0;
        try {
            yearlySalary = Double.parseDouble(yearlySalaryInput.getText().toString());

            //  Check to ensure the salary is not negative and doesn't go beyond double bounds
            if (yearlySalary == Double.POSITIVE_INFINITY || yearlySalary == Double.NEGATIVE_INFINITY) {
                yearlySalaryInput.setError("Yearly salary is too large.");
                isValid = false;
            } else if (yearlySalary < 0) {
                yearlySalaryInput.setError("Yearly salary must be greater than or equal to 0.");
                isValid = false;
            }

        } catch (NumberFormatException e) {
            // This handles cases where BigDecimal cannot parse the input
            yearlySalaryInput.setError("Yearly salary must be a valid number.");
            isValid = false;
        }


        // Validate yearly_bonus
        double yearlyBonus = 0;
        try {
            yearlyBonus = Double.parseDouble(yearlyBonusInput.getText().toString());

            // Additional check to ensure the bonus is not negative and doesn't go beyond double bounds
            if (yearlyBonus == Double.POSITIVE_INFINITY || yearlyBonus == Double.NEGATIVE_INFINITY) {
                yearlyBonusInput.setError("Yearly bonus is too large.");
                isValid = false;
            } else if (yearlyBonus < 0) {
                yearlyBonusInput.setError("Yearly bonus must be greater than or equal to 0.");
                isValid = false;
            }

        } catch (NumberFormatException e) {
            yearlyBonusInput.setError("Yearly bonus must be a valid number.");
            isValid = false;
        }


        // Validate retirement_percentage (should be between 0 and 100)
        int retirementPercentage;
        try {
            retirementPercentage = Integer.parseInt(retirementInput.getText().toString());
            if (retirementPercentage < 0 || retirementPercentage > 100) {
                retirementInput.setError("Retirement percentage must be between 0 and 100");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            retirementInput.setError("Retirement percentage must be a number");
            isValid = false;
        }

        // Validate restricted_stock
        double restrictedStock;
        try {
            // Attempt to parse the input as a double
            restrictedStock = Double.parseDouble(restrictedStockInput.getText().toString());

            // Check if the restricted stock value is negative and doesn't go beyond double bounds
            if (restrictedStock == Double.POSITIVE_INFINITY || restrictedStock == Double.NEGATIVE_INFINITY) {
                restrictedStockInput.setError("Restricted stock is too large.");
                isValid = false;
            } else if (restrictedStock < 0) {
                restrictedStockInput.setError("Restricted stock must be greater than or equal to 0.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            restrictedStockInput.setError("Restricted stock must be a valid number.");
            isValid = false;
        }


        // Validate learning_dev_budget (must be between 0 and 18,000)
        double learningDevBudget;
        try {
            learningDevBudget = Double.parseDouble(learningDevInput.getText().toString());
            if (learningDevBudget < 0 || learningDevBudget > 18000) {
                learningDevInput.setError("Learning Dev Budget must be between 0 and 18,000");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            learningDevInput.setError("Learning Dev Budget must be a number");
            isValid = false;
        }

        // Validate family_assistance_planning (must be between 0 and 12% of yearly
        // salary)
        double familyAssistance;
        try {
            familyAssistance = Double.parseDouble(familyAssistanceInput.getText().toString());

            double maxFamilyAssistance = yearlySalary * 0.12;
            if(yearlySalaryInput.getText().toString().isEmpty()){
                familyAssistanceInput.setError("Yearly Salary must be inputted.");
                isValid = false;
            }
            else {
                if (familyAssistance < 0 || familyAssistance > maxFamilyAssistance) {
                    familyAssistanceInput.setError("Family Assistance must be between 0 and " + maxFamilyAssistance);
                    isValid = false;
                }
            }
        } catch (NumberFormatException e) {
            familyAssistanceInput.setError("Family Assistance must be a number");
            isValid = false;
        }

        return isValid;
    }

}