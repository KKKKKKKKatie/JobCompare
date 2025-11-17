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

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;

public class AdjustComparisonSettings extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    EditText salaryWeightInput, bonusWeightInput, retirementWeightInput, stockWeightInput, learningDevWeightInput,
            familyPlanningWeightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_comparison_settings);

        // Initialize EditText fields
        salaryWeightInput = findViewById(R.id.input_salary_weight);
        bonusWeightInput = findViewById(R.id.input_bonus_weight);
        retirementWeightInput = findViewById(R.id.input_retirement_weight);
        stockWeightInput = findViewById(R.id.input_stock_witght);
        learningDevWeightInput = findViewById(R.id.input_learning_dev_weight);
        familyPlanningWeightInput = findViewById(R.id.input_family_planning_wight);

        Button saveButton = findViewById(R.id.btn_save_comparison_settings);
        Button cancelButton = findViewById(R.id.btn_cancel_comparison_settings);

        Intent intent = getIntent();

        int userId = intent.getIntExtra("user_id", -1);

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();

        ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(userId);
        databaseHelper.close();

        salaryWeightInput.setText(String.valueOf(comparisonSettings.getSalaryWeight()));
        bonusWeightInput.setText(String.valueOf(comparisonSettings.getBonusWeight()));
        retirementWeightInput.setText(String.valueOf(comparisonSettings.getRetirementWeight()));
        stockWeightInput.setText(String.valueOf(comparisonSettings.getStockWeight()));
        learningDevWeightInput.setText(String.valueOf(comparisonSettings.getLearningDevWeight()));
        familyPlanningWeightInput.setText(String.valueOf(comparisonSettings.getFamilyPlanningWeight()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean navigate = false;
                if (!validateInputs()) {
                    // System.out.println("invalid inputs");
                    CharSequence charSeq = "Please correct the errors";
                    Toast.makeText(AdjustComparisonSettings.this, charSeq, Toast.LENGTH_SHORT).show();
                } else {
                    int salaryWeight = Integer.parseInt(salaryWeightInput.getText().toString());
                    int bonusWeight = Integer.parseInt(bonusWeightInput.getText().toString());
                    int retirementWeight = Integer.parseInt(retirementWeightInput.getText().toString());
                    int stockWeight = Integer.parseInt(stockWeightInput.getText().toString());
                    int learningDevWeight = Integer.parseInt(learningDevWeightInput.getText().toString());
                    int familyPlanningWeightWeight = Integer.parseInt(familyPlanningWeightInput.getText().toString());

                    ComparisonSettings new_settings = new ComparisonSettings(0, salaryWeight, bonusWeight,
                            retirementWeight, stockWeight, learningDevWeight, familyPlanningWeightWeight, userId);

                    databaseHelper.open();
                    int return_val = databaseHelper.upsertSettings(new_settings);
                    databaseHelper.close();
                    if (return_val == -1) {
                        CharSequence charSeq = "Failed to update comparison settings";
                        Toast.makeText(AdjustComparisonSettings.this, charSeq, Toast.LENGTH_SHORT).show();
                    } else {
                        navigate = true;
                    }
                }

                if (navigate) {
                    Intent intent = new Intent(AdjustComparisonSettings.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to MainMenu
                Intent intent = new Intent(AdjustComparisonSettings.this, MainMenu.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean validateInputs() {
        String salaryWeightStr = salaryWeightInput.getText().toString();
        String bonusWeightStr = bonusWeightInput.getText().toString();
        String retirementWeightStr = retirementWeightInput.getText().toString();
        String stockWeightStr = stockWeightInput.getText().toString();
        String learningDevWeightStr = learningDevWeightInput.getText().toString();
        String familyPlanningWeightStr = familyPlanningWeightInput.getText().toString();

        boolean ret = true;

        // Validate each input to ensure it is an integer between 0 and 9
        if (!isValidWeight(salaryWeightStr)) {
            salaryWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        if (!isValidWeight(bonusWeightStr)) {
            bonusWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        if (!isValidWeight(retirementWeightStr)) {
            retirementWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        if (!isValidWeight(stockWeightStr)) {
            stockWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        if (!isValidWeight(learningDevWeightStr)) {
            learningDevWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        if (!isValidWeight(familyPlanningWeightStr)) {
            familyPlanningWeightInput.setError("Enter an integer between 0 and 9");
            ret = false;
        }
        return ret;
    }

    private boolean isValidWeight(String weightStr) {
        try {
            int weight = Integer.parseInt(weightStr);
            return weight >= 0 && weight <= 9;
        } catch (NumberFormatException e) {
            return false; // Input was not a valid integer
        }
    }
}