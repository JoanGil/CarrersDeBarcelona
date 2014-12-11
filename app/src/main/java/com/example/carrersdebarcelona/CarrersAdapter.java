package com.example.carrersdebarcelona;



import java.util.ArrayList;
import java.util.List;

import com.coneixbarcelona.main.R;
import com.example.sqlite.CarrersBD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarrersAdapter extends ArrayAdapter<Carrer> implements Filterable {

	private List<Carrer> carrerList;
	private Context context;
	private Filter carrerFilter;
	private List<Carrer> origCarrerList;
	public Context contexto = this.context;
	
	public CarrersAdapter(List<Carrer> carrerList, Context ctx) {
		super(ctx, R.layout.listview_item, carrerList);
		this.carrerList = carrerList;
		this.context = ctx;
		this.origCarrerList = carrerList;
	}
	
	@Override
	public int getCount() {
		return carrerList.size();
	}

	@Override
	public Carrer getItem(int position) {
		return carrerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return carrerList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		CarrerHolder holder = new CarrerHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listview_item, null);
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.tvContent);
			ImageView im = (ImageView) v.findViewById(R.id.favourite);
			
			holder.carrerNameView = tv;
			holder.FavouriteImage = im;
			v.setTag(holder);
		}
		else  
			holder = (CarrerHolder) v.getTag();
		
		Carrer c = carrerList.get(position);
		//Posar a cada ítem de la llista el nom del carrer.
		holder.carrerNameView.setText(c.getNom());
		
		
		//Posar imatge*********************************************************************************************
		CarrersBD bd = new CarrersBD(getContext());
		boolean fav = false;
		try {
			bd.abrir();
			fav = bd.exists(c.getNom().replace("'", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		bd.cerrar();
        if (!fav) {
        	holder.FavouriteImage.setImageResource(R.drawable.favourite_bn3);
        }
        else {
        	holder.FavouriteImage.setImageResource(R.drawable.favourite_color);
        }
		//posar imatge*********************************************************************************************

		return v;
	}
	
	public void resetData() {
		carrerList = origCarrerList;
	}
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	private static class CarrerHolder {
		public TextView carrerNameView;
		public ImageView FavouriteImage;
	}
	
	/* We create our filter */
	@Override
	public Filter getFilter() {
		if (carrerFilter == null)
			carrerFilter = new CarrerFilter();
		
		return carrerFilter;
	}

	private class CarrerFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origCarrerList;
				results.count = origCarrerList.size();
			}
			else {
				// We perform filtering operation
				List<Carrer> nCarrerList = new ArrayList<Carrer>();
				
				for (Carrer p : carrerList) {
					//Es fltren els carrer que contenen les lletres escrites al textview
					if (p.getNom().toUpperCase().contains(constraint.toString().toUpperCase()))
						nCarrerList.add(p);
				}
				
				results.values = nCarrerList;
				results.count = nCarrerList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				carrerList = (List<Carrer>) results.values;
				notifyDataSetChanged();
			}
			
		}

		
	}
	
	  

}
