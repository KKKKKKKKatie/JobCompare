package edu.gatech.seclass.jobcompare6300.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;

public class Calculator {
    private final ComparisonSettings comparisonSettings;
    public Calculator(ComparisonSettings comparisonSettings){
        this.comparisonSettings = comparisonSettings;
    }
    public double calculateAYS(Job job, Location location) {
        // AYS = YS * 100 / INDEX
        double ays = job.getYearlySalary() * 100 / location.getCostOfLivingIndex();
        BigDecimal bd = new BigDecimal(Double.toString(ays));
        bd = bd.setScale(2, RoundingMode.HALF_UP);  // Rounds to 2 decimal places
        return bd.doubleValue();
    }

    public double calculateAYB(Job job, Location location) {
        // AYB = YB * 100 / INDEX
        double ayb = job.getYearlyBonus() * 100 / location.getCostOfLivingIndex();
        BigDecimal bd = new BigDecimal(Double.toString(ayb));
        bd = bd.setScale(2, RoundingMode.HALF_UP);  // Rounds to 2 decimal places
        return bd.doubleValue();
    }

    public double calculateJobScore(Job job, Location location) {
        // n/9 * AYS + n/9 * AYB + n/9 * (R * AYS / 100) + n/9 * (RSUA/3) + n/9 * PLD + n/9 * FPA
        double AYS = calculateAYS(job, location);
        double AYB = calculateAYB(job, location);
        double totalWeight = comparisonSettings.getSalaryWeight() +
                comparisonSettings.getBonusWeight()+
                comparisonSettings.getRetirementWeight() +
                comparisonSettings.getStockWeight() +
                +comparisonSettings.getLearningDevWeight() +
                +comparisonSettings.getFamilyPlanningWeight();

        if (totalWeight == 0) {
            // Apply default weights, for example, setting all to 1
            comparisonSettings.setSalaryWeight(1);
            comparisonSettings.setBonusWeight(1);
            comparisonSettings.setRetirementWeight(1);
            comparisonSettings.setStockWeight(1);
            comparisonSettings.setLearningDevWeight(1);
            comparisonSettings.setFamilyPlanningWeight(1);

            // Recalculate total weight with default values
            totalWeight = 6; // Since all weights are now 1
        }

        double ranking = (double) (comparisonSettings.getSalaryWeight() /totalWeight) * AYS +
                (double) (comparisonSettings.getBonusWeight() /totalWeight) * AYB +
                (double) (comparisonSettings.getRetirementWeight() /totalWeight) * (job.getRetirementPercentage() * AYS / 100) +
                (double) (comparisonSettings.getStockWeight() /totalWeight) * (job.getRestrictedStock()/3) +
                (double) (comparisonSettings.getLearningDevWeight() /totalWeight) * job.getLearningDevBudget() +
                (double) (comparisonSettings.getFamilyPlanningWeight() /totalWeight) * job.getFamilyAssistancePlanning();
//        System.out.println("job type:" + job.getJobType() + " job_id" + job.getJobId() + " total weights:  " + totalWeight + " ranking:" + ranking);
        return ranking;
    }
}
