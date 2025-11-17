package edu.gatech.seclass.jobcompare6300.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.model.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.model.Job;
import edu.gatech.seclass.jobcompare6300.model.Location;
import edu.gatech.seclass.jobcompare6300.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "JobComparison";
    public static final int DB_VERSION = 2;
    public SQLiteDatabase database = null;
    public final String USER_TABLE_NAME = "user";
    public final String JOBOFFER_TABLE_NAME = "jobOffer";
    public final String CURRENTJOB_TABLE_NAME = "currentJob";
    public final String LOCATION_TABLE_NAME = "location";
    public final String COMPARISONSETTINGS_TABLE_NAME = "comparisonSettings";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE user(" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ");";

        String CREATE_JOB_OFFER_TABLE = "CREATE TABLE jobOffer(" +
                "job_offer_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT," +
                "company TEXT," +
                "location_id INTEGER NOT NULL," +
                "yearly_salary REAL," +
                "yearly_bonus REAL," +
                "retirement_percentage INTEGER CHECK(retirement_percentage BETWEEN 0 and 100)," +
                "restricted_stock REAL," +
                "learning_dev_budget REAL CHECK(learning_dev_budget BETWEEN 0 and 18000)," +
                "family_assistance_planning REAL CHECK(family_assistance_planning BETWEEN 0 and 0.12 * yearly_salary),"
                +
                "user_id INTEGER NOT NULL," +
                "FOREIGN KEY (location_id) REFERENCES location(location_id)," +
                "FOREIGN KEY (user_id) REFERENCES user(user_id)" +
                ");";

        String CREATE_CURRENT_JOB_TABLE = "CREATE TABLE currentJob(" +
                "current_job_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT," +
                "company TEXT," +
                "location_id INTEGER NOT NULL," +
                "yearly_salary REAL," +
                "yearly_bonus REAL," +
                "retirement_percentage INTEGER CHECK(retirement_percentage BETWEEN 0 and 100)," +
                "restricted_stock REAL," +
                "learning_dev_budget REAL CHECK(learning_dev_budget BETWEEN 0 and 18000)," +
                "family_assistance_planning REAL CHECK(family_assistance_planning BETWEEN 0 and 0.12 * yearly_salary),"
                +
                "user_id INTEGER NOT NULL," +
                "FOREIGN KEY (location_id) REFERENCES location(location_id)," +
                "FOREIGN KEY (user_id) REFERENCES user(user_id)" +
                ");";

        String CREATE_LOCATION_TABLE = "CREATE TABLE location(" +
                "location_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "city TEXT," +
                "state TEXT," +
                "cost_of_living_index INTEGER" +
                ");";

        String CREATE_COMPARISON_SETTINGS_TABLE = "CREATE TABLE comparisonSettings(" +
                "settings_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "salary_weight INTEGER DEFAULT 1 CHECK(salary_weight BETWEEN 0 and 9)," +
                "bonus_weight INTEGER DEFAULT 1 CHECK(bonus_weight BETWEEN 0 and 9)," +
                "retirement_weight INTEGER DEFAULT 1 CHECK(retirement_weight BETWEEN 0 and 9)," +
                "stock_weight INTEGER DEFAULT 1 CHECK(stock_weight BETWEEN 0 and 9)," +
                "learning_dev_weight INTEGER DEFAULT 1 CHECK(learning_dev_weight BETWEEN 0 and 9)," +
                "family_planning_weight INTEGER DEFAULT 1 CHECK(family_planning_weight BETWEEN 0 and 9)," +
                "user_id INTEGER NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES User(user_id)" +
                ");";

        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_CURRENT_JOB_TABLE);
        db.execSQL(CREATE_JOB_OFFER_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_COMPARISON_SETTINGS_TABLE);

        // Check if the singular-user exists
        // Check if any users exist
        Cursor cursor = db.rawQuery("SELECT COUNT(user_id) FROM user", null);
        cursor.moveToFirst();
        int userCount = cursor.getInt(0);
        cursor.close();

        // If no user exists, insert the singular user
        if (userCount == 0) {
            String insertDefaultUser = "INSERT INTO user (user_id) VALUES (NULL)";
            db.execSQL(insertDefaultUser);

            // Retrieve the new user_id of the inserted user
            Cursor newUserCursor = db.rawQuery("SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1", null);
            newUserCursor.moveToFirst();
            int newUserId = newUserCursor.getInt(0);
            newUserCursor.close();

            // Insert the default comparison settings for the newly inserted user
            String insertDefaultCompSettings = "INSERT INTO comparisonSettings (user_id, salary_weight, bonus_weight, retirement_weight, stock_weight, learning_dev_weight, family_planning_weight) "
                    +
                    "VALUES (" + newUserId + ", 1, 1, 1, 1, 1, 1)";
            db.execSQL(insertDefaultCompSettings);
        } else {
            // If user exists, check if singular user has comparison settings
            Cursor cursor_user = db.rawQuery("SELECT user_id FROM user", null);
            cursor_user.moveToFirst();
            int user_id = cursor_user.getInt(0);
            cursor_user.close();

            // Check if comparison settings exist for this user
            Cursor comparisonCursor = db.rawQuery("SELECT COUNT(*) FROM comparisonSettings WHERE user_id = " + user_id,
                    null);
            comparisonCursor.moveToFirst();
            int settingsCount = comparisonCursor.getInt(0);
            comparisonCursor.close();

            // If no comparison settings exist, insert the default comparison settings
            if (settingsCount == 0) {
                String insertDefaultCompSettings = "INSERT INTO comparisonSettings (user_id, salary_weight, bonus_weight, retirement_weight, stock_weight, learning_dev_weight, family_planning_weight) "
                        +
                        "VALUES (" + user_id + ", 1, 1, 1, 1, 1, 1)";
                db.execSQL(insertDefaultCompSettings);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER_STATEMENT = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";\n";
        String DROP_COMPARISON_STATEMENT = "DROP TABLE IF EXISTS " + COMPARISONSETTINGS_TABLE_NAME + ";\n";
        String DROP_CURRENTJOB_STATEMENT = "DROP TABLE IF EXISTS " + CURRENTJOB_TABLE_NAME + ";\n";
        String DROP_JOBOFFER_STATEMENT = "DROP TABLE IF EXISTS " + JOBOFFER_TABLE_NAME + ";\n";
        String DROP_LOCATION_STATEMENT = "DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME + ";\n";
        db.execSQL(DROP_USER_STATEMENT);
        db.execSQL(DROP_COMPARISON_STATEMENT);
        db.execSQL(DROP_CURRENTJOB_STATEMENT);
        db.execSQL(DROP_JOBOFFER_STATEMENT);
        db.execSQL(DROP_LOCATION_STATEMENT);
        this.onCreate(db);
    }

    public void open() {
        // Open the database only if it is not already open
        if (this.database == null || !this.database.isOpen()) {
            this.database = this.getWritableDatabase();
        }
    }

    public void close() {
        if (this.database != null && this.database.isOpen()) {
            this.database.close();
            this.database = null;
        }
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public ComparisonSettings getUserSettings(int userId) {

        String where_clause = "user_id = ?";
        String[] where_clause_val = { String.valueOf(userId) };
        Cursor cursor = database.query(COMPARISONSETTINGS_TABLE_NAME, null, where_clause, where_clause_val, null, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ComparisonSettings comparisonSettings = new ComparisonSettings();
                comparisonSettings.setSettingsId(cursor.getInt(0));
                comparisonSettings.setSalaryWeight(cursor.getInt(1));
                comparisonSettings.setBonusWeight(cursor.getInt(2));
                comparisonSettings.setRetirementWeight(cursor.getInt(3));
                comparisonSettings.setStockWeight(cursor.getInt(4));
                comparisonSettings.setLearningDevWeight(cursor.getInt(5));
                comparisonSettings.setFamilyPlanningWeight(cursor.getInt(6));
                comparisonSettings.setUser_id(cursor.getInt(7));
                cursor.close();
                return comparisonSettings;
            }
            cursor.close();
        }
        return null; // no settings associated with user.

    }

    public int upsertSettings(ComparisonSettings comparisonSettings) {
        ContentValues values = new ContentValues();
        values.put("salary_weight", comparisonSettings.getSalaryWeight());
        values.put("bonus_weight", comparisonSettings.getBonusWeight());
        values.put("retirement_weight", comparisonSettings.getRetirementWeight());
        values.put("stock_weight", comparisonSettings.getStockWeight());
        values.put("learning_dev_weight", comparisonSettings.getLearningDevWeight());
        values.put("family_planning_weight", comparisonSettings.getFamilyPlanningWeight());
        String where_clause = "user_id = ?";
        String[] where_clause_values = { String.valueOf(comparisonSettings.getUser_id()) };
        Cursor cursor = database.query(COMPARISONSETTINGS_TABLE_NAME, null, where_clause, where_clause_values, null,
                null, null);

        int result;

        if (cursor != null && cursor.moveToFirst()) {
            // Update the existing row
            result = database.update(COMPARISONSETTINGS_TABLE_NAME, values, where_clause, where_clause_values);
        } else {
            // Insert new row as no settings exist for the user
            values.put("user_id", comparisonSettings.getUser_id());
            result = (int) database.insert(COMPARISONSETTINGS_TABLE_NAME, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public long updateCurrentJob(Job currentJob, Location location, int userId) {
        double yearly_salary = currentJob.getYearlySalary();
        if (currentJob.getFamilyAssistancePlanning() < 0
                || currentJob.getFamilyAssistancePlanning() > (yearly_salary * .12)) {
            return -1;
        }
        if (currentJob.getLearningDevBudget() < 0 || currentJob.getLearningDevBudget() > 18000) {
            return -1;
        }
        if (currentJob.getRetirementPercentage() < 0 || currentJob.getRetirementPercentage() > 100) {
            return -1;
        }
        int location_id = (int) this.insertLocation(location);
        currentJob.setLocationId(location_id);
        ContentValues values = new ContentValues();
        values.put("title", currentJob.getTitle());
        values.put("company", currentJob.getCompany());
        values.put("location_id", location_id);
        values.put("yearly_salary", currentJob.getYearlySalary());
        values.put("yearly_bonus", currentJob.getYearlyBonus());
        values.put("retirement_percentage", currentJob.getRetirementPercentage());
        values.put("restricted_stock", currentJob.getRestrictedStock());
        values.put("learning_dev_budget", currentJob.getLearningDevBudget());
        values.put("family_assistance_planning", currentJob.getFamilyAssistancePlanning());
        values.put("user_id", userId);
        String where_clause = "user_id = ?";
        String[] where_clause_values = { String.valueOf(userId) };

        if (this.doesUserExist(currentJob.getUserId()) && this.doesLocationExist(location_id)) {
            database.delete(CURRENTJOB_TABLE_NAME, where_clause, where_clause_values);
            int job_id = (int) database.insert(CURRENTJOB_TABLE_NAME, null, values);
            currentJob.setJobId(job_id);
            return job_id;
        } else {
            return -1;
        }
    }

    private boolean doesUserHaveCurrentJob(int userId) {
        String query = "SELECT 1 FROM " + CURRENTJOB_TABLE_NAME + " WHERE user_id = ?";
        String[] whereArgs = { String.valueOf(userId) };
        Cursor cursor = database.rawQuery(query, whereArgs);

        boolean exists = (cursor.getCount() > 0); // If there is at least one row
        cursor.close();
        return exists;
    }

    private boolean doesUserExist(int userId) {
        String query = "SELECT 1 FROM " + USER_TABLE_NAME + " WHERE user_id = ?";
        String[] whereArgs = { String.valueOf(userId) };
        Cursor cursor = database.rawQuery(query, whereArgs);

        boolean exists = (cursor.getCount() > 0); // If there is at least one row
        cursor.close();
        return exists;
    }

    private boolean doesLocationExist(int locationId) {
        String query = "SELECT 1 FROM " + LOCATION_TABLE_NAME + " WHERE location_id = ?";
        String[] whereArgs = { String.valueOf(locationId) };
        Cursor cursor = database.rawQuery(query, whereArgs);

        boolean exists = (cursor.getCount() > 0); // If there is at least one row
        cursor.close();
        return exists;
    }

    public long insertCurrentJob(Job currentJob, Location location, int userId) {
        if (doesUserHaveCurrentJob(userId)) {
            return -1;
        }
        double yearly_salary = currentJob.getYearlySalary();
        if (currentJob.getFamilyAssistancePlanning() < 0
                || currentJob.getFamilyAssistancePlanning() > (yearly_salary * .12)) {
            return -1;
        }
        if (currentJob.getLearningDevBudget() < 0 || currentJob.getLearningDevBudget() > 18000) {
            return -1;
        }
        if (currentJob.getRetirementPercentage() < 0 || currentJob.getRetirementPercentage() > 100) {
            return -1;
        }
        int location_id = (int) this.insertLocation(location);
        currentJob.setLocationId(location_id);
        ContentValues values = new ContentValues();
        values.put("title", currentJob.getTitle());
        values.put("company", currentJob.getCompany());
        values.put("location_id", location_id);
        values.put("yearly_salary", currentJob.getYearlySalary());
        values.put("yearly_bonus", currentJob.getYearlyBonus());
        values.put("retirement_percentage", currentJob.getRetirementPercentage());
        values.put("restricted_stock", currentJob.getRestrictedStock());
        values.put("learning_dev_budget", currentJob.getLearningDevBudget());
        values.put("family_assistance_planning", currentJob.getFamilyAssistancePlanning());
        values.put("user_id", userId);
        if (this.doesUserExist(userId) && this.doesLocationExist(location_id)) {
            int job_id = (int) database.insert(CURRENTJOB_TABLE_NAME, null, values);
            return job_id;
        } else {
            return -1;
        }
    }

    public long insertJobOffer(Job jobOffer, Location location, int userId) {
        double yearly_salary = jobOffer.getYearlySalary();
        if (jobOffer.getFamilyAssistancePlanning() < 0
                || jobOffer.getFamilyAssistancePlanning() > (yearly_salary * .12)) {
            return -1;
        }
        if (jobOffer.getLearningDevBudget() < 0 || jobOffer.getLearningDevBudget() > 18000) {
            return -1;
        }
        if (jobOffer.getRetirementPercentage() < 0 || jobOffer.getRetirementPercentage() > 100) {
            return -1;
        }
        int location_id = (int) this.insertLocation(location);
        jobOffer.setLocationId(location_id);
        ContentValues values = new ContentValues();
        values.put("title", jobOffer.getTitle());
        values.put("company", jobOffer.getCompany());
        values.put("location_id", jobOffer.getLocationId());
        values.put("yearly_salary", jobOffer.getYearlySalary());
        values.put("yearly_bonus", jobOffer.getYearlyBonus());
        values.put("retirement_percentage", jobOffer.getRetirementPercentage());
        values.put("restricted_stock", jobOffer.getRestrictedStock());
        values.put("learning_dev_budget", jobOffer.getLearningDevBudget());
        values.put("family_assistance_planning", jobOffer.getFamilyAssistancePlanning());
        values.put("user_id", jobOffer.getUserId());
        if (this.doesUserExist(jobOffer.getUserId()) && this.doesLocationExist(location_id)) {
            int job_id = (int) database.insert(JOBOFFER_TABLE_NAME, null, values);
            return job_id;
        } else {
            return -1;
        }
    }

    public Job getJobOffer(int userId, int job_id) {
        Job jobOffer = null;
        String where_clause = "user_id = ? AND job_offer_id = ?";
        String[] where_clause_values = { String.valueOf(userId), String.valueOf(job_id) };
        Cursor cursor = null;
        try {
            // Query the current job table
            cursor = this.database.query(JOBOFFER_TABLE_NAME, null, where_clause, where_clause_values, null, null,
                    null);

            // Check if the cursor has results
            if (cursor != null && cursor.moveToFirst()) {
                jobOffer = new Job(); // Initialize currentJob
                jobOffer.setJobId(cursor.getInt(0));
                jobOffer.setTitle(cursor.getString(1));
                jobOffer.setCompany(cursor.getString(2));
                jobOffer.setLocationId(cursor.getInt(3));
                jobOffer.setYearlySalary(cursor.getDouble(4));
                jobOffer.setYearlyBonus(cursor.getDouble(5));
                jobOffer.setRetirementPercentage(cursor.getInt(6));
                jobOffer.setRestrictedStock(cursor.getDouble(7));
                jobOffer.setLearningDevBudget(cursor.getDouble(8));
                jobOffer.setFamilyAssistancePlanning(cursor.getDouble(9));
                jobOffer.setUserId(cursor.getInt(10));
                jobOffer.setJobType(Job.JobType.OFFER);
            }
        } finally {
            // Ensure cursor and database are closed properly
            if (cursor != null) {
                cursor.close(); // Close cursor if it was opened
            }
        }

        return jobOffer; // Return the job or null if no job was found
    }

    public ArrayList<Job> getUserJobOffers(int userId) {
        String where_clause = "user_id = ?";
        String[] where_clause_values = { String.valueOf(userId) };
        Cursor cursor = database.query(JOBOFFER_TABLE_NAME, null, where_clause, where_clause_values, null, null, null);
        ArrayList<Job> jobOffers = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) { // Check if cursor is not null and has at least one record
            do {
                Job jobOffer = new Job();
                jobOffer.setJobId(cursor.getInt(0));
                jobOffer.setTitle(cursor.getString(1));
                jobOffer.setCompany(cursor.getString(2));
                jobOffer.setLocationId(cursor.getInt(3));
                jobOffer.setYearlySalary(cursor.getDouble(4));
                jobOffer.setYearlyBonus(cursor.getDouble(5));
                jobOffer.setRetirementPercentage(cursor.getInt(6));
                jobOffer.setRestrictedStock(cursor.getDouble(7));
                jobOffer.setLearningDevBudget(cursor.getDouble(8));
                jobOffer.setFamilyAssistancePlanning(cursor.getDouble(9));
                jobOffer.setUserId(cursor.getInt(10));
                jobOffer.setJobType(Job.JobType.OFFER);
                jobOffers.add(jobOffer); // Add the job offer to the list
            } while (cursor.moveToNext()); // Move to the next record
        }
        if (cursor != null) {
            cursor.close(); // Close cursor in finally block
        }
        return jobOffers; // user doesn't have any job offers
    }

    public long insertLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put("city", location.getCity());
        values.put("state", location.getState());
        values.put("cost_of_living_index", location.getCostOfLivingIndex());
        int location_id = (int) database.insert(LOCATION_TABLE_NAME, null, values);
        location.setLocationId(location_id);
        return location_id;
    }

    public User getSingularUser() {
        Cursor cursor = this.getDatabase().query(USER_TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setUserId(cursor.getInt(0));
            cursor.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null; // no singular user created.
    }

    public Job getCurrentJob(int userId) {
        Job currentJob = null;
        String where_clause = "user_id = ?";
        String[] where_clause_values = { String.valueOf(userId) };
        Cursor cursor = null;
        try {
            // Query the current job table
            cursor = this.database.query(CURRENTJOB_TABLE_NAME, null, where_clause, where_clause_values, null, null,
                    null);

            // Check if the cursor has results
            if (cursor != null && cursor.moveToFirst()) {
                currentJob = new Job(); // Initialize currentJob
                currentJob.setJobId(cursor.getInt(0));
                currentJob.setTitle(cursor.getString(1));
                currentJob.setCompany(cursor.getString(2));
                currentJob.setLocationId(cursor.getInt(3));
                currentJob.setYearlySalary(cursor.getDouble(4));
                currentJob.setYearlyBonus(cursor.getDouble(5));
                currentJob.setRetirementPercentage(cursor.getInt(6));
                currentJob.setRestrictedStock(cursor.getDouble(7));
                currentJob.setLearningDevBudget(cursor.getDouble(8));
                currentJob.setFamilyAssistancePlanning(cursor.getDouble(9));
                currentJob.setUserId(cursor.getInt(10));
                currentJob.setJobType(Job.JobType.CURRENT);
            }
        } finally {
            // Ensure cursor and database are closed properly
            if (cursor != null) {
                cursor.close(); // Close cursor if it was opened
            }
        }

        return currentJob; // Return the job or null if no job was found
    }

    public ArrayList<Job> getAllUserJobs(int userId) {
        ArrayList<Job> userJobs = new ArrayList<>();
        Job currentJob = this.getCurrentJob(userId);
        if(currentJob != null){
            userJobs.add(currentJob);
        }
        userJobs.addAll(this.getUserJobOffers(userId));
        return userJobs;

    }

    public Location getLocation(int location_id) {
        String where_clause = "location_id = ?";
        String[] where_clause_values = { String.valueOf(location_id) };
        Cursor cursor = database.query(LOCATION_TABLE_NAME, null, where_clause, where_clause_values, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Location location = new Location();
            location.setLocationId(location_id);
            location.setCity(cursor.getString(1));
            location.setState(cursor.getString(2));
            location.setCostOfLivingIndex(cursor.getInt(3));
            cursor.close();
            return location;
        }
        return null; // no location set for job
    }
    public void deleteJobOffers() {
        database.delete(JOBOFFER_TABLE_NAME, null, null);
    }
    public void deleteCurrentJob() {
        database.delete(CURRENTJOB_TABLE_NAME, null, null);
    }
}
