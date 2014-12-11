package com.coneixbarcelona.main;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


@SuppressLint("NewApi")
public class MapaCarrerFragment extends Fragment {
	
	MapView mapa;
	GoogleMap map;
	String nom;
	LatLng l;
	Geocoder coder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// inflat and return the layout
		getActivity().setTitle(R.string.MapaCarrerFTitol);
		View rootView = inflater.inflate(R.layout.map, container, false);
		mapa = (MapView) rootView.findViewById(R.id.mapView);
		mapa.onCreate(savedInstanceState);
		
		//Té el seu propi menu
		setHasOptionsMenu(true);
		
		MapsInitializer.initialize(getActivity());
		
		String address = recuperarValors();
		
		List<Address> foundGeocode = null;

		try {
			foundGeocode = new Geocoder(getActivity()).getFromLocationName(address, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		double lat = foundGeocode.get(0).getLatitude();
		double lon = foundGeocode.get(0).getLongitude();

		l = new LatLng(lat, lon);		
		map = mapa.getMap();

		MarkerOptions options = new MarkerOptions().position(l).title(nom);
		map.addMarker(options);
		
		moveToCurrentLocation(l, map);
	
		return rootView;
	}
	
	private void moveToCurrentLocation(LatLng currentLocation, GoogleMap map) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomIn());
		// Zoom out to zoom level 10, animating with a duration of 2 seconds.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_mapa, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
			case R.id.menu_snapshot:
				SnapshotReadyCallback callback = new SnapshotReadyCallback() {
		            Bitmap bitmap;

					@Override
		            public void onSnapshotReady(Bitmap snapshot) {
		                // TODO Auto-generated method stub
		                bitmap = snapshot;
		                try {
		                	String mPath = Environment.getExternalStorageDirectory().toString();
		                    FileOutputStream out = new FileOutputStream(mPath + "/ "+ nom + ".png");
		                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		                    Toast.makeText(getActivity(), R.string.capturaOK, Toast.LENGTH_LONG).show();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    Toast.makeText(getActivity(), R.string.capturaNoOK, Toast.LENGTH_LONG).show();
		                }
		            }
		        };
		        map.snapshot(callback);
				return true;

			default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
	protected String recuperarValors() {
		Bundle extras = getArguments();
		if (extras != null) {
			nom = extras.getString("nom");
		}
		return nom;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapa.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapa.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapa.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapa.onLowMemory();
	}
}
