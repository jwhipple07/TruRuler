package measurements;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MeasurementsTable {
    // Database table
    public static final String TABLE_MEASUREMENTS = "measurements";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_WIDTH = "width";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LAST_UPDTTM = "lastUPDTTM";
    public static final String COLUMN_ORIGINAL_UPDTTM = "originalUPDTTM";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MEASUREMENTS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_WIDTH + " double not null, "
            + COLUMN_HEIGHT + " double not null, "
            + COLUMN_LAST_UPDTTM + " date not null, "
            + COLUMN_ORIGINAL_UPDTTM + " date default CURRENT_DATE,"
            + COLUMN_DESCRIPTION + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MeasurementsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENTS);
        onCreate(database);
    }
}
