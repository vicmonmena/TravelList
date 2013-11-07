package es.vicmonmena.openuax;

import es.vicmonmena.openuax.model.TravelInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author vicmonmena
 *
 */
public class EditTravelActivity extends Activity {

	/**
	 * TAG for log messages.
	 */
	private String TAG = EditTravelActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_travel_activity_layout);
	}
	/**
	 * Called when click on view parameter.
	 * @param view - view clicked
	 */
	public void onClickView(View view) {
		
		if (view.getId() == R.id.save_travel) {
			
			if (TextUtils.isEmpty(((EditText) findViewById(R.id.countryField)).getText())) {
				Toast.makeText(this, getString(R.string.country_validation_msg), Toast.LENGTH_SHORT).show();
			} else if (TextUtils.isEmpty(((EditText) findViewById(R.id.cityField)).getText())) {
				Toast.makeText(this, getString(R.string.city_validation_msg), Toast.LENGTH_SHORT).show();
			} else if (TextUtils.isEmpty(((EditText) findViewById(R.id.yearField)).getText())) {
				Toast.makeText(this, getString(R.string.year_validation_msg), Toast.LENGTH_SHORT).show();
			} else {
				
				Intent intent = new Intent();
				
				//Bundle b = new Bundle();
				//b.putString(TravelInfo.EXTRA_COUNTRY, ((EditText) findViewById(R.id.countryField)).getText().toString());
				//intent.putExtras(b);
				
				// Se utilizaría esta forma o PARCELABLE, ¡NO AMBAS!
				intent.putExtra(TravelInfo.EXTRA_COUNTRY, ((EditText) findViewById(R.id.countryField)).getText().toString());
				intent.putExtra(TravelInfo.EXTRA_CITY, ((EditText) findViewById(R.id.cityField)).getText().toString());
				intent.putExtra(TravelInfo.EXTRA_YEAR, ((EditText) findViewById(R.id.yearField)).getText().toString());
				intent.putExtra(TravelInfo.EXTRA_NOTE, ((EditText) findViewById(R.id.noteField)).getText().toString());
				
				// Alternativa: PARCELABLE (Para modificaciones)
				TravelInfo travel = new TravelInfo(
						((EditText) findViewById(R.id.countryField)).getText().toString(), 
						((EditText) findViewById(R.id.cityField)).getText().toString(),
						Integer.parseInt(((EditText) findViewById(R.id.yearField)).getText().toString()),
						((EditText) findViewById(R.id.noteField)).getText().toString());
				
				intent.putExtra(TravelInfo.EXTRA_TRAVEL, travel);
				// ----------------------------------------------
				
				setResult(RESULT_OK, intent);
				finish();
			}
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
