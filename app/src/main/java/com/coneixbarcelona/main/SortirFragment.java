package com.coneixbarcelona.main;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class SortirFragment extends Fragment {
	
	public SortirFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		
        View rootView = inflater.inflate(R.layout.fragment_pages, container, false);
         finish();
        return rootView;
    }

	private void finish() {
		// TODO Auto-generated method stub
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
