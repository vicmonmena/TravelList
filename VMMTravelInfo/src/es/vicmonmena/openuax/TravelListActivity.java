package es.vicmonmena.openuax;

import java.util.ArrayList;

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

/**
 * VERSION 1:
 * Este ejemplo muestra el uso de una clase ListActivity que muestra una lista de paises visitados.
 * 
 * Para ello hacemos uso de una extension del ArrayAdapter que contiene una lista de objetos TravelInfo.
 * El metodo getView del adapter se encarga de mostrar la informacion de cada entrada TravelInfo de la
 * forma correcta en la vista.
 * 
 * VERSION 2:
 * Anadimos en esta version los controles necesarios para lanzas las activities correspondientes
 * a mostrar un viaje mediante un click (metodo onListItemClick) y para crear un viaje usando el menu
 * de  opciones (onCreateOptionsMenu y onMenuItemSelected).
 * 
 * 
 */
public class TravelListActivity extends ListActivity{
	
	static int REQUEST_CODE_TRAVEL_CREATED = 10;
	static int REQUEST_CODE_TRAVEL_UPDATED = 20;
	
	/**
	 * Posición del item en el list view que va a ser actualizado.
	 * ¡¡ NO ME GUSTA NADA !! Investigar alternativa.
	 */
	int position = -1;
	
	private TravelAdapter adapter;
//	ListView lv;
	
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
		
		public void update(int position, TravelInfo travel) {
			travels.set(position, travel);
			notifyDataSetChanged();
		}
	}
	
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Generamos los datos
        ArrayList<TravelInfo> values = getData();
        
        //Creamos el adapter y lo asociamos a la activity
        adapter = new TravelAdapter(this, values);
        
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }
    
    //Generamos datos a mostrar
    //En una aplicacion funcional se tomarian de base de datos o algun otro medio
    private ArrayList<TravelInfo> getData(){
    	ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();

        TravelInfo info = new TravelInfo("Londres", "UK", 2012, "¡Juegos Olimpicos!");
        TravelInfo info2 = new TravelInfo("Paris", "Francia", 2007);
        TravelInfo info3 = new TravelInfo("Gotham City", "EEUU", 2011, "¡¡Batman!!");
        TravelInfo info4 = new TravelInfo("Hamburgo", "Alemania", 2009);
        TravelInfo info5 = new TravelInfo("Pekin", "China", 2011);

        travels.add(info);
        travels.add(info2);
        travels.add(info3);
        travels.add(info4);
        travels.add(info5);
        
        return travels;
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
		
		//Anadimos como extras los datos que consideremos necesarios para la Activity a lanzar
		intent.putExtra(TravelInfo.EXTRA_COUNTRY, info.getCountry());
		intent.putExtra(TravelInfo.EXTRA_CITY, info.getCity());
		intent.putExtra(TravelInfo.EXTRA_YEAR, info.getYear());
		intent.putExtra(TravelInfo.EXTRA_NOTE, info.getNote());
		
		/* Nota de vicmonmena:
		 * ------------------
		 *  La alternativa a pasar cada campo de TravelInfo como un EXTRA seía:
		 *  - Utilizar parámetro Bundle en putExtra(...)
		 *  - Extender de Parcelable la clase TravelInfo u otra con los 
		 *  	parámetros que queramos pasar. 
		 */
		
		//Lanzamos la Activity con el Intent creado
		startActivity(intent);
		
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_TRAVEL_CREATED) {
				
				/*
				 * Nota de vicmonmena:
				 * -----------------------
				 * Distingo entre creación y modificación para mostrar la 
				 * alternativa, usando PARCEL.
				 */ 
				
		    	if (data != null) {
	    			TravelInfo travel = new TravelInfo();
	    			
	    			if (data.hasExtra(TravelInfo.EXTRA_COUNTRY)) {
	    				travel.setCountry(data.getStringExtra(TravelInfo.EXTRA_COUNTRY));
	    			}
	    			
	    			if (data.hasExtra(TravelInfo.EXTRA_CITY)) {
	    				travel.setCity(data.getStringExtra(TravelInfo.EXTRA_CITY));
	    			}
	    			
	    			if (data.hasExtra(TravelInfo.EXTRA_YEAR)) {
	    				travel.setYear(Integer.parseInt(data.getStringExtra(TravelInfo.EXTRA_YEAR)));
	    			}
	    			
	    			if (data.hasExtra(TravelInfo.EXTRA_NOTE)) {
	    				travel.setNote(data.getStringExtra(TravelInfo.EXTRA_NOTE));
	    			}
	    			
	    			adapter.add(travel);
		    	}
			} else if (requestCode == REQUEST_CODE_TRAVEL_UPDATED) {
				if (data != null && data.hasExtra(TravelInfo.EXTRA_TRAVEL)) {
					TravelInfo travel = data.getParcelableExtra(TravelInfo.EXTRA_TRAVEL);
					adapter.update(position, travel);
				}
	    		
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
		
		switch (item.getItemId()) {
			case R.id.ctx_menu_option_edit:
				//Creamos el Intent para lanzar la Activity EditTravelActivity
				Intent intent = new Intent(TravelListActivity.this, EditTravelActivity.class);
				position = info.position;
				startActivityForResult(intent, REQUEST_CODE_TRAVEL_UPDATED);
				break;
			default:
				adapter.remove(adapter.getItem(info.position));
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
