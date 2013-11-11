package es.vicmonmena.openuax;

import java.util.ArrayList;

import es.vicmonmena.openuax.database.TravelsDatabaseHelper;
import es.vicmonmena.openuax.model.TravelInfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author vicmonmena
 */
public class TravelListActivity extends ListActivity{
	
	private static TravelsDatabaseHelper dbHelper;
	
	static int REQUEST_CODE_TRAVEL_CREATED = 10;
	static int REQUEST_CODE_TRAVEL_UPDATED = 20;
	
	private TravelAdapter adapter;
	
	private class TravelAdapter extends ArrayAdapter<TravelInfo>{
		
		private Context context;
		private ArrayList<TravelInfo> travels;
		private static final int RESOURCE = android.R.layout.simple_list_item_2;

		public ArrayList<TravelInfo> getTravels() {
			return travels;
		}
		
		public void setTravels(ArrayList<TravelInfo> travels) {
			this.travels = travels;
		}
		
		public TravelAdapter(Context context, ArrayList<TravelInfo> travels) {
			super(context, RESOURCE, travels);
			
			this.context = context;
			this.travels = travels;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LinearLayout view;
			ViewHolder holder;
			
			if (convertView == null){
				view = new LinearLayout(context);
				
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater.inflate(RESOURCE, view, true);
				
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(android.R.id.text1);
				holder.text2 = (TextView) view.findViewById(android.R.id.text2);
				view.setTag(holder);
				
			} else {
				view = (LinearLayout) convertView;
				holder = (ViewHolder) view.getTag();
			}
			
			//Rellenamos la vista con los datos
			TravelInfo info = travels.get(position);
			holder.text1.setText(info.getCity() + " (" + info.getCountry() + ")");
			holder.text2.setText(getResources().getString(R.string.year_hint) + ": " + info.getYear());
			
			return view;
		}
		
		@Override
		public int getCount() {
			return travels.size();
		}
	}
	
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new TravelsDatabaseHelper(this);

        //Obtenemos los datos de la base de datos
        ArrayList<TravelInfo> values = dbHelper.getTravelsList();

        //Creamos el adapter y lo asociamos a la activity
        adapter = new TravelAdapter(this, values);

        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.menu_new_travel:
				//Creamos el Intent para lanzar la Activity EditTravelActivity
				Intent intent = new Intent(TravelListActivity.this, EditTravelActivity.class);		
				startActivityForResult(intent, REQUEST_CODE_TRAVEL_CREATED);
				break;
			
			}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//Tomamos la informacion del viaje seleccionado
		TravelInfo info = adapter.getItem(position);

		//Creamos el Intent para lanzar la Activity TravelActivity
		Intent intent = new Intent(this, TravelActivity.class);

		//Anadimos como extra el objeto viaje seleccionado en el ListView
		intent.putExtra(TravelInfo.EXTRA_TRAVEL, adapter.getItem(position));
		
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
				if (requestCode == REQUEST_CODE_TRAVEL_CREATED) {
					dbHelper.insertTravel(dbHelper.getWritableDatabase(), 
						travel.getCity(), travel.getCountry(), 
						travel.getYear(), travel.getNote());
				} else if (requestCode == REQUEST_CODE_TRAVEL_UPDATED) {
					dbHelper.updateTravel(dbHelper.getWritableDatabase(), travel);
				}
			} else {
				Toast.makeText(this, getString(R.string.error_travel_information_empty), Toast.LENGTH_SHORT).show();
			}
			
			refreshUI();
		}
	}
	
	/**
	 * Actualiza el adapter y refresca la lista para mostrar los nuevos datos.
	 */
	public void refreshUI() {
		
		ArrayList<TravelInfo> values = dbHelper.getTravelsList();
		//adapter.setTravels(values);
        //adapter.notifyDataSetChanged();
		adapter = new TravelAdapter(this, values);
        setListAdapter(adapter);
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
		
		switch (item.getItemId()) {
			case R.id.ctx_menu_option_edit:
				//Creamos el Intent para lanzar la Activity EditTravelActivity
				Intent intent = new Intent(TravelListActivity.this, EditTravelActivity.class);
				intent.putExtra(TravelInfo.EXTRA_TRAVEL, adapter.getItem(info.position));
				startActivityForResult(intent, REQUEST_CODE_TRAVEL_UPDATED);
				break;
			default:
				dbHelper.deleteTravel(dbHelper.getWritableDatabase(), adapter.getItem(info.position));
				refreshUI();
				break;
		}
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Mantenemos el estado del listado actual 
		outState.putParcelableArrayList("currentList", adapter.getTravels());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		if (state != null) {
			if (state.getParcelableArrayList("currentList") != null) {
				ArrayList<TravelInfo> travels = state.getParcelableArrayList("currentList");
				adapter.setTravels(travels);
			}
		}
		super.onRestoreInstanceState(state);
	}
}
