package es.vicmonmena.openuax;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import es.vicmonmena.openuax.database.TravelsProvider;
import es.vicmonmena.openuax.database.utils.TravelsConstants;
import es.vicmonmena.openuax.model.TravelInfo;

/**
 * @author vicmonmena
 *
 */
public class TravelActivity extends Activity {
	
	TravelInfo travel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_activity_layout);
        
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	Intent intent = getIntent();
    	
    	if (intent != null) {
    		
			if (getIntent().hasExtra(TravelInfo.EXTRA_TRAVEL)) {
				int travelId = getIntent().getExtras().getInt(TravelInfo.EXTRA_TRAVEL);
				ContentResolver cr = getContentResolver();
				Cursor c = cr.query(
					Uri.parse(TravelsProvider.CONTENT_URI + "/" + travelId),
		        	TravelsConstants.PROJECTION_FULL, 
		        	null, null, null);
		        if (c.moveToFirst()) {
					travel = new TravelInfo(
						c.getString(c.getColumnIndex(TravelsConstants.COUNTRY)),
						c.getString(c.getColumnIndex(TravelsConstants.CITY)),
						c.getInt(c.getColumnIndex(TravelsConstants.YEAR)),
						c.getString(c.getColumnIndex(TravelsConstants.NOTE)));
					
					((TextView) findViewById(R.id.countryLabel)).setText(travel.getCountry());
					((TextView) findViewById(R.id.cityLabel)).setText(travel.getCity());
					((TextView) findViewById(R.id.yearLabel)).setText(""+travel.getYear());
					((TextView) findViewById(R.id.noteLabel)).setText(travel.getNote());
		        }
			}
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_travel, menu);
        return true;
    }
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
    	String text =
			getString(R.string.lbl_new_visit) + ": "
			+ travel.getCity() + " (" 
			+ travel.getCountry() + "), " 
			+ getString(R.string.year_hint) + " " 
			+ travel.getYear();
    	
    	Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.putExtra(Intent.EXTRA_TEXT, text);
    	intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, getString(R.string.menu_share_travel)));
		
		return super.onMenuItemSelected(featureId, item);
	}
}
