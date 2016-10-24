package contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import database.DataBaseHelper;
import measurements.MeasurementsTable;

@SuppressWarnings("ConstantConditions")
public class MeasurementsContentProvider extends ContentProvider {

    // database
    private DataBaseHelper database;

    // used for the UriMatcher
    private static final int MEASURE = 10;
    private static final int MEASURE_ID = 20;

    private static final String AUTHORITY = "contentprovider";
    private static final String BASE_PATH = "measures";
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
//    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/measures";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/measure";

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MEASURE);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MEASURE_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DataBaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(MeasurementsTable.TABLE_MEASUREMENTS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MEASURE:
                break;
            case MEASURE_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(MeasurementsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id;
        switch (uriType) {
            case MEASURE:
                id = sqlDB.insert(MeasurementsTable.TABLE_MEASUREMENTS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case MEASURE:
                rowsDeleted = sqlDB.delete(MeasurementsTable.TABLE_MEASUREMENTS, selection,
                        selectionArgs);
                break;
            case MEASURE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MeasurementsTable.TABLE_MEASUREMENTS,
                            MeasurementsTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MeasurementsTable.TABLE_MEASUREMENTS,
                            MeasurementsTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case MEASURE:
                rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MEASURE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS,
                            values,
                            MeasurementsTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MeasurementsTable.TABLE_MEASUREMENTS,
                            values,
                            MeasurementsTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {MeasurementsTable.COLUMN_DESCRIPTION, MeasurementsTable.COLUMN_HEIGHT,
                MeasurementsTable.COLUMN_ID, MeasurementsTable.COLUMN_LAST_UPDTTM, MeasurementsTable.COLUMN_ORIGINAL_UPDTTM,
                MeasurementsTable.COLUMN_WIDTH, MeasurementsTable.COLUMN_TYPE};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

}
