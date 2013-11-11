package es.vicmonmena.openuax.database.utils;

import android.provider.BaseColumns;

/**
 * Constantes con la informacion de la base de datos.
 * 
 * @author vicmonmena
 *
 */
public interface TravelsConstants extends BaseColumns {
	
	/**
	 * Travel table name
	 */
	public static final String TRAVELS_TABLE_NAME = "travels";

	/**
	 * The city of the travel
	 * <P>Type: TEXT</P>
	 */
	public static final String CITY = "city";

	/**
	 * The city of the travel
	 * <P>Type: TEXT</P>
	 */
	public static final String COUNTRY = "country";

	/**
	 * The year of the travel
	 * <P>Type: NUMBER</P>
	 */
	public static final String YEAR = "year";

	/**
	 * The note
	 * <P>Type: TEXT</P>
	 */
	public static final String NOTE = "notes";
	
	/**
	 * Campos que obtenemos para mostrar en un listado de viajes.
	 */
	public static final String[] PROJECTION = {_ID, COUNTRY, CITY, YEAR};
	
	/**
	 * Campos que obtenemos para mostrar en los detalles de un viaje.
	 */
	public static final String[] PROJECTION_FULL = {_ID, COUNTRY, CITY, YEAR, NOTE};
}