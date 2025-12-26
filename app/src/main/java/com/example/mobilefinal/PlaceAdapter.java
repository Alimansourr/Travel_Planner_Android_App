package com.example.mobilefinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PlaceAdapter extends ArrayAdapter<PlaceResponse.Feature> {

    public PlaceAdapter(Context context, List<PlaceResponse.Feature> features) {
        super(context, 0, features);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PlaceResponse.Feature feature = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place, parent, false);
        }

        // Lookup view for data population
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);

        // Populate the data into the template view using the data object
        textViewName.setText(feature.getProperties().getName());
        textViewAddress.setText(feature.getProperties().getFormatted());

        // Return the completed view to render on screen
        return convertView;
    }
}
