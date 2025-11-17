package edu.gatech.seclass.jobcompare6300;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.database.DatabaseHelper;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;
import edu.gatech.seclass.jobcompare6300.model.User;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        database = databaseHelper.getDatabase();
        databaseHelper.onUpgrade(database, 1, 1);
//        databaseHelper.onCreate(database);  // Ensure tables are created
    }

    @After
    public void tearDown() {
        databaseHelper.close();
        databaseHelper = null;
    }

    @Test
    public void test_getSingularUser(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        assertNotNull(user);
        assertTrue(user.getUserId() != -1);
        databaseHelper.close();

    }

    @Test
    public void test_getUserSettingsNotSet(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(user.getUserId());
        assertNotNull(comparisonSettings);
        databaseHelper.close();
    }

    @Test
    public void test_upsertSettingsInsert(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(user.getUserId());
        assertNotNull(comparisonSettings);
        ComparisonSettings updateSettings = new ComparisonSettings(5, 5, 5, 5, 5, 5);
        updateSettings.setUser_id(user.getUserId());
        int return_val = databaseHelper.upsertSettings(updateSettings);
        assertTrue(return_val >= 0);
        ComparisonSettings actual_settings = databaseHelper.getUserSettings(user.getUserId());
        assertEquals(5, actual_settings.getSalaryWeight());
        assertEquals(5, actual_settings.getBonusWeight());
        assertEquals(5, actual_settings.getRetirementWeight());
        assertEquals(5, actual_settings.getStockWeight());
        assertEquals(5, actual_settings.getLearningDevWeight());
        assertEquals(5, actual_settings.getFamilyPlanningWeight());
        assertEquals(user.getUserId(), actual_settings.getUser_id());
        databaseHelper.close();
    }

    @Test
    public void test_upsertSettingsUpdate(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        ComparisonSettings comparisonSettings = databaseHelper.getUserSettings(user.getUserId());
        assertNotNull(comparisonSettings);
        ComparisonSettings updateSettings = new ComparisonSettings(5, 5, 5, 5, 5, 5);
        updateSettings.setUser_id(user.getUserId());
        int return_val = databaseHelper.upsertSettings(updateSettings);
        assertTrue(return_val >= 0);
        ComparisonSettings updateSettings2 = new ComparisonSettings(6, 6, 6, 6, 6, 6);
        updateSettings2.setUser_id(user.getUserId());
        return_val = databaseHelper.upsertSettings(updateSettings2);
        assertEquals(1, return_val);
        ComparisonSettings actual_settings = databaseHelper.getUserSettings(user.getUserId());
        assertEquals(6, actual_settings.getSalaryWeight());
        assertEquals(6, actual_settings.getBonusWeight());
        assertEquals(6, actual_settings.getRetirementWeight());
        assertEquals(6, actual_settings.getStockWeight());
        assertEquals(6, actual_settings.getLearningDevWeight());
        assertEquals(6, actual_settings.getFamilyPlanningWeight());
        assertEquals(user.getUserId(), actual_settings.getUser_id());
        databaseHelper.close();
    }

    @Test
    public void test_insertCurrentJob(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job current_job = new Job();
        current_job.setUserId(user.getUserId());
        current_job.setTitle("Dev");
        current_job.setCompany("Sample Company");
        current_job.setYearlySalary(180000.99);
        current_job.setYearlyBonus(9999);
        current_job.setRetirementPercentage(60);
        current_job.setRestrictedStock(10.8);
        current_job.setLearningDevBudget(16000);
        current_job.setFamilyAssistancePlanning(2000);
        current_job.setJobType(Job.JobType.CURRENT);
        int return_val = (int)databaseHelper.insertCurrentJob(current_job, location, user.getUserId());
        assertTrue(return_val>0);
        databaseHelper.close();
    }

    @Test
    public void test_getCurrentJob(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job current_job = new Job();
        current_job.setUserId(user.getUserId());
        current_job.setTitle("Dev");
        current_job.setCompany("Sample Company");
        current_job.setYearlySalary(180000.99);
        current_job.setYearlyBonus(9999);
        current_job.setRetirementPercentage(60);
        current_job.setRestrictedStock(10.8);
        current_job.setLearningDevBudget(16000);
        current_job.setFamilyAssistancePlanning(2000);
        current_job.setJobType(Job.JobType.CURRENT);
        int return_val = (int)databaseHelper.insertCurrentJob(current_job, location, user.getUserId());
        assertTrue(return_val>0);
        Job job_for_user = databaseHelper.getCurrentJob(user.getUserId());
        assertEquals("Dev", job_for_user.getTitle());
        assertEquals("Sample Company", job_for_user.getCompany());
        assertEquals(180000.99, job_for_user.getYearlySalary(), .001);
        assertEquals(9999, job_for_user.getYearlyBonus(), .001);
        assertEquals(60, job_for_user.getRetirementPercentage());
        assertEquals(10.8, job_for_user.getRestrictedStock(), .001);
        assertEquals(16000, job_for_user.getLearningDevBudget(), .001);
        assertEquals(2000, job_for_user.getFamilyAssistancePlanning(), .001);
        assertEquals(Job.JobType.CURRENT, job_for_user.getJobType());
        assertEquals(location.getLocationId(), job_for_user.getLocationId());
        Location job_location = databaseHelper.getLocation(job_for_user.getLocationId());
        assertEquals("San Francisco", job_location.getCity());
        assertEquals("CA", job_location.getState());
        assertEquals(10, job_location.getCostOfLivingIndex());
        databaseHelper.close();
    }

    @Test
    public void test_updateCurrentJob(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job current_job = new Job();
        current_job.setUserId(user.getUserId());
        current_job.setTitle("Dev");
        current_job.setCompany("Sample Company");
        current_job.setYearlySalary(180000.99);
        current_job.setYearlyBonus(9999);
        current_job.setRetirementPercentage(60);
        current_job.setRestrictedStock(10.8);
        current_job.setLearningDevBudget(16000);
        current_job.setFamilyAssistancePlanning(2000);
        current_job.setJobType(Job.JobType.CURRENT);
        int return_val = (int)databaseHelper.insertCurrentJob(current_job, location, user.getUserId());
        assertTrue(return_val>0);
        Location location2 = new Location();
        location2.setCity("San Francisco");
        location2.setState("CA");
        location2.setCostOfLivingIndex(10);
        Job current_job2 = new Job();
        current_job2.setUserId(user.getUserId());
        current_job2.setTitle("Dev2");
        current_job2.setCompany("Sample Company2");
        current_job2.setYearlySalary(180000.99);
        current_job2.setYearlyBonus(9999);
        current_job2.setRetirementPercentage(60);
        current_job2.setRestrictedStock(10.8);
        current_job2.setLearningDevBudget(16000);
        current_job2.setFamilyAssistancePlanning(2000);
        current_job2.setJobType(Job.JobType.CURRENT);
        return_val = (int)databaseHelper.updateCurrentJob(current_job2, location2, user.getUserId());
        assertEquals(current_job2.getJobId(), return_val);
        Job job_for_user = databaseHelper.getCurrentJob(user.getUserId());
        assertEquals("Dev2", job_for_user.getTitle());
        assertEquals("Sample Company2", job_for_user.getCompany());
        assertEquals(180000.99, job_for_user.getYearlySalary(), .001);
        assertEquals(9999, job_for_user.getYearlyBonus(), .001);
        assertEquals(60, job_for_user.getRetirementPercentage());
        assertEquals(10.8, job_for_user.getRestrictedStock(), .001);
        assertEquals(16000, job_for_user.getLearningDevBudget(), .001);
        assertEquals(2000, job_for_user.getFamilyAssistancePlanning(), .001);
        assertEquals(Job.JobType.CURRENT, job_for_user.getJobType());
        assertEquals(location2.getLocationId(), job_for_user.getLocationId());
        Location job_location = databaseHelper.getLocation(job_for_user.getLocationId());
        assertEquals("San Francisco", job_location.getCity());
        assertEquals("CA", job_location.getState());
        assertEquals(10, job_location.getCostOfLivingIndex());
        databaseHelper.close();
    }

    @Test
    public void test_insertCurrentJobDouble(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job current_job = new Job();
        current_job.setUserId(user.getUserId());
        current_job.setTitle("Dev");
        current_job.setCompany("Sample Company");
        current_job.setYearlySalary(180000.99);
        current_job.setYearlyBonus(9999);
        current_job.setRetirementPercentage(60);
        current_job.setRestrictedStock(10.8);
        current_job.setLearningDevBudget(16000);
        current_job.setFamilyAssistancePlanning(2000);
        current_job.setJobType(Job.JobType.CURRENT);
        int return_val = (int)databaseHelper.insertCurrentJob(current_job, location, user.getUserId());
        assertTrue(return_val>0);
        Job current_job2 = new Job();
        current_job2.setUserId(user.getUserId());
        current_job2.setTitle("Dev2");
        current_job2.setCompany("Sample Company2");
        current_job2.setYearlySalary(180000.99);
        current_job2.setYearlyBonus(9999);
        current_job2.setRetirementPercentage(60);
        current_job2.setRestrictedStock(10.8);
        current_job2.setLearningDevBudget(16000);
        current_job2.setFamilyAssistancePlanning(2000);
        current_job2.setJobType(Job.JobType.CURRENT);
        return_val = (int)databaseHelper.insertCurrentJob(current_job2, location, user.getUserId());
        assertEquals(-1, return_val);
        Job job_for_user = databaseHelper.getCurrentJob(user.getUserId());
        assertEquals("Dev", job_for_user.getTitle());
        assertEquals("Sample Company", job_for_user.getCompany());
        assertEquals(180000.99, job_for_user.getYearlySalary(), .001);
        assertEquals(9999, job_for_user.getYearlyBonus(), .001);
        assertEquals(60, job_for_user.getRetirementPercentage());
        assertEquals(10.8, job_for_user.getRestrictedStock(), .001);
        assertEquals(16000, job_for_user.getLearningDevBudget(), .001);
        assertEquals(2000, job_for_user.getFamilyAssistancePlanning(), .001);
        assertEquals(Job.JobType.CURRENT, job_for_user.getJobType());
        assertEquals(location.getLocationId(), job_for_user.getLocationId());
        Location job_location = databaseHelper.getLocation(job_for_user.getLocationId());
        assertEquals("San Francisco", job_location.getCity());
        assertEquals("CA", job_location.getState());
        assertEquals(10, job_location.getCostOfLivingIndex());
        databaseHelper.close();
    }

    @Test
    public void test_insertJobOffer(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job job_offer = new Job();
        job_offer.setUserId(user.getUserId());
        job_offer.setTitle("Dev3");
        job_offer.setCompany("Sample Company3");
        job_offer.setYearlySalary(180000.99);
        job_offer.setYearlyBonus(9999);
        job_offer.setRetirementPercentage(60);
        job_offer.setRestrictedStock(10.8);
        job_offer.setLearningDevBudget(16000);
        job_offer.setFamilyAssistancePlanning(2000);
        job_offer.setJobType(Job.JobType.OFFER);
        int return_val = (int)databaseHelper.insertJobOffer(job_offer, location, user.getUserId());
        assertTrue(return_val>0);
        databaseHelper.close();
    }


    @Test
    public void test_getJobOffer(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job job_offer = new Job();
        job_offer.setUserId(user.getUserId());
        job_offer.setTitle("Dev3");
        job_offer.setCompany("Sample Company3");
        job_offer.setYearlySalary(180000.99);
        job_offer.setYearlyBonus(9999);
        job_offer.setRetirementPercentage(60);
        job_offer.setRestrictedStock(10.8);
        job_offer.setLearningDevBudget(16000);
        job_offer.setFamilyAssistancePlanning(2000);
        job_offer.setJobType(Job.JobType.OFFER);
        int return_val = (int)databaseHelper.insertJobOffer(job_offer, location, user.getUserId());
        assertTrue(return_val>0);
        Job job = databaseHelper.getJobOffer(user.getUserId(), return_val);
        assertNotNull(job);
        databaseHelper.close();
    }

    @Test
    public void test_getUserJobOffers(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job job_offer = new Job();
        job_offer.setUserId(user.getUserId());
        job_offer.setTitle("Dev3");
        job_offer.setCompany("Sample Company3");
        job_offer.setYearlySalary(180000.99);
        job_offer.setYearlyBonus(9999);
        job_offer.setRetirementPercentage(60);
        job_offer.setRestrictedStock(10.8);
        job_offer.setLearningDevBudget(16000);
        job_offer.setFamilyAssistancePlanning(2000);
        job_offer.setJobType(Job.JobType.OFFER);
        int return_val = (int)databaseHelper.insertJobOffer(job_offer, location, user.getUserId());
        assertTrue(return_val>0);
        ArrayList<Job> jobs_for_user = databaseHelper.getUserJobOffers(user.getUserId());
        Job job_offer_for_user = jobs_for_user.get(0);
        assertEquals("Dev3", job_offer_for_user.getTitle());
        assertEquals("Sample Company3", job_offer_for_user.getCompany());
        assertEquals(180000.99, job_offer_for_user.getYearlySalary(), .001);
        assertEquals(9999, job_offer_for_user.getYearlyBonus(), .001);
        assertEquals(60, job_offer_for_user.getRetirementPercentage());
        assertEquals(10.8, job_offer_for_user.getRestrictedStock(), .001);
        assertEquals(16000, job_offer_for_user.getLearningDevBudget(), .001);
        assertEquals(2000, job_offer_for_user.getFamilyAssistancePlanning(), .001);
        assertEquals(Job.JobType.OFFER, job_offer_for_user.getJobType());
        assertEquals(location.getLocationId(), job_offer_for_user.getLocationId());
        Location job_location = databaseHelper.getLocation(job_offer_for_user.getLocationId());
        assertEquals("San Francisco", job_location.getCity());
        assertEquals("CA", job_location.getState());
        assertEquals(10, job_location.getCostOfLivingIndex());
        databaseHelper.close();
    }

    @Test
    public  void test_insertLocation(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        int return_val = (int)databaseHelper.insertLocation(location);
        assertEquals(databaseHelper.getLocation(location.getLocationId()).getLocationId(), return_val);
        databaseHelper.close();
    }

    @Test
    public void test_getAllUserJobs(){
        databaseHelper.open();
        User user = databaseHelper.getSingularUser();
        Location location = new Location();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCostOfLivingIndex(10);
        Job current_job = new Job();
        current_job.setUserId(user.getUserId());
        current_job.setTitle("Dev");
        current_job.setCompany("Sample Company");
        current_job.setYearlySalary(180000.99);
        current_job.setYearlyBonus(9999);
        current_job.setRetirementPercentage(60);
        current_job.setRestrictedStock(10.8);
        current_job.setLearningDevBudget(16000);
        current_job.setFamilyAssistancePlanning(2000);
        current_job.setJobType(Job.JobType.CURRENT);
        int return_val = (int)databaseHelper.insertCurrentJob(current_job, location, user.getUserId());
        assertTrue(return_val>0);
        Location location2 = new Location();
        location2.setCity("San Francisco");
        location2.setState("CA");
        location2.setCostOfLivingIndex(10);
        Job job_offer = new Job();
        job_offer.setUserId(user.getUserId());
        job_offer.setTitle("Dev3");
        job_offer.setCompany("Sample Company3");
        job_offer.setYearlySalary(180000.99);
        job_offer.setYearlyBonus(9999);
        job_offer.setRetirementPercentage(60);
        job_offer.setRestrictedStock(10.8);
        job_offer.setLearningDevBudget(16000);
        job_offer.setFamilyAssistancePlanning(2000);
        job_offer.setJobType(Job.JobType.OFFER);
        return_val = (int)databaseHelper.insertJobOffer(job_offer, location2, user.getUserId());
        assertTrue(return_val>0);
        ArrayList<Job> userJobs = databaseHelper.getAllUserJobs(user.getUserId());
        assertEquals(2, userJobs.size());
        databaseHelper.close();
    }
}
