package mse.hqevaluator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import mse.hqevaluator.entities.LastUpdated;
import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;

/**
 * Created by aga on 3/22/15.
 */
public class DbHelper {

    private SQLiteDatabase db = null;
    private Context context = null;
    private static final String DATABASE_NAME = "appdata.db";
    private static final int DATABASE_VERSION = 1;

    // private static final int TIMESPAN_BETWEEN_UPDATES = 604800; // this means 7 days (expressed in seconds)
    private static final int TIMESPAN_BETWEEN_UPDATES = 300; // this means 5 minutes (expressed in seconds

    public DbHelper(Context context) {
        this.context = context;
        db = openDatabase();

        // Check if we must create the schema as well
        if (!tableExists("LastUpdated")) {
            Log.i("DbHelper", "LastUpdated table is missing. Web must create the schema.");
            createSchema(db);
        }

        // Check if timestamp records exist
        LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();
        if (lastUpdatedTable.getRecordCount() != 2) {
            setInitRecords();
        }

        if (upgradeRequired(db)) {
            // Handle upgrade procedure
            upgradeDatabase(db);
        }
    }

    public boolean mustUpdateNuclearPowerPlants() throws DbException {
        long currTimestamp = System.currentTimeMillis() / 1000L;
        LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();
        LastUpdated lastUpdated = null;

        Log.i("DbHelper", "Checking if nuclear power plants need an update...");

        try {
            lastUpdated = lastUpdatedTable.getByTableName("NuclearPowerPlant");
        } catch (DbRecordNotFoundException e) {
            throw new DbException("Error while checking if nuclear power plants need an update.");
        }

        if (lastUpdated.Timestamp + TIMESPAN_BETWEEN_UPDATES < currTimestamp) {
            Log.i("DbHelper", "Nuclear power plants must be updated");
            return true;
        } else {
            Log.i("DbHelper", "Nuclear power plants must not be updated");
            return false;
        }
    }

    public boolean mustUpdateMotorwayRamps() throws DbException {
        long currTimestamp = System.currentTimeMillis() / 1000L;
        LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();
        LastUpdated lastUpdated = null;

        Log.i("DbHelper", "Checking if motorway ramps need an update...");

        try {
            lastUpdated = lastUpdatedTable.getByTableName("MotorwayRamp");
        } catch (DbRecordNotFoundException e) {
            throw new DbException("Error while checking if motorway ramps need an update.");
        }

        if (lastUpdated.Timestamp + TIMESPAN_BETWEEN_UPDATES < currTimestamp) {
            Log.i("DbHelper", "Motorway ramps must be updated");
            return true;
        } else {
            Log.i("DbHelper", "Motorway ramps must not be updated");
            return false;
        }
    }

    public void updateNuclearPowerPlants(List<NuclearPowerPlant> nuclearPowerPlants) throws DbException {
        try {
            Log.i("DbHelper", "Nuclear power plants: Starting update now...");
            NuclearPowerPlantTable nuclearPowerPlantTable = getNuclearPowerPlantTable();

            // Clear table
            nuclearPowerPlantTable.clear();

            // Insert fresh records
            Iterator<NuclearPowerPlant> iterator = nuclearPowerPlants.iterator();

            while (iterator.hasNext()) {
                NuclearPowerPlant nuclearPowerPlant = iterator.next();
                nuclearPowerPlantTable.insert(nuclearPowerPlant);
            }

            // Update timestamp
            LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();
            LastUpdated lastUpdated = lastUpdatedTable.getByTableName("NuclearPowerPlant");
            lastUpdated.Timestamp = System.currentTimeMillis() / 1000L;
            lastUpdatedTable.update(lastUpdated);

            Log.i("DbHelper", "Nuclear power plants: Update successful.");
        } catch (DbException e) {
            throw e;
        }
    }

    public void updateMotorwayRamps(List<MotorwayRamp> motorwayRamps) throws DbException {
        try {
            Log.i("DbHelper", "Motorway ramps: Starting update now...");
            MotorwayRampTable motorwayRampTable = getMotorwayRampTable();

            // Clear table
            motorwayRampTable.clear();

            // Insert fresh records
            Iterator<MotorwayRamp> iterator = motorwayRamps.iterator();

            while(iterator.hasNext()) {
                MotorwayRamp motorwayRamp = iterator.next();
                motorwayRampTable.insert(motorwayRamp);
            }

            // Update timestamp
            LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();
            LastUpdated lastUpdated = lastUpdatedTable.getByTableName("MotorwayRamp");
            lastUpdated.Timestamp = System.currentTimeMillis() / 1000L;
            lastUpdatedTable.update(lastUpdated);

            Log.i("DbHelper", "Motorway ramps: Update successful.");
        } catch (DbException e) {
            throw e;
        }
    }

