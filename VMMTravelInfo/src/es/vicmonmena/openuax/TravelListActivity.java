package es.vicmonmena.openuax;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import es.vicmonmena.openuax.database.TravelsProvider;
import es.vicmonmena.openuax.database.utils.TravelsConstants;
import es.vicmonmena.openuax.model.TravelInfo;

/**
 * @author vicmonmena
 */
public class TravelListActivity extends ListActivity {

	static final int REQUEST_CODE_TRAVEL_CREATED = 10;
	static final int REQUEST_CODE_TRAVEL_UPDATED = 20;
	
	static final int LEFT_TAB = 0;
	static final int RIGHT_TAB = 1;
	
	private TravelCursorAdapter adapter;
	
	final class TravelCursorAdapter extends ResourceCursorAdapter {

		private LayoutInflater mInflater;
		
		public TravelCursorAdapter(Context context, Cursor cursor) {
			super(context, android.R.layout.simple_list_item_2, cursor, true);
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			View view = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

			ViewHolder holder = new ViewHolder();
			TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
			TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
			holder.titleTxt = textView1;
			holder.subtitleTxt = textView2;
			view.setTag(holder);

			return view;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor c) {
			
			ViewHolder holder = (ViewHolder) view.getTag();
			
			//Rellenamos la vista con los datos
			holder.titleTxt.setText(c.getString(c.getColumnIndex(TravelsConstants.CITY))
				+ " (" + c.getString(c.getColumnIndex(TravelsConstants.COUNTRY))
				+ ")");
			holder.subtitleTxt.setText(getResources().getString(R.string.year_hint) 
				+ ": " 
				+ c.getString(c.getColumnIndex(TravelsConstants.YEAR)));
		}
		
	}
	
	static class ViewHolder {
		TextView titleTxt;
		TextView subtitleTxt;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(TravelsProvider.CONTENT_URI, 
        	TravelsConstants.PROJECTION, null, null, TravelsConstants.YEAR);
        adapter = new TravelCursorAdapter(this, c);
        setListAdapter(adapter);
        
        // registramos el listview para el menú contextual
        registerForContextMenu(getListView());
    }

    /**
     * Inicializa la action bar.
     */
    private void initActionBar() {
    	
    	final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actiion_bar_shape));
        
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
			case android.R.id.home:
				Toast.makeText(this, getString(R.string.app_name), Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_new_travel:
				//Creamos el Intent para lanzar la Activity EditTravelActivity
				Intent intent = new Intent(TravelListActivity.this, EditTravelActivity.class);		
				startActivityForResult(intent, REQUEST_CODE_TRAVEL_CREATED);
				break;
			case R.id.menu_info_app:
				new AlertDialog.Builder(this)
            	.setTitle(R.string.app_info)
            	.setMessage(R.string.app_info_content)
            	.setNeutralButton(R.string.ok,
            		new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int whichButton) {
                    		dialog.dismiss();
                    	}
                	})
            .create()
            .show();
				break;
			}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Cursor c = (Cursor) adapter.getItem(position);
		
		//Creamos el Intent para lanzar la Activity TravelActivity
		Intent intent = new Intent(this, TravelActivity.class);

		//Anadimos como extra el objeto viaje seleccionado en el ListView
		intent.putExtra(TravelInfo.EXTRA_TRAVEL, c.getInt(c.getColumnIndex(TravelsConstants._ID)));
		
		//Lanzamos la Activity con el Intent creado
		startActivity(intent);

		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			
			if (data != null && data.hasExtra(TravelInfo.EXTRA_TRAVEL)) {
				
				TravelInfo travel = data.getParcelableExtra(TravelInfo.EXTRA_TRAVEL);
				
				ContentValues values = new ContentValues(1);
				
				values.put(TravelsConstants.COUNTRY, travel.getCountry());
				values.put(TravelsConstants.CITY, travel.getCity());
				values.put(TravelsConstants.YEAR, travel.getYear());
				values.put(TravelsConstants.NOTE, travel.getNote());
				ContentResolver cr = getContentResolver();
				
				if (requestCode == REQUEST_CODE_TRAVEL_CREATED) {
			        cr.insert(TravelsProvider.CONTENT_URI, values);
				} else if (requestCode == REQUEST_CODE_TRAVEL_UPDATED) {
			        cr.update(TravelsProvider.CONTENT_URI, values, 
			        	TravelsConstants._ID + "="+travel.getId(), null);
				}
			} else {
				Toast.makeText(this, getString(R.string.error_travel_information_empty), 
					Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.ctx_menu_title));  
		getMenuInflater().inflate(R.menu.activity_travellist, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Cursor c = null;
		
		switch (item.getItemId()) {
			case R.id.ctx_menu_option_edit:
				c = (Cursor) adapter.getItem(info.position);
				
				//Creamos el Intent para lanzar la Activity EditTravelActivity
				Intent intent = new Intent(TravelListActivity.this, EditTravelActivity.class);
				intent.putExtra(TravelInfo.EXTRA_TRAVEL, c.getInt(c.getColumnIndex(TravelsConstants._ID)));
				startActivityForResult(intent, REQUEST_CODE_TRAVEL_UPDATED);
				
				break;
			default:
				
				c = (Cursor) adapter.getItem(info.position);
			
				final int travelId = c.getInt(c.getColumnIndex(TravelsConstants._ID));
				
				new AlertDialog.Builder(this)
	            	.setTitle(R.string.dialog_confirm_delete)
	            	.setPositiveButton(R.string.dialog_confirm_delete_yes,
	            	new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	ContentResolver cr = getContentResolver();
	        				cr.delete(TravelsProvider.CONTENT_URI,
	        						TravelsConstants._ID + "=" + travelId, null);
	                    }
	                })
	            	.setNegativeButton(R.string.dialog_confirm_delete_no,
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        dialog.dismiss();
	                    }
	                })
	            .create()
	            .show();
				break;
		}
		return true;
	}
}
