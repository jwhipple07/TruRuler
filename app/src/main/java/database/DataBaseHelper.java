package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import measurements.MeasurementsTable;

public class DataBaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "measurementstable.db";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context -
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MeasurementsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MeasurementsTable.onUpgrade(db, oldVersion, newVersion);
    }


}
