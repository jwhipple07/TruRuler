package measurements;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.truruler.truruler.R;

import contentprovider.MeasurementsContentProvider;

/*
 * MeasurementsOverviewActivity displays the existing measurement items
 * in a list
 *
 * You can create new ones via the ActionBar entry "Insert"
 * You can delete existing ones via a long press on the item
 */

public class MeasurementsOverviewActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DELETE_ID = Menu.FIRST + 1;
    private SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure_list);
        this.getListView().setDividerHeight(2);
        fillData();
        registerForContextMenu(getListView());
        Button addNew = (Button) findViewById(R.id.addNewMeasure);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeasurement();
            }
        });
    }

    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createMeasurement();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //if delete option is selected in pop up menu item then delete the item.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MeasurementsContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createMeasurement() {
        Intent i = new Intent(this, MeasurementsDetailActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, MeasurementsDetailActivity.class);
        Uri measureUri = Uri.parse(MeasurementsContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MeasurementsContentProvider.CONTENT_ITEM_TYPE, measureUri);

        startActivity(i);
    }


    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{MeasurementsTable.COLUMN_ID, MeasurementsTable.COLUMN_WIDTH, MeasurementsTable.COLUMN_HEIGHT, MeasurementsTable.COLUMN_TYPE,
        MeasurementsTable.COLUMN_DESCRIPTION, MeasurementsTable.COLUMN_ORIGINAL_UPDTTM};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.label, R.id.measure_width_row, R.id.measure_height_row, R.id.measure_type_row,
        R.id.measure_descr_row, R.id.measure_date_row};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.measure_row, null, from, to, 0);

        setListAdapter(adapter);
    }

    //create a pop up menu to delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MeasurementsTable.COLUMN_ID, MeasurementsTable.COLUMN_WIDTH, MeasurementsTable.COLUMN_HEIGHT, MeasurementsTable.COLUMN_TYPE, MeasurementsTable.COLUMN_DESCRIPTION, MeasurementsTable.COLUMN_ORIGINAL_UPDTTM};
        return new CursorLoader(this, MeasurementsContentProvider.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}
