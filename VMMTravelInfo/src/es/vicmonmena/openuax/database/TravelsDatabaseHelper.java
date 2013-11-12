package es.vicmonmena.openuax.database;

import java.util.ArrayList;

import es.vicmonmena.openuax.database.utils.TravelsConstants;
import es.vicmonmena.openuax.model.TravelInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TravelsDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = TravelsDatabaseHelper.class.getName();
	
	private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "travels.db";
	
	public static final String TABLE_NAME = TravelsConstants.TRAVELS_TABLE_NAME;
    
	public TravelsDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
			TravelsConstants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
			TravelsConstants.CITY + " TEXT NOT NULL, " +
			TravelsConstants.COUNTRY + " TEXT NOT NULL, " +
			TravelsConstants.YEAR + " INTEGER NOT NULL, " +
			TravelsConstants.NOTE + " TEXT " + ");"
		);
		
		
		//Initial data
		initialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade");
		if (oldVersion < newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
			onCreate(db);
		}
	}

	
	private void initialData(SQLiteDatabase db){
		insertTravel(db, "Londres", "UK", 2012, "¡Juegos Olimpicos!");
		insertTravel(db, "Paris", "Francia", 2007, null);
		insertTravel(db, "Gotham City", "EEUU", 2011, "¡¡Batman!!");
		insertTravel(db, "Hamburgo", "Alemania", 2009, null);
		insertTravel(db, "Pekin", "China", 2011, null);

	}
	
	public void insertTravel(SQLiteDatabase db, String city, String country, int year, String note){
		ContentValues values = new ContentValues();
		
		values.put(TravelsConstants.CITY, city);
		values.put(TravelsConstants.COUNTRY, country);
		values.put(TravelsConstants.YEAR, year);
		values.put(TravelsConstants.NOTE, note);
		
		db.insert(TABLE_NAME, null, values);
	}
	
	 //Generamos datos a mostrar
    //En una aplicacion funcional se tomarian de base de datos o algun otro medio
    public ArrayList<TravelInfo> getTravelsList(){
    	
    	ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
    	
    	SQLiteDatabase db = getReadableDatabase();
    	
    	Cursor c = db.query(TravelsConstants.TRAVELS_TABLE_NAME, null, null, null, null, null, null);
    	
    	if (c.moveToFirst()){

    		int idIndex = c.getColumnIndex(TravelsConstants._ID);
    		int cityIndex = c.getColumnIndex(TravelsConstants.CITY);
    		int countryIndex = c.getColumnIndex(TravelsConstants.COUNTRY);
    		int yearIndex = c.getColumnIndex(TravelsConstants.YEAR);
    		int noteIndex = c.getColumnIndex(TravelsConstants.NOTE);
    		
    		do {

    			int id = c.getInt(idIndex);
    			String city = c.getString(cityIndex);
    			String country = c.getString(countryIndex);
    			int year = c.getInt(yearIndex);
    			String note = c.getString(noteIndex);
    			
    			TravelInfo travel = new TravelInfo(id, city, country, year, note);
    			
    			travels.add(travel);
    			
    		} while (c.moveToNext());
    		
    		c.close();
    	}
        
        return travels;
    }
    
    /**
     * Atualiza un viaje en la BBDD con id coincidente.
     * @param db - nombre de la BBDD.
     * @param travel - datos que se van a actualizar.
     */
    public void updateTravel(SQLiteDatabase db, TravelInfo travel){
		ContentValues values = new ContentValues();
		
		values.put(TravelsConstants.CITY, travel.getCity());
		values.put(TravelsConstants.COUNTRY, travel.getCountry());
		values.put(TravelsConstants.YEAR, travel.getYear());
		values.put(TravelsConstants.NOTE, travel.getNote());
		
		db.update(TABLE_NAME, values, TravelsConstants._ID + "=" + travel.getId(),null);
	}
    
    /**
     * Elimina un viaje de la BBDD.
     * @param db - nombre de la BBDD.
     * @param travel - objeto que se va a eliminar.
     */
    public void deleteTravel(SQLiteDatabase db, TravelInfo travel) {
    	db.delete(TABLE_NAME, TravelsConstants._ID + "=" + travel.getId(), null);
    }
}