package mse.hqevaluator.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mse.hqevaluator.entities.MotorwayRamp;

/**
 * Created by aga on 3/22/15.
 */
public class MotorwayRampTable {

    private SQLiteDatabase db;
    private static final String TABLE_NAME = "MotorwayRamp";

    public MotorwayRampTable(SQLiteDatabase db) {
        this.db = db;
    }

    public List<MotorwayRamp> getAll() {
        List<MotorwayRamp> motorwayRamps = new ArrayList<MotorwayRamp>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add records to list
        if (cursor.moveToFirst()) {
            do {
                MotorwayRamp motorwayRamp = new MotorwayRamp();
                motorwayRamp.Id = Integer.parseInt(cursor.getString(0));
                motorwayRamp.Name = cursor.getString(1);
                motorwayRamp.Motorway = cursor.getString(2);
                motorwayRamp.Longitude = Float.parseFloat(cursor.getString(3));
                motorwayRamp.Latitude = Float.parseFloat(cursor.getString(4));
                motorwayRamps.add(motorwayRamp);
            } while (cursor.moveToNext());
        }

        return motorwayRamps;
    }

    public MotorwayRamp getById(int id) {
        MotorwayRamp motorwayRamp = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE Id = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        motorwayRamp = new MotorwayRamp();
        motorwayRamp.Id = Integer.parseInt(cursor.getString(0));
        motorwayRamp.Name = cursor.getString(1);
        motorwayRamp.Motorway = cursor.getString(2);
        motorwayRamp.Longitude = Float.parseFloat(cursor.getString(3));
        motorwayRamp.Latitude = Float.parseFloat(cursor.getString(4));

        return motorwayRamp;
    }

    public void insert(MotorwayRamp motorwayRamp) {
        ContentValues values = new ContentValues();
        values.put("Id", motorwayRamp.Id);
        values.put("Name", motorwayRamp.Name);
        values.put("Motorway", motorwayRamp.Motorway);
        values.put("Longitude", motorwayRamp.Longitude);
        values.put("Latitude", motorwayRamp.Latitude);

        db.insert(TABLE_NAME, null, values);
    }

    public void update(MotorwayRamp motorwayRamp) {
        ContentValues values = new ContentValues();
        values.put("Name", motorwayRamp.Name);
        values.put("Motorway", motorwayRamp.Motorway);
        values.put("Longitude", motorwayRamp.Longitude);
        values.put("Latitude", motorwayRamp.Latitude);

        // updating row
        db.update(TABLE_NAME, values, "Id = ?",
                new String[]{String.valueOf(motorwayRamp.Id)});
    }

    public void deleteById(MotorwayRamp motorwayRamp) {
        db.delete(TABLE_NAME, "Id = ?",
            new String[] { String.valueOf(motorwayRamp.Id) });
    }

    public long getRecordCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public void clear() {
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