    public NuclearPowerPlantTable getNuclearPowerPlantTable() {
        return new NuclearPowerPlantTable(db);
    }

    public MotorwayRampTable getMotorwayRampTable() {
        return new MotorwayRampTable(db);
    }

    public LastUpdatedTable getLastUpdatedTable() {
        return new LastUpdatedTable(db);
    }

    public boolean tableExists(String tableName) {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();

        return count > 0;
    }

    private SQLiteDatabase openDatabase() {
        SQLiteDatabase db;

        Log.i("DbHelper", "Opening database...");

        // Create the database if it does not exist yet.
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + "/" + DATABASE_NAME, null, null);

        Log.i("DbHelper", "Database successfully opened.");

        return db;
    }

    private void createSchema(SQLiteDatabase db) {
        String createStmtMotorway = "CREATE TABLE MotorwayRamp" +
            " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Motorway TEXT NOT NULL, " +
            "Longitude REAL NOT NULL, Latitude REAL NOT NULL);";

        String createStmtNuclearPowerPlant = "CREATE TABLE NuclearPowerPlant " +
            "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Description TEXT NOT NULL, " +
            "Longitude REAL NOT NULL, Latitude REAL NOT NULL);";

        String createStmtLastUpdated = "CREATE TABLE LastUpdated " +
             "(Id INTEGER PRIMARY KEY AUTOINCREMENT, TableName TEXT NOT NULL, " +
             "Timestamp INTEGER NOT NULL);";

        Log.i("DbHelper", "Creating database schema...");

        db.execSQL(createStmtMotorway);
        db.execSQL(createStmtNuclearPowerPlant);
        db.execSQL(createStmtLastUpdated);
        db.setVersion(DATABASE_VERSION);

        Log.i("DbHelper", "Database schema successfully created");
    }

    private void setInitRecords() {
        Log.i("DbHelper", "Setting init records");

        LastUpdatedTable lastUpdatedTable = getLastUpdatedTable();

        lastUpdatedTable.clear();

        LastUpdated lastUpdatedNuclearPowerPlant = new LastUpdated();
        lastUpdatedNuclearPowerPlant.Id = 1;
        lastUpdatedNuclearPowerPlant.TableName = "NuclearPowerPlant";
        lastUpdatedNuclearPowerPlant.Timestamp = 0;
        lastUpdatedTable.insert(lastUpdatedNuclearPowerPlant);

        LastUpdated lastUpdatedMotorwayRamp = new LastUpdated();
        lastUpdatedMotorwayRamp.Id = 2;
        lastUpdatedMotorwayRamp.TableName = "MotorwayRamp";
        lastUpdatedMotorwayRamp.Timestamp = 0;
        lastUpdatedTable.insert(lastUpdatedMotorwayRamp);

        Log.i("DbHelper", "Init records successfully set");
    }

    private boolean upgradeRequired(SQLiteDatabase db) {
        // Compare database versions
        int currDbVersion = db.getVersion();

        if (currDbVersion < DATABASE_VERSION) {
            Log.i("DbHelper", "Current database has schema version " + currDbVersion +
                " but app required schema version " + DATABASE_VERSION + ". Need to upgrade database schema now.");
            return true;
        } else {
            Log.i("DbHelper", "Detected schema version " + currDbVersion + ", which is current. No upgrade required.");
            return false;
        }
    }

    private void upgradeDatabase(SQLiteDatabase db) {
        // We can simplify this process by just dropping and recreating tables.

        String dropStmtMotorway = "DROP TABLE IF IT EXISTS Motorway";
        String dropStmtNuclearPowerPlant = "DROP TABLE IF IT EXISTS NuclearPowerPlant";
        String dropStmtLastUpdated = "DROP TABLE IF IT EXISTS LastUpdated";

        // Log the version upgrade.
        Log.w("DbOpenHelper", "Upgrading database from version " + db.getVersion() + " to " +
            DATABASE_VERSION + ". Existing data will be destroyed.");

        db.execSQL(dropStmtMotorway);
        db.execSQL(dropStmtNuclearPowerPlant);
        db.execSQL(dropStmtLastUpdated);
        createSchema(db);
    }
}

