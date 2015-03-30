package mse.hqevaluator.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mse.hqevaluator.entities.NuclearPowerPlant;

/**
 * Created by aga on 3/22/15.
 */
public class NuclearPowerPlantTable {

    private SQLiteDatabase db;
    private static final String TABLE_NAME = "NuclearPowerPlant";

    public NuclearPowerPlantTable(SQLiteDatabase db) {
        this.db = db;
    }

    public List<NuclearPowerPlant> getAll() {
        List<NuclearPowerPlant> nuclearPowerPlants = new ArrayList<NuclearPowerPlant>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add records to list
        if (cursor.moveToFirst()) {
            do {
                NuclearPowerPlant nuclearPowerPlant = new NuclearPowerPlant();
                nuclearPowerPlant.Id = Integer.parseInt(cursor.getString(0));
                nuclearPowerPlant.Name = cursor.getString(1);
                nuclearPowerPlant.Description = cursor.getString(2);
                nuclearPowerPlant.Longitude = Float.parseFloat(cursor.getString(3));
                nuclearPowerPlant.Latitude = Float.parseFloat(cursor.getString(4));
                nuclearPowerPlants.add(nuclearPowerPlant);
            } while (cursor.moveToNext());
        }

        return nuclearPowerPlants;
    }

    public NuclearPowerPlant getById(int id) {
        NuclearPowerPlant nuclearPowerPlant = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE Id = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        nuclearPowerPlant = new NuclearPowerPlant();
        nuclearPowerPlant.Id = Integer.parseInt(cursor.getString(0));
        nuclearPowerPlant.Name = cursor.getString(1);
        nuclearPowerPlant.Description = cursor.getString(2);
        nuclearPowerPlant.Longitude = Float.parseFloat(cursor.getString(3));
        nuclearPowerPlant.Latitude = Float.parseFloat(cursor.getString(4));

        return nuclearPowerPlant;
    }

    public void insert(NuclearPowerPlant nuclearPowerPlant) {
        ContentValues values = new ContentValues();
        values.put("Id", nuclearPowerPlant.Id);
        values.put("Name", nuclearPowerPlant.Name);
        values.put("Description", nuclearPowerPlant.Description);
        values.put("Longitude", nuclearPowerPlant.Longitude);
        values.put("Latitude", nuclearPowerPlant.Latitude);

        db.insert(TABLE_NAME, null, values);
    }

    public void update(NuclearPowerPlant nuclearPowerPlant) {
        ContentValues values = new ContentValues();
        values.put("Name", nuclearPowerPlant.Name);
        values.put("Description", nuclearPowerPlant.Description);
        values.put("Longitude", nuclearPowerPlant.Longitude);
        values.put("Latitude", nuclearPowerPlant.Latitude);

        // updating row
        db.update(TABLE_NAME, values, "Id = ?",
                new String[]{String.valueOf(nuclearPowerPlant.Id)});
    }

    public void deleteById(NuclearPowerPlant nuclearPowerPlant) {
        db.delete(TABLE_NAME, "Id = ?",
            new String[] { String.valueOf(nuclearPowerPlant.Id) });
    }

    public long getRecordCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public void clear() {
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
