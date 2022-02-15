package com.example.galaxyalarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BackgroundAdapter extends ArrayAdapter<BackgroundItem> {

    public BackgroundAdapter(Context context, ArrayList<BackgroundItem> backgroundList) {
        super(context, 0, backgroundList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    // Write method that we can use in the getView and getDropDownView (= no duplicate code)
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.background_spinner_row, parent, false
            );
        }

        ImageView imageViewBackground = convertView.findViewById(R.id.imageview_background);
        TextView textViewName = convertView.findViewById(R.id.textview_name);

        BackgroundItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewBackground.setImageResource(currentItem.getBackgroundImage());
            textViewName.setText(currentItem.getBackgroundName());
        }

        return convertView;
    }
}
