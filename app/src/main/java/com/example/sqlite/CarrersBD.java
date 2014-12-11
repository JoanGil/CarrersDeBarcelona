package com.example.sqlite;

import java.util.ArrayList;

import com.example.carrersdebarcelona.Carrer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarrersBD {

	public static final String ID_FILA = "_id";
	public static final String ID_CARRER = "nom_carrer";
	public static final String ID_DESCRIPCIO = "descripcio_carrer";
	public static final String ID_FONT = "font_carrer";
	public static final String ID_DATA = "data_carrer";
	public static final String ID_NOM_ANT = "nomanterior_carrer";
	public static final String ID_DISTRICTE = "districte_carrer";
	
	private static final String NOM_BD = "CarrersBD";
	private static final String NOM_TAULA = "Taula_Carrers";
	private static final int VERSIO_BD = 1;

	private final Context Contexto;
	private BDHelper Helper;
	private SQLiteDatabase BaseDatos;

	public CarrersBD(Context c) {
		Contexto = c;
	}

	private static class BDHelper extends SQLiteOpenHelper {

		public BDHelper(Context context) {
			super(context, NOM_BD, null, VERSIO_BD);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + NOM_TAULA + "(" 
					+ ID_FILA + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ ID_CARRER + " TEXT NOT NULL, " 
					+ ID_DESCRIPCIO + " TEXT NOT NULL, " 
					+ ID_FONT + " TEXT NOT NULL, " 
					+ ID_DATA + " TEXT NOT NULL, "
					+ ID_NOM_ANT + " TEXT NOT NULL, "
					+ ID_DISTRICTE + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + NOM_TAULA);
			onCreate(db);

		}
	}

	public CarrersBD abrir() throws Exception {
		Helper = new BDHelper(Contexto);
		BaseDatos = Helper.getWritableDatabase();
		return this;
	}

	public void cerrar() {
		Helper.close();
	}

	public long crearEntrada(String nom, String des, String font, String data, String ant, String dis) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(ID_CARRER, nom);
		cv.put(ID_DESCRIPCIO, des);
		cv.put(ID_FONT, font);
		cv.put(ID_DATA, data);
		cv.put(ID_NOM_ANT, ant);
		cv.put(ID_DISTRICTE, dis);
		return BaseDatos.insert(NOM_TAULA, null, cv);
	}

	public ArrayList<Carrer> getData() {
		
		//Carregar totes les dades i guardarles en un carrer
		String[] columnas = new String[] { ID_FILA, ID_CARRER, ID_DESCRIPCIO, ID_FONT, ID_DATA, ID_NOM_ANT, ID_DISTRICTE };
		ArrayList<Carrer> carrers = new ArrayList<Carrer>();
		Carrer carrer = new Carrer();
		Cursor c = BaseDatos.query(NOM_TAULA, columnas, null, null, null,null, null);
//		String result = "";
		
//		int iRow = c.getColumnIndex(ID_FILA);
		int iNom = c.getColumnIndex(ID_CARRER);
		int iDesc = c.getColumnIndex(ID_DESCRIPCIO);
		int iFont = c.getColumnIndex(ID_FONT);
		int iData = c.getColumnIndex(ID_DATA);
		int iAnte = c.getColumnIndex(ID_NOM_ANT);
		int iDisc = c.getColumnIndex(ID_DISTRICTE);
		
//		int iTelefono = c.getColumnIndex(ID_TELEFONO);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//			result = result + c.getString(iRow) + " " + c.getString(iName) + "\n";
			
			carrer = new Carrer(c.getString(iNom), c.getString(iDesc), c.getString(iFont), 
					 c.getString(iData), c.getString(iAnte), c.getString(iDisc));
			carrers.add(carrer);
		}

		//Fer return carrer
		return carrers;
	}

	public boolean exists(String Bbus) throws SQLException {
		Cursor c = BaseDatos.rawQuery("select * from "+ NOM_TAULA +" where "+ ID_CARRER +"='"+ Bbus +"'", null);
		if (c.getCount() > 0) return true;	
		else return false;
	}	

	public void eliminar(String Bbus) throws SQLException {
		BaseDatos.delete(NOM_TAULA, ID_CARRER + "='" + Bbus + "'", null);
	}	
	
	public void eliminarTabla(String tabla) throws SQLException {
		BaseDatos.delete(tabla, null, null);
	}	
	
	
	
//	public String getName(String bus) {
//	// TODO Auto-generated method stub
//	String[] columnas = new String[] { ID_FILA, ID_PERSONA, ID_TELEFONO };
//	Cursor c = BaseDatos.query(NOMBRE_TABLA, columnas, ID_PERSONA + "='"
//			+ bus + "'", null, null, null, null);
//	if (c != null) {
//		c.moveToFirst();
//		String nombre = c.getString(1);
//		return nombre;
//	}
//	return null;
//}
//
//public String getTel(String bus) {
//	// TODO Auto-generated method stub
//	String[] columnas = new String[] { ID_FILA, ID_PERSONA, ID_TELEFONO };
//	Cursor c = BaseDatos.query(NOMBRE_TABLA, columnas, ID_PERSONA + "='"
//			+ bus + "'", null, null, null, null);
//	if(c != null){
//		c.moveToFirst();
//		String telefono = c.getString(2);
//		return telefono;
//	}
//	return null;
//}
//
//public void modificar(String Ebus, String mNombre, String mTel) throws SQLException{
//	// TODO Auto-generated method stub
//	ContentValues cvModificar = new ContentValues();
//	cvModificar.put(ID_PERSONA, mNombre);
//	cvModificar.put(ID_TELEFONO, mTel);
//	BaseDatos.update(NOMBRE_TABLA, cvModificar, ID_PERSONA + "='" + Ebus + "'", null);
//	
//}
	
}
