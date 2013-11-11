package es.vicmonmena.openuax.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import es.vicmonmena.openuax.database.utils.TravelsConstants;

/**
 * Content Provider para la base de datos de viajes
 *
 */
public class TravelsProvider extends ContentProvider {
	
	private static final String TAG = TravelsProvider.class.getName();
	
	private static final String AUTHORITY = "es.vicmonmena.openuax.travellist";
	
	public static final Uri CONTENT_URI = 
			Uri.parse("content://" + AUTHORITY + "/travels");

	private static final int URI_TRAVELS = 1;
	private static final int URI_TRAVEL_ITEM = 2;
	
	private static final UriMatcher mUriMatcher;
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, "travels", URI_TRAVELS);
		mUriMatcher.addURI(AUTHORITY, "travels/#", URI_TRAVEL_ITEM);
	}
	
	private TravelsDatabaseHelper mDbHelper;

	@Override
	public boolean onCreate() {
		mDbHelper = new TravelsDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		long id = db.insert(TravelsDatabaseHelper.TABLE_NAME, null, values);
		
		Uri result = null;
		
		if (id >= 0){
			result = ContentUris.withAppendedId(CONTENT_URI, id);
			notifyChange(result);
		}
		
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		int match = mUriMatcher.match(uri);

		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(TravelsDatabaseHelper.TABLE_NAME);
		
		switch (match){
		case URI_TRAVELS:
			//nada
			break;
		case URI_TRAVEL_ITEM:
			String id = uri.getPathSegments().get(1);
			qBuilder.appendWhere(TravelsConstants._ID + "=" + id);
			break;
		default:
			Log.w(TAG, "Uri didn't match: " + uri);
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		Cursor c = qBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		int numAffectedRows = db.update(TravelsDatabaseHelper.TABLE_NAME, values, 
			selection, selectionArgs);
		
		if (numAffectedRows > 0){
			notifyChange(uri);
		}
		return numAffectedRows;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int numAffectedRows = db.delete(TravelsDatabaseHelper.TABLE_NAME, selection, selectionArgs);
		if (numAffectedRows > 0){
			notifyChange(uri);
		}
		return numAffectedRows;
	}

	@Override
	public String getType(Uri uri) {
		
		int match = mUriMatcher.match(uri);
		
		switch (match){
		case URI_TRAVELS:
			return "vnd.android.cursor.dir/vnd.es.vicmonmena.openuax.travels";
		case URI_TRAVEL_ITEM:
			return "vnd.android.cursor.item/vnd.es.vicmonmena.openuax.travels";
		default:
			Log.w(TAG, "Uri didn't match: " + uri);
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	protected void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
