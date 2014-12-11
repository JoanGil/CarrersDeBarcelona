package com.coneixbarcelona.main;

import java.util.ArrayList;
import java.util.List;

import com.example.carrersdebarcelona.Preferencies;
import com.example.sqlite.CarrersBD;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DetallsCarrerFragment extends Fragment implements OnClickListener {

	private String[] id;
	TextView nom, descripcio, font, data, noms_anteriors, districtes;
//	String codi;
	ImageButton ibMapa;
	private ShareActionProvider mShareActionProvider;
	public static List<String> informacioCarrersList = new ArrayList<String>();
//	public static String codiPreferencies;

	
	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		activarProgressDialog();
		getActivity().setTitle(R.string.DetallsCarrerFTitol);
		View rootView = inflater.inflate(R.layout.explicacio_carrer, container, false);

		//Té el seu propi menu
		setHasOptionsMenu(true);
		
		ibMapa = (ImageButton) rootView.findViewById(R.id.ibMapa);
		ibMapa.setOnClickListener(this);

		// Recuperem els valors de la llista (nom i codi)
		recuperarValors();

		// Recuperem el valor que hi ha com a idioma
//		SharedPreferences sharedPrefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		codiPreferencies = sharedPrefs.getString("idiomaApp", "0");

//		codi = id[1];
//		new GetInfo().execute(codi, codiPreferencies);
		
		
		
		nom = (TextView) rootView.findViewById(R.id.tvNomCarrer);
		descripcio = (TextView) rootView.findViewById(R.id.tvDescripcio);
		font = (TextView) rootView.findViewById(R.id.tvFont);
		data = (TextView) rootView.findViewById(R.id.tvData);
		noms_anteriors = (TextView) rootView.findViewById(R.id.tvNomsAnteriors);
		districtes = (TextView) rootView.findViewById(R.id.tvDistrictes);

		nom.setText(id[0]);
		descripcio.setText(id[1]);
		font.setText(id[2]);
		data.setText(id[3]);
		noms_anteriors.setText(id[4]);
		districtes.setText(id[5]);
		//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);

		return rootView;
	}

	@SuppressLint("NewApi")
	protected void recuperarValors() {
		Bundle extras = getArguments();
		if (extras != null) {
			id = extras.getStringArray("extras");
		}
	}
	
