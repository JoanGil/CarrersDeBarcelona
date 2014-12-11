package com.coneixbarcelona.main;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

@SuppressLint("NewApi")
public class SobreMiFragment extends Fragment implements View.OnClickListener {
	
	public SobreMiFragment(){}
	
	Button btnEnviarCorreu, btnCompartir;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		
        View rootView = inflater.inflate(R.layout.sobremi, container, false);
        
//        ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);

     // Evitar que el teclat aparegui a la llista
     		getActivity().getWindow().setSoftInputMode(
     				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        
		btnEnviarCorreu = (Button) rootView.findViewById(R.id.btnEnviarCorreu);
		btnCompartir = (Button) rootView.findViewById(R.id.btnCompartir);

		btnEnviarCorreu.setOnClickListener(this);
		btnCompartir.setOnClickListener(this);
		
        return rootView;		
        
        

    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEnviarCorreu:
			// Intent enviar correu			
			Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"coneix.barcelona.app@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, R.string.appConeixBcn);
            email.putExtra(Intent.EXTRA_TEXT, R.string.recomanaAppConeixBcn);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, getResources().getString(R.string.enviarCorreu)));
			break;

		case R.id.btnCompartir:
			// Intent compartir xarxes socials
			Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");
		    intent.putExtra(Intent.EXTRA_TEXT, R.string.appConeixBcn);  
		    startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));	
			break;

		default:
			break;
		}
		
	}
}
