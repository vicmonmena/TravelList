package es.vicmonmena.openuax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
    			
			travel = new TravelInfo();
			
			if (intent.hasExtra(TravelInfo.EXTRA_COUNTRY)) {
				travel.setCountry(intent.getExtras().getString(TravelInfo.EXTRA_COUNTRY));
				((TextView) findViewById(R.id.countryLabel)).setText(travel.getCountry());
			}
			
			if (intent.hasExtra(TravelInfo.EXTRA_CITY)) {
				travel.setCity(intent.getExtras().getString(TravelInfo.EXTRA_CITY));
				((TextView) findViewById(R.id.cityLabel)).setText(travel.getCity());
			}
			
			if (intent.hasExtra(TravelInfo.EXTRA_YEAR)) {
				travel.setYear(intent.getExtras().getInt(TravelInfo.EXTRA_YEAR));
				((TextView) findViewById(R.id.yearLabel)).setText(""+travel.getYear());
			}
			
			if (intent.hasExtra(TravelInfo.EXTRA_NOTE)) {
				travel.setNote(intent.getExtras().getString(TravelInfo.EXTRA_NOTE));
				((TextView) findViewById(R.id.noteLabel)).setText(travel.getNote());
			}
    		/*
    		 * Si uitilizásemos Parcelable como alternativa no tendríamos que 
    		 * recoger parámetro a parámetro (travel.setXXX()), bastaría:
    		 * 
    		 * travel = 
    		 */
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
