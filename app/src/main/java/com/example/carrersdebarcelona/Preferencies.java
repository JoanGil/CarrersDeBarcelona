package com.example.carrersdebarcelona;

import com.coneixbarcelona.main.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

public class Preferencies extends PreferenceActivity {

	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);
		setTitle(R.string.PreferenciesFTitol);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String cat = getResources().getString(R.string.idiomaCatala);
		String cast = getResources().getString(R.string.idiomaCastella);
		String ang = getResources().getString(R.string.idiomaAngles);
		final String seleccion = getResources().getString(R.string.toastSeleccio);
		
		final String[] idiomes = {cat, cast, ang};

		
		ListPreference listPreference = (ListPreference) findPreference("idiomaApp");
		
	    listPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        
	        @Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	int posicio = Integer.valueOf(newValue.toString());
				Toast.makeText(Preferencies.this, idiomes[posicio] + " " + seleccion, Toast.LENGTH_LONG).show();
	            return true;
	        }
	    });
	    
	    
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
