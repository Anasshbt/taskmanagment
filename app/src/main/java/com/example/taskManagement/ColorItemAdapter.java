package com.example.taskManagement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskManagement.R;

import java.util.ArrayList;

public class ColorItemAdapter extends ArrayAdapter<Integer> {

    private ArrayList<Integer> colors;
    private Context context;

    public ColorItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Integer> colors) {
        super(context, resource, colors);
        this.context = context;
        this.colors = colors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View colorItemView = convertView;
        if (colorItemView == null) {
            colorItemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);
        }

        // Set background color for the first square
        View colorSquare1 = colorItemView.findViewById(R.id.colorSquare1);
        colorSquare1.setBackgroundColor(colors.get(position * 2));

        // Check if there is a second color for this row
        if (position * 2 + 1 < colors.size()) {
            View colorSquare2 = colorItemView.findViewById(R.id.colorSquare2);
            colorSquare2.setVisibility(View.VISIBLE);
            colorSquare2.setBackgroundColor(colors.get(position * 2 + 1));
        } else {
            // Hide the second square if there is no second color
            View colorSquare2 = colorItemView.findViewById(R.id.colorSquare2);
            colorSquare2.setVisibility(View.INVISIBLE);
        }

        return colorItemView;
    }
}
