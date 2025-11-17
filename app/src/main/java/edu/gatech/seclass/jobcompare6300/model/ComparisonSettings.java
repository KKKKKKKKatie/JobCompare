package edu.gatech.seclass.jobcompare6300.model;

public class ComparisonSettings {


    private int settingsId;
    private int salaryWeight = 1;
    private int bonusWeight = 1;
    private int retirementWeight = 1;
    private int stockWeight = 1;
    private int learningDevWeight = 1;
    private int familyPlanningWeight = 1;
    private int user_id;

    public ComparisonSettings(){}

    public ComparisonSettings(int salaryWeight, int bonusWeight, int retirementWeight, int stockWeight, int learningDevWeight, int familyPlanningWeight) {
        this.salaryWeight = salaryWeight;
        this.bonusWeight = bonusWeight;
        this.retirementWeight = retirementWeight;
        this.stockWeight = stockWeight;
        this.learningDevWeight = learningDevWeight;
        this.familyPlanningWeight = familyPlanningWeight;
    }

    public ComparisonSettings(int settingsId, int salaryWeight, int bonusWeight, int retirementWeight, int stockWeight, int learningDevWeight, int familyPlanningWeight, int user_id) {
        this.settingsId = settingsId;
        this.salaryWeight = salaryWeight;
        this.bonusWeight = bonusWeight;
        this.retirementWeight = retirementWeight;
        this.stockWeight = stockWeight;
        this.learningDevWeight = learningDevWeight;
        this.familyPlanningWeight = familyPlanningWeight;
        this.user_id = user_id;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public int getSalaryWeight() {
        return salaryWeight;
    }

    public void setSalaryWeight(int salaryWeight) {
        this.salaryWeight = salaryWeight;
    }

    public int getBonusWeight() {
        return bonusWeight;
    }

    public void setBonusWeight(int bonusWeight) {
        this.bonusWeight = bonusWeight;
    }

    public int getRetirementWeight() {
        return retirementWeight;
    }

    public void setRetirementWeight(int retirementWeight) {
        this.retirementWeight = retirementWeight;
    }

    public int getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(int stockWeight) {
        this.stockWeight = stockWeight;
    }

    public int getLearningDevWeight() {
        return learningDevWeight;
    }

    public void setLearningDevWeight(int learningDevWeight) {
        this.learningDevWeight = learningDevWeight;
    }

    public int getFamilyPlanningWeight() {
        return familyPlanningWeight;
    }

    public void setFamilyPlanningWeight(int familyPlanningWeight) {
        this.familyPlanningWeight = familyPlanningWeight;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
