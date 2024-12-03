package com.example.projetmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class ProfileAdapter extends BaseAdapter {

    private Context context;
    private String[] options;
    private int[] icons;

    // Constructor to initialize context, data, and icons
    public ProfileAdapter(Context context, String[] options, int[] icons) {
        this.context = context;
        this.options = options;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return options.length;  // Return the number of items
    }

    @Override
    public Object getItem(int position) {
        return options[position];  // Return the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;  // Return the position as the item ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the row layout if it's null
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_profile, parent, false);
        }

        // Get references to the ImageView and TextView in the row layout
        ImageView rowIcon = convertView.findViewById(R.id.row_icon);
        TextView rowText = convertView.findViewById(R.id.row_text);

        // Set the icon and text for the current item
        rowIcon.setImageResource(icons[position]);  // Set the appropriate icon for the row
        rowText.setText(options[position]);  // Set the corresponding text for the row

        return convertView;
    }
}