//	@SuppressLint("NewApi")
//	protected void activarProgressDialog() {
//		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.carregantInfo), true);
//		dialog.show();
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//		    public void run() {
//		        dialog.dismiss();
//		    }   
//		}, 500);  // 500 milliseconds
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibMapa:
			if (hasConnection()) {
				String nomCarrer = id[0];
				String nomFinal = modificarNom(nomCarrer);
				
				Fragment fragment = new MapaCarrerFragment();
				final Bundle bundle = new Bundle();
				bundle.putString("nom", nomFinal + ", Barcelona, Espanya");
	
				fragment.setArguments(bundle); 
				FragmentManager fm = getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.frame_container, fragment);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				transaction.addToBackStack(null);
				transaction.commit();
			}
			else {
				Toast.makeText(getActivity(), R.string.noInternetConection, Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
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

	
	@SuppressLint("NewApi")
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_detalls_carrer, menu);
		inflater.inflate(R.menu.items_share, menu);
		
		/*** Getting the actionprovider associated with the menu item whose id is share ***/
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share)
				.getActionProvider();

		/** Setting a share intent **/
		mShareActionProvider.setShareIntent(getDefaultShareIntent());
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// Comprovem que el carrer no estigui a la base de dades 
		// Si esta a la BD, desabilitem la opció de poder posar el carrer com a favorit.
		super.onPrepareOptionsMenu(menu);
		CarrersBD bd = new CarrersBD(getActivity());
		boolean fav = false;
		String nom = id[0].replace("'","");
		try {
			bd.abrir();
			fav = bd.exists(nom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bd.cerrar();
        if (fav == true) {
        	menu.findItem(R.id.menu_insertar).setEnabled(false);
        	menu.findItem(R.id.menu_esborrar).setEnabled(true);
        }
        if (fav == false) {
        	menu.findItem(R.id.menu_insertar).setEnabled(true);
        	menu.findItem(R.id.menu_esborrar).setEnabled(false);
        }
		return;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		String eliminarMsg = getResources().getString(R.string.eliminatOK);
		String afegirMsg = getResources().getString(R.string.afegitOK);
		
		switch (item.getItemId()) {
		
		case R.id.menu_settings:
			Intent i = new Intent(getActivity(), Preferencies.class);
			startActivity(i);
			return true;
			
		case R.id.menu_esborrar:
			try {
				CarrersBD eliminar = new CarrersBD(getActivity());
				eliminar.abrir();
				eliminar.eliminar(id[0].replace("'",""));
				eliminar.cerrar();
				Toast.makeText(getActivity(), modificarNom(id[0]) + " " + eliminarMsg, Toast.LENGTH_SHORT).show();

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

			break;	
			
		case R.id.menu_insertar:
			
			boolean funciono = true;

			try {

				CarrersBD entrada = new CarrersBD(getActivity());
				entrada.abrir();
				entrada.crearEntrada(id[0].replace("'",""), id[1], id[2], id[3], id[4], id[5]);
				entrada.cerrar();

			} catch (Exception e) {
				funciono = false;
				String error = e.toString();
				Dialog d = new Dialog(getActivity());
				d.setTitle("No Funciona");
				TextView tv = new TextView(getActivity());
				tv.setText(error);
				d.setContentView(tv);
				d.show();

			} finally {
				if (funciono) {
					Toast.makeText(getActivity(), modificarNom(id[0]) + " " + afegirMsg, Toast.LENGTH_LONG).show();
				}
			}

			break;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/** Returns a share intent */
	private Intent getDefaultShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
		intent.putExtra(Intent.EXTRA_TEXT, R.string.registresAnteriors);
		return intent;
	}
	
	  public boolean hasConnection() {
		  
		  ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		  
		  NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		  if (wifiNetwork != null && wifiNetwork.isConnected()) {
			  return true;
		  }
	
		  NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		  if (mobileNetwork != null && mobileNetwork.isConnected()) {
			  return true;
		  }
	
		  NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		  if (activeNetwork != null && activeNetwork.isConnected()) {
			  return true;
		  }
	
		  return false;
	}

//	private String networkType() {
//		TelephonyManager teleMan = (TelephonyManager)
//				getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//		int networkType = teleMan.getNetworkType();
//		switch (networkType) {
//		case TelephonyManager.NETWORK_TYPE_1xRTT:
//			return "1xRTT";
//		case TelephonyManager.NETWORK_TYPE_CDMA:
//			return "CDMA";
//		case TelephonyManager.NETWORK_TYPE_EDGE:
//			return "EDGE";
//		case TelephonyManager.NETWORK_TYPE_EHRPD:
//			return "eHRPD";
//		case TelephonyManager.NETWORK_TYPE_EVDO_0:
//			return "EVDO rev. 0";
//		case TelephonyManager.NETWORK_TYPE_EVDO_A:
//			return "EVDO rev. A";
//	  	case TelephonyManager.NETWORK_TYPE_EVDO_B:
//	  		return "EVDO rev. B";
//	  	case TelephonyManager.NETWORK_TYPE_GPRS:
//	  		return "GPRS";
//	  	case TelephonyManager.NETWORK_TYPE_HSDPA:
//	  		return "HSDPA";
//	  	case TelephonyManager.NETWORK_TYPE_HSPA:
//	  		return "HSPA";
//	  	case TelephonyManager.NETWORK_TYPE_HSPAP:
//	  		return "HSPA+";
//	  	case TelephonyManager.NETWORK_TYPE_HSUPA:
//	  		return "HSUPA";
//	  	case TelephonyManager.NETWORK_TYPE_IDEN:
//	  		return "iDen";
//	  	case TelephonyManager.NETWORK_TYPE_LTE:
//	  		return "LTE";
//	  	case TelephonyManager.NETWORK_TYPE_UMTS:
//	  		return "UMTS";
//	  	case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//	  		return "Unknown";
//		}
//		throw new RuntimeException("New type of network");
//  }
	  
	/** Conexió amb la pàgina que volem passant-li el codi de carrer */
//	public class GetInfo extends AsyncTask<String, Void, List<String>> {
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			progDailog = new ProgressDialog(DetallsCarrer.this);
//			progDailog.setMessage(getResources().getString(R.string.carregantInfo));
//			progDailog.setIndeterminate(false);
//			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			progDailog.setCancelable(true);
//			progDailog.show();
//		}
//
//		@Override
//		protected List<String> doInBackground(String... params) {
//			try {
//
//				String[] parametres = params;
//				URL url = new URL(
//						"http://w10.bcn.cat/APPS/nomenclator/ficha.do?codic="
//								+ parametres[0] + "&idioma=" + parametres[1]);
//
//				Document doc = Jsoup.connect(url.toString()).get();
//
//				Elements elements = doc.select("td.textonoticia");
//				Iterator<Element> it = elements.iterator();
//				while (it.hasNext()) {
//					Element e = it.next();
//					if (e.text() == "") {
//						informacioCarrersList
//								.add(getResources().getString(R.string.registresAnteriors));
//					} else {
//						informacioCarrersList.add(e.text());
//					}
//
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return informacioCarrersList;
//		}
//
//		@Override
//		protected void onPostExecute(List<String> result) {
//
//			nom = (TextView) findViewById(R.id.tvNomCarrer);
//			descripcio = (TextView) findViewById(R.id.tvDescripcio);
//			data = (TextView) findViewById(R.id.tvData);
//			noms_anteriors = (TextView) findViewById(R.id.tvNomsAnteriors);
//			districtes = (TextView) findViewById(R.id.tvDistrictes);
//
//			nom.setText(informacioCarrersList.get(0).toString());
//			descripcio.setText(informacioCarrersList.get(1).toString());
//			data.setText(informacioCarrersList.get(3).toString());
//			noms_anteriors.setText(informacioCarrersList.get(4).toString());
//			districtes.setText(informacioCarrersList.get(5).toString());
//
//			informacioCarrersList.clear();
//			super.onPostExecute(result);
//			progDailog.dismiss();
//		}
//
//	}

	

}
