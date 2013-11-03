package es.vicmonmena.openuax;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author vicmonmena
 *
 */
public class TravelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_activity_layout);
        
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	((TextView) findViewById(R.id.countryLabel)).setText("Canada");
    	((TextView) findViewById(R.id.cityLabel)).setText("Toronto");
    	((TextView) findViewById(R.id.yearLabel)).setText("2015");
    }
}
