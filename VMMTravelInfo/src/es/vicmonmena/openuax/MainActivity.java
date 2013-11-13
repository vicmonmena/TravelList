package es.vicmonmena.openuax;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity principal que maneja los fragments.
 * 
 * @author vicmonmena
 *
 */
public class MainActivity extends ActionBarActivity {

	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initActionBar();
	}
	
	/**
     * Inicializa la action bar.
     */
    private void initActionBar() {
    	
    	actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(
        	getResources().getDrawable(R.drawable.action_bar_shape));
        
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle presses on the action bar items
        switch (item.getItemId()) {
	        case android.R.id.home:
	        	Toast.makeText(this, "Go home!", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_new_travel:
				Toast.makeText(this, "New travel...", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_report:
				Toast.makeText(this, "Report issue...", Toast.LENGTH_SHORT).show();
				return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
