package edu.gatech.seclass.jobcompare6300.model;

public class Job {
    public enum JobType {
        CURRENT,
        OFFER
    }

    private int jobId = -1;
    private String title;
    private String company;
    private int locationId = -1;
    private double yearlySalary;
    private double yearlyBonus;
    private int retirementPercentage;
    private double restrictedStock;
    private double learningDevBudget;
    private double familyAssistancePlanning;
    private JobType jobType; // Enum field to specify job type
    private int userId; //for job offers

    public Job(){}

    public Job(int jobId, String title, String company, int locationId,
               double yearlySalary, double yearlyBonus, int retirementPercentage,
               double restrictedStock, double learningDevBudget,
               double familyAssistancePlanning, JobType jobType, int user_id) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.locationId = locationId;
        this.yearlySalary = yearlySalary;
        this.yearlyBonus = yearlyBonus;
        this.retirementPercentage = retirementPercentage;
        this.restrictedStock = restrictedStock;
        this.learningDevBudget = learningDevBudget;
        this.familyAssistancePlanning = familyAssistancePlanning;
        this.jobType = jobType;
        this.userId = user_id;
    }

    public int getUserId(){
        return userId;
    }

    public int getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public int getLocationId() {
        return locationId;
    }

    public double getYearlySalary() {
        return yearlySalary;
    }

    public double getYearlyBonus() {
        return yearlyBonus;
    }

    public int getRetirementPercentage() {
        return retirementPercentage;
    }

    public double getRestrictedStock() {
        return restrictedStock;
    }

    public double getLearningDevBudget() {
        return learningDevBudget;
    }

    public double getFamilyAssistancePlanning() {
        return familyAssistancePlanning;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setYearlySalary(double yearlySalary) {
        this.yearlySalary = yearlySalary;
    }

    public void setYearlyBonus(double yearlyBonus) {
        this.yearlyBonus = yearlyBonus;
    }

    public void setRetirementPercentage(int retirementPercentage) {
        this.retirementPercentage = retirementPercentage;
    }

    public void setRestrictedStock(double restrictedStock) {
        this.restrictedStock = restrictedStock;
    }

    public void setLearningDevBudget(double learningDevBudget) {
        this.learningDevBudget = learningDevBudget;
    }

    public void setFamilyAssistancePlanning(double familyAssistancePlanning) {
        this.familyAssistancePlanning = familyAssistancePlanning;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }
}
