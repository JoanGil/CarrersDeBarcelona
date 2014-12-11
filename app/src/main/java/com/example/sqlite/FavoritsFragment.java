package com.example.sqlite;


import java.util.ArrayList;

import com.coneixbarcelona.main.DetallsCarrerFragment;
import com.coneixbarcelona.main.R;
import com.example.carrersdebarcelona.Carrer;
import com.example.carrersdebarcelona.CarrersAdapter;
import com.example.carrersdebarcelona.Preferencies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FavoritsFragment extends Fragment implements OnItemClickListener  {

	private ListView lvCarrers;
	private CarrersAdapter adapter;
	private ArrayList<Carrer> carrers;
	private String name, descripcio, font, data, nom_anteriors, districtes;
	boolean clicked = false;

	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getActivity().setTitle(R.string.FavoritsFTitol);
		View rootView = inflater.inflate(R.layout.vista_database, container, false);
		
		lvCarrers = (ListView) rootView.findViewById(com.coneixbarcelona.main.R.id.lvItemsDatabase);

		carrers = new ArrayList<Carrer>();
		
		//Té el seu propi menu
		setHasOptionsMenu(true);
		
		//Obrim base i carreguem les dades de la base de dades.
		connectAndUpdateList();
	
		lvCarrers.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parentView, View view, int position, long ID) {
//            	lvCarrers.getItemAtPosition(position);
            	showDialogAndDelete(position);
            	return true;
            }
        });
		
		return rootView;
		
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_index_carrers, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent i = new Intent(getActivity(), Preferencies.class);
				startActivity(i);
				return true;
				
			case R.id.menu_borrarBD:
				CarrersBD entrada = new CarrersBD(getActivity());
				try {
					entrada.abrir();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					Toast.makeText(getActivity(), R.string.carrersEsborrats, Toast.LENGTH_LONG).show();
				}
				entrada.eliminarTabla("Taula_Carrers");
				carrers = entrada.getData();
				entrada.cerrar();
				adapter = new CarrersAdapter(carrers, getActivity());
				lvCarrers.setAdapter(adapter);
				lvCarrers.setOnItemClickListener(this);
//				
			default:
		        return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long ID) {
		// TODO Auto-generated method stub
		name = this.adapter.getItem(position).getNom();
		descripcio = this.adapter.getItem(position).getDescripcio();
		font = this.adapter.getItem(position).getFont();
		data = this.adapter.getItem(position).getData();
		nom_anteriors = this.adapter.getItem(position).getNoms_anteriors();
		districtes = this.adapter.getItem(position).getDistricte();

		// String amb les dades que volem passarli a la nova activitat
		String[] extras = { name, descripcio, font, data, nom_anteriors,
				districtes };
		
		Fragment fragment = new DetallsCarrerFragment();
		final Bundle bundle = new Bundle();
		bundle.putStringArray("extras", extras);
		fragment.setArguments(bundle); 
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.frame_container, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@SuppressLint("NewApi")
	protected void showDialogAndDelete(final int position) {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	
		// Setting Dialog Title
		alertDialog.setTitle(R.string.comfirmarEliminar);
		// Setting Dialog Message
		alertDialog.setMessage(R.string.comfirmarElPregunta);
		
		//Setting Icon to Dialog
//		alertDialog.setIcon(R.drawable.cross);
		
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// Write your code here to invoke NO event
//					Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});
		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton(R.string.deleteButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				eliminar(position);
				
				// Write your code here to invoke YES event
//					Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
			}
		});

		alertDialog.show();
	}
	
	protected void eliminar(int position) {
		String eliminarMsg = getResources().getString(R.string.eliminatOKBD);
		
		try {
			CarrersBD eliminar = new CarrersBD(getActivity());
			eliminar.abrir();
			eliminar.eliminar(this.adapter.getItem(position).getNom());
			eliminar.cerrar();
			Toast.makeText(getActivity(), modificarNom(this.adapter.getItem(position).getNom()) + " " + eliminarMsg, Toast.LENGTH_SHORT).show();
			connectAndUpdateList();

		} catch(Exception e) {
			e.printStackTrace();
			String error = e.toString();
			Dialog d = new Dialog(getActivity());
			d.setTitle("No funciona");
			TextView tv = new TextView(getActivity());
			tv.setText(error);
			d.setContentView(tv);
			d.show();
		}
	}
	
	protected void connectAndUpdateList() {
		CarrersBD info = new CarrersBD(getActivity());
		try {
			info.abrir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		carrers = info.getData();
		
		info.cerrar();
		adapter = new CarrersAdapter(carrers, getActivity());
		lvCarrers.setAdapter(adapter);
		lvCarrers.setOnItemClickListener(this);
	}
	
	public String modificarNom (String nom) {
		String sub = "";
		String acabat = "";
		if (nom.contains(",")) {
			int i = nom.indexOf(",");
			sub = nom.substring(i+1, nom.length());
			nom = nom.substring(0, i);
			if (sub.contains("'")) {
				acabat = sub + nom;	
			}
			else {
				acabat = sub + " " + nom;
			}
		}
		return acabat;
	}


}
