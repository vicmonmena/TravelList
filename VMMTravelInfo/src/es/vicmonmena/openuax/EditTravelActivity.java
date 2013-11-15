package es.vicmonmena.openuax;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.vicmonmena.openuax.database.TravelsProvider;
import es.vicmonmena.openuax.database.utils.TravelsConstants;
import es.vicmonmena.openuax.model.TravelInfo;

/**
 * @author vicmonmena
 *
 */
public class EditTravelActivity extends Activity {

	/**
	 * TAG for log messages.
	 */
	private String TAG = EditTravelActivity.class.getName();
	
	private TravelInfo travel;
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_travel_activity_layout);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// Es edición?
		if (getIntent().hasExtra(TravelInfo.EXTRA_TRAVEL)) {
			
			int travelId = getIntent().getExtras().getInt(TravelInfo.EXTRA_TRAVEL);
			ContentResolver cr = getContentResolver();
			Uri uri = Uri.parse(TravelsProvider.CONTENT_URI + "/" + travelId);
	        Cursor c = cr.query(uri, TravelsConstants.PROJECTION_FULL, 
	        	null, null, null);
	        
	        if (c.moveToFirst()) {
				travel = new TravelInfo(
					c.getInt(c.getColumnIndex(TravelsConstants._ID)),
					c.getString(c.getColumnIndex(TravelsConstants.CITY)),
					c.getString(c.getColumnIndex(TravelsConstants.COUNTRY)),
					c.getInt(c.getColumnIndex(TravelsConstants.YEAR)),
					c.getString(c.getColumnIndex(TravelsConstants.NOTE)));
				
				((TextView) findViewById(R.id.countryField)).setText(travel.getCountry());
				((TextView) findViewById(R.id.cityField)).setText(travel.getCity());
				((TextView) findViewById(R.id.yearField)).setText(""+travel.getYear());
				((TextView) findViewById(R.id.noteField)).setText(travel.getNote());
	        }
		} else {
			// Es creación => instanciamos el objeto vacío
			travel = new TravelInfo();
		}
		
		initActionBar();
	}
	
	/**
     * Inicializa la action bar.
     */
    private void initActionBar() {
    	actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(
        	getResources().getDrawable(R.drawable.action_bar_shape));
        
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_travel_edit, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle presses on the action bar items
        switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(this,TravelListActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(intent);
	        	return true;
	        case R.id.menu_option_save:
	        	if (TextUtils.isEmpty(((EditText) findViewById(R.id.countryField)).getText())) {
					Toast.makeText(this, getString(R.string.country_validation_msg), Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(((EditText) findViewById(R.id.cityField)).getText())) {
					Toast.makeText(this, getString(R.string.city_validation_msg), Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(((EditText) findViewById(R.id.yearField)).getText())) {
					Toast.makeText(this, getString(R.string.year_validation_msg), Toast.LENGTH_SHORT).show();
				} else {
					
					Intent iTravel = new Intent();
					
					// Si es edición mantenemos el id
					travel.setCountry(((EditText) findViewById(R.id.countryField)).getText().toString()); 
					travel.setCity(((EditText) findViewById(R.id.cityField)).getText().toString());
					travel.setYear(Integer.parseInt(((EditText) findViewById(R.id.yearField)).getText().toString()));
					travel.setNote(((EditText) findViewById(R.id.noteField)).getText().toString());
					
					iTravel.putExtra(TravelInfo.EXTRA_TRAVEL, travel);
					
					setResult(RESULT_OK, iTravel);
					finish();
				}
	        	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "Saving activity data...");
		if (!TextUtils.isEmpty(((EditText) findViewById(R.id.countryField)).getText())) {
			outState.putString("country", ((EditText) findViewById(R.id.countryField)).getText().toString());
		}
		
		if (!TextUtils.isEmpty(((EditText) findViewById(R.id.cityField)).getText())) {
			outState.putString("city", ((EditText) findViewById(R.id.cityField)).getText().toString());
		}
		
		if (!TextUtils.isEmpty(((EditText) findViewById(R.id.yearField)).getText())) {
			outState.putString("year", ((EditText) findViewById(R.id.yearField)).getText().toString());
		}
		
		if (!TextUtils.isEmpty(((EditText) findViewById(R.id.noteField)).getText())) {
			outState.putString("note", ((EditText) findViewById(R.id.noteField)).getText().toString());
		}
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			Log.i(TAG, "Loading activity data...");
			if (!TextUtils.isEmpty(savedInstanceState.getString("country"))) {
				((EditText) findViewById(R.id.countryField)).setText(savedInstanceState.getString("country"));
			}
			
			if (!TextUtils.isEmpty(savedInstanceState.getString("city"))) {
				((EditText) findViewById(R.id.cityField)).setText(savedInstanceState.getString("city"));
			}
			
			if (!TextUtils.isEmpty(savedInstanceState.getString("year"))) {
				((EditText) findViewById(R.id.yearField)).setText(savedInstanceState.getString("year"));
			}
			
			if (!TextUtils.isEmpty(savedInstanceState.getString("note"))) {
				((EditText) findViewById(R.id.noteField)).setText(savedInstanceState.getString("note"));
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED, null);
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
