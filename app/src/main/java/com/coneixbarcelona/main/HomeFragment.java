package com.coneixbarcelona.main;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
//	ImageView imgView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        imgView = (ImageView) rootView.findViewById(R.id.imageView1);
//        imgView.setOnClickListener((OnClickListener) this);
         
        return rootView;
    }

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		Toast.makeText(getActivity(), "Obre el menú lateral a dalt a l'esquerra.", Toast.LENGTH_LONG).show();
//	}
}
