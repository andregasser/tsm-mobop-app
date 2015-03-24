package mse.hqevaluator.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mse.hqevaluator.entities.LastUpdated;

/**
 * Created by aga on 3/22/15.
 */
public class LastUpdatedTable {

    private SQLiteDatabase db;
    private static final String TABLE_NAME = "LastUpdated";

    public LastUpdatedTable(SQLiteDatabase db) {
        this.db = db;
    }

    public List<LastUpdated> getAll() {
        List<LastUpdated> lastUpdatedList = new ArrayList<LastUpdated>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add records to list
        if (cursor.moveToFirst()) {
            do {
                LastUpdated lastUpdated = new LastUpdated();
                lastUpdated.Id = Integer.parseInt(cursor.getString(0));
                lastUpdated.TableName = cursor.getString(1);
                lastUpdated.Timestamp = Integer.parseInt(cursor.getString(2));
                lastUpdatedList.add(lastUpdated);
            } while (cursor.moveToNext());
        }

        return lastUpdatedList;
    }

    public LastUpdated getByTableName(String tableName) throws DbRecordNotFoundException {
        LastUpdated lastUpdated = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE TableName = '" + tableName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            lastUpdated = new LastUpdated();
            lastUpdated.Id = cursor.getInt(0);
            lastUpdated.TableName = cursor.getString(1);
            lastUpdated.Timestamp = cursor.getInt(2);
            return lastUpdated;
        } else {
            throw new DbRecordNotFoundException("The table " + tableName + " was not found in the schema");
        }
    }

    public void insert(LastUpdated lastUpdated) {
        ContentValues values = new ContentValues();
        values.put("Id", lastUpdated.Id);
        values.put("TableName", lastUpdated.TableName);
        values.put("Timestamp", lastUpdated.Timestamp);

        db.insert(TABLE_NAME, null, values);
    }

    public void update(LastUpdated lastUpdated) {
        ContentValues values = new ContentValues();
        values.put("TableName", lastUpdated.TableName);
        values.put("Timestamp", lastUpdated.Timestamp);

        // updating row
        db.update(TABLE_NAME, values, "Id = ?",
                new String[]{String.valueOf(lastUpdated.Id)});
    }

    public void deleteById(LastUpdated lastUpdated) {
        db.delete(TABLE_NAME, "Id = ?",
            new String[] { String.valueOf(lastUpdated.Id) });
    }

    public long getRecordCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public void clear() {
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
