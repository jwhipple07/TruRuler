package measurements;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.truruler.truruler.R;

import java.util.Locale;

import contentprovider.MeasurementsContentProvider;

/*
 * MeasurementDetailActivity allows to enter a new measurement item
 * or to change an existing
 */
public class MeasurementsDetailActivity extends AppCompatActivity {
    private Spinner mType;
    private EditText mBodyText;
    private EditText mwidth;
    private EditText mHeight;
    private DatePicker mdate;


    private Uri measureUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.measure_edit);
        mType = (Spinner) findViewById(R.id.measure_type);
        mBodyText = (EditText) findViewById(R.id.measure_edit_summary);
        mwidth = (EditText) findViewById(R.id.measure_edit_width);
        mHeight = (EditText) findViewById(R.id.measure_edit_height);
        mdate = (DatePicker) findViewById(R.id.measure_datePicker);
        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        measureUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            measureUri = extras.getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);
            if (measureUri != null) {
                fillData(measureUri);
            } else { //loading from mainActivity insert
                float width = extras.getFloat("width");
                float height = extras.getFloat("height");
                fillExtra(width, height);
            }
        }
    }

    public void fillExtra(Float width, Float height) {
        mwidth.setText(String.format(Locale.getDefault(), "%.1f", width));
        mHeight.setText(String.format(Locale.getDefault(), "%.1f", height));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int type = Integer.parseInt(sharedPreferences.getString("example_list", "0"));
        mType.setSelection(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.measure_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_measurement:
                String type = (String) mType.getSelectedItem();
                String description = mBodyText.getText().toString();
                String width = mwidth.getText().toString();
                String height = mHeight.getText().toString();

                // only save if either summary or description is available
                if (description.isEmpty() || width.isEmpty() || height.isEmpty() || type.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Missing data, fill in all options", Toast.LENGTH_SHORT).show();
                    return true;
                }
                setResult(RESULT_OK);
                saveState();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillData(Uri uri) {
        String[] projection = {MeasurementsTable.COLUMN_TYPE, MeasurementsTable.COLUMN_DESCRIPTION, MeasurementsTable.COLUMN_HEIGHT,
                MeasurementsTable.COLUMN_ID, MeasurementsTable.COLUMN_LAST_UPDTTM, MeasurementsTable.COLUMN_ORIGINAL_UPDTTM,
                MeasurementsTable.COLUMN_WIDTH};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_TYPE));

            for (int i = 0; i < mType.getCount(); i++) {
                String s = (String) mType.getItemAtPosition(i);
                if (s.equalsIgnoreCase(type)) {
                    mType.setSelection(i);
                }
            }

            mwidth.setText(cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_WIDTH)));
            mHeight.setText(cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_HEIGHT)));
            mBodyText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_DESCRIPTION)));
            String[] date = cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_LAST_UPDTTM)).split("/");
            mdate.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1]));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        outState.putParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE, measureUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // saveState();
    }

    private void saveState() {
        String type = (String) mType.getSelectedItem();
        String description = mBodyText.getText().toString();
        String width = mwidth.getText().toString();
        String height = mHeight.getText().toString();
        Integer day = mdate.getDayOfMonth();
        Integer month = mdate.getMonth();
        Integer year = mdate.getYear();
        String date = month + "/" + day + "/" + year;

        // only save if either summary or description is available
        if (description.isEmpty() || width.isEmpty() || height.isEmpty() || type.isEmpty()) {
            Toast.makeText(getBaseContext(), "Missing data, fill in all options", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MeasurementsTable.COLUMN_TYPE, type);
        values.put(MeasurementsTable.COLUMN_DESCRIPTION, description);
        values.put(MeasurementsTable.COLUMN_HEIGHT, height);
        values.put(MeasurementsTable.COLUMN_WIDTH, width);
        values.put(MeasurementsTable.COLUMN_LAST_UPDTTM, date);

        if (measureUri == null) {  // New measurement
            measureUri = getContentResolver().insert(MeasurementsContentProvider.CONTENT_URI, values);
        } else { // Update measurement
            getContentResolver().update(measureUri, values, null, null);
        }
    }
}