package com.example.taskManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ThemeFragment extends Fragment {

    private final ArrayList<Integer> colors = new ArrayList<>();
    private OnThemeSelectedListener mListener;

    public ThemeFragment() {
        // Required empty public constructor
    }

    public static ThemeFragment newInstance() {
        return new ThemeFragment();
    }

    public interface OnThemeSelectedListener {
        void onThemeSelected(String theme);
        String getColorName(int color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnThemeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnThemeSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        GridLayout gridLayout = view.findViewById(R.id.colorGrid);

        colors.add(Color.parseColor("#FF0000"));
        colors.add(Color.parseColor("#00FF00"));
        colors.add(Color.parseColor("#0000FF"));
        colors.add(Color.parseColor("#FF00FF"));
        colors.add(Color.parseColor("#FFF000"));
        colors.add(Color.parseColor("#00FFFF"));
        colors.add(Color.parseColor("#FFA500"));
        colors.add(Color.parseColor("#800080"));
        colors.add(Color.parseColor("#775ADA"));
        colors.add(Color.parseColor("#28C7FA"));
        colors.add(Color.parseColor("#974EC3"));
        colors.add(Color.parseColor("#E03E36"));











        int numRows = (int) Math.ceil(colors.size() / 2.0); // Divide by 2 since there are 2 columns

        // Set the number of rows for the GridLayout
        gridLayout.setRowCount(numRows);
        // Add color squares to the GridLayout
        for (int color : colors) {
            View colorView = new View(getContext());

            View colorSquare = createColorSquare(color);
            gridLayout.addView(colorSquare);
        }

        return view;
    }

    private View createColorSquare(int color) {
        View colorSquare = new View(getContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDimensionPixelSize(R.dimen.color_square_size);
        params.height = getResources().getDimensionPixelSize(R.dimen.color_square_size);
        params.setMargins(8, 8, 8, 8); // Margins between color squares
        colorSquare.setLayoutParams(params);
        colorSquare.setBackgroundColor(color);
        colorSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTheme = mListener.getColorName(color);
                applyTheme(selectedTheme);
                Toast.makeText(getActivity(), "Selected Theme: " + selectedTheme, Toast.LENGTH_SHORT).show();
            }
        });
        return colorSquare;
    }

    private void applyTheme(String selectedTheme) {
        mListener.onThemeSelected(selectedTheme);

        SharedPreferences preferences = getActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTheme", selectedTheme);
        editor.apply();
    }

}
