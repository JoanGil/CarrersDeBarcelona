package com.coneixbarcelona.main;


import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.example.carrersdebarcelona.Carrer;
import com.example.carrersdebarcelona.CarrersAdapter;
import com.example.carrersdebarcelona.Preferencies;
import com.example.sqlite.CarrersBD;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class IndexFragment extends Fragment implements OnItemClickListener {

	private ArrayList<Carrer> carrers;
	int textlength = 0;
	private ListView lvCarrers;
	private CarrersAdapter adapter;
	private String tagName, name, descripcio, font, data, nom_anteriors,
			districtes;
	private Carrer carrer;
	private EditText inputSearch;
	public static String codiPreferencies;
	public ImageView fav;

	public IndexFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().setTitle(R.string.IndexFTitol);
		View rootView = inflater.inflate(R.layout.llista_carrers, container,
				false);
		// ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);

		// Inicializamos las variables.
		carrers = new ArrayList<Carrer>();

		// Afegim la llista que toca tot el xml pq es vegi com a llista
		omplirArrayList();

		adapter = new CarrersAdapter(carrers, getActivity());

		inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
		lvCarrers = (ListView) rootView.findViewById(R.id.lvItems);
		// Asignamos el Adapter al ListView, en este punto hacemos que el
		// ListView muestre los datos que queremos.
		lvCarrers.setAdapter(adapter);

		// fer que canvii de color!
		fav = (ImageView) rootView.findViewById(R.id.favourite);
		
		// Asignamos el Listener al ListView para cuando pulsamos sobre uno de
		// sus items.
		lvCarrers.setOnItemClickListener(this);
		lvCarrers.setTextFilterEnabled(true);
		
		//Té el seu propi menu
		setHasOptionsMenu(true);
		
		// Evitar que el teclat aparegui a la llista
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count < before) {
					// We're deleting char so we need to reset the adapter data
					adapter.resetData();
				}
				adapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_altres, menu);
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

			default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
	
	/**
	 * Método que rellena el ArrayList con los carrers que queremos mostrar en
	 * el ListView.
	 */
	private void omplirArrayList() {
		try {
			carrer = new Carrer();
			
			// Recuperem el valor que hi ha com a idioma
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			codiPreferencies = sharedPrefs.getString("idiomaApp", "0");
			
			XmlPullParser xpp = null;
			if (codiPreferencies.equals("0")) xpp = getResources().getXml(R.xml.informacio_carrers_cat);	
			else if (codiPreferencies.equals("1")) xpp = getResources().getXml(R.xml.informacio_carrers_cas);
			else if (codiPreferencies.equals("2")) xpp = getResources().getXml(R.xml.informacio_carrers_eng);

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				tagName = xpp.getName();

				switch (xpp.getEventType()) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (tagName.equals("carrer")) {
						carrer = new Carrer();
					}
					if (tagName.equals("name")) {
						name = xpp.nextText().toString();
					}
					if (tagName.equals("descripcio")) {
						descripcio = xpp.nextText().toString();
					}
					if (tagName.equals("font")) {
						font = xpp.nextText().toString();
						if (font == "")
							font = getResources().getString(
									R.string.registresAnteriors);
					}
					if (tagName.equals("data")) {
						data = xpp.nextText().toString();
						if (data == "")
							data = getResources().getString(
									R.string.registresAnteriors);
					}
					if (tagName.equals("anteriors")) {
						nom_anteriors = xpp.nextText().toString();
						if (nom_anteriors == "")
							nom_anteriors = getResources().getString(
									R.string.registresAnteriors);
					}
					if (tagName.equals("districtes")) {
						districtes = xpp.nextText().toString();
					}
					break;
				case XmlPullParser.END_TAG:
					if (tagName.equals("carrer")) {
						carrer = new Carrer(name, descripcio, font, data,
								nom_anteriors, districtes);
						carrers.add(carrer);
					}

					break;
				}
				xpp.next();
			}
		} catch (Throwable t) {
			// Toast.makeText(this, "Request failed: " + t.toString(),
			// Toast.LENGTH_LONG).show();
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
//		System.out.println("pos: " + position);
//		
		name = this.adapter.getItem(position).getNom();
		descripcio = this.adapter.getItem(position).getDescripcio();
		font = this.adapter.getItem(position).getFont();
		data = this.adapter.getItem(position).getData();
		nom_anteriors = this.adapter.getItem(position).getNoms_anteriors();
		districtes = this.adapter.getItem(position).getDistricte();
		String[] extras = { name, descripcio, font, data, nom_anteriors, districtes };

//		//String amb les dades que volem passarli a la nova activitat
//		System.out.println("DADES:" + name +" "+ descripcio +" "+ font +" "+ data +" "+ nom_anteriors +" "+ districtes);
//		

		
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


}
