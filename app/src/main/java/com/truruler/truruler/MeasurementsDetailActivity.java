package com.truruler.truruler;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import contentprovider.MeasurementsContentProvider;
import database.MeasurementsTable;

/*
 * TodoDetailActivity allows to enter a new todo item
 * or to change an existing
 */
public class MeasurementsDetailActivity extends AppCompatActivity {
    private Spinner mType;
    //private EditText mTitleText;
    private EditText mBodyText;
    private EditText mwidth;
    private EditText mHeight;
    private DatePicker mdate;
    private boolean validSave = false;

    private Uri todoUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.measure_edit);

        mType = (Spinner) findViewById(R.id.measure_type);
       // mTitleText = (EditText) findViewById(R.id.measure_edit_summary);
        mBodyText = (EditText) findViewById(R.id.measure_edit_summary);
        mwidth = (EditText) findViewById(R.id.measure_edit_width);
        mHeight = (EditText) findViewById(R.id.measure_edit_height);
        mdate = (DatePicker) findViewById(R.id.measure_datePicker);
        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        todoUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras
                    .getParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE);

            fillData(todoUri);
        }
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
                //String summary = mTitleText.getText().toString();
                String description = mBodyText.getText().toString();
                String width = mwidth.getText().toString();
                String height = mHeight.getText().toString();

                // only save if either summary or description
                // is available

                if (description.isEmpty() || width.isEmpty() || height.isEmpty() || type.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Missing data, fill in all options", Toast.LENGTH_SHORT).show();
                    return true;
                }


                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillData(Uri uri) {
        String[] projection = { MeasurementsTable.COLUMN_TYPE, MeasurementsTable.COLUMN_DESCRIPTION, MeasurementsTable.COLUMN_HEIGHT,
                MeasurementsTable.COLUMN_ID, MeasurementsTable.COLUMN_LAST_UPDTTM, MeasurementsTable.COLUMN_ORIGINAL_UPDTTM, MeasurementsTable.COLUMN_WIDTH};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String type = cursor.getString(cursor
                    .getColumnIndexOrThrow(MeasurementsTable.COLUMN_TYPE));

            for (int i = 0; i < mType.getCount(); i++) {

                String s = (String) mType.getItemAtPosition(i);
                if (s.equalsIgnoreCase(type)) {
                    mType.setSelection(i);
                }
            }

            mwidth.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(MeasurementsTable.COLUMN_WIDTH)));
            mHeight.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(MeasurementsTable.COLUMN_HEIGHT)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(MeasurementsTable.COLUMN_DESCRIPTION)));
            String[] date = cursor.getString(cursor.getColumnIndexOrThrow(MeasurementsTable.COLUMN_LAST_UPDTTM)).split("/");
            mdate.updateDate(Integer.parseInt(date[2]),Integer.parseInt(date[0]),Integer.parseInt(date[1]));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MeasurementsContentProvider.CONTENT_ITEM_TYPE, todoUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String type = (String) mType.getSelectedItem();
        //String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();
        String width = mwidth.getText().toString();
        String height = mHeight.getText().toString();
        Integer day = mdate.getDayOfMonth();
        Integer month = mdate.getMonth();
        Integer year = mdate.getYear();
        String date = month + "/" + day + "/" + year;

        // only save if either summary or description
        // is available

        if (description.isEmpty() || width.isEmpty() || height.isEmpty() || type.isEmpty()) {
            Toast.makeText(getBaseContext(), "Missing data, fill in all options", Toast.LENGTH_SHORT).show();
            return;
        }


        ContentValues values = new ContentValues();
        values.put(MeasurementsTable.COLUMN_TYPE, type);
        //values.put(MeasurementsTable.COLUMN_DESCRIPTION, summary);
        values.put(MeasurementsTable.COLUMN_DESCRIPTION, description);
        values.put(MeasurementsTable.COLUMN_HEIGHT, height);
        values.put(MeasurementsTable.COLUMN_WIDTH, width);
        values.put(MeasurementsTable.COLUMN_LAST_UPDTTM, date);
        //values.put(MeasurementsTable.COLUMN_ORIGINAL_UPDTTM, date);


        if (todoUri == null) {
            // New todo
            todoUri = getContentResolver().insert(
                    MeasurementsContentProvider.CONTENT_URI, values);
        } else {
            // Update todo
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(MeasurementsDetailActivity.this, "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }
}