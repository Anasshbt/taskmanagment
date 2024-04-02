package com.example.taskManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_profile);

        MaterialToolbar toolbare =findViewById(R.id.topappbarprofile);
        DrawerLayout drawerprofile=findViewById(R.id.drawerprofile);
        NavigationView navigationView =findViewById(R.id.navigationviewprofile);

        LineChart lineChart = findViewById(R.id.lineChart);
        BarChart barChart = findViewById(R.id.barChart);
        mAuth = FirebaseAuth.getInstance();

        toolbare.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerprofile != null) {
                    drawerprofile.openDrawer(GravityCompat.END);
                }
            }
        });


      navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int itemId = item.getItemId();

               if (itemId == R.id.logout) {
                    Intent intent = new Intent(Profile.this, FirstActivity.class);
                    mAuth.signOut();
                    startActivity(intent);
                    finish();


                } else{


                    return true;
                }
                drawerprofile.closeDrawer(GravityCompat.END);
                finish();

                return true;
            }

        });





























        // Données fictives pour le graphique de lignes
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 12));
        entries.add(new Entry(4, 18));
        entries.add(new Entry(5, 20));
        entries.add(new Entry(6, 25));
        entries.add(new Entry(7, 22));

        LineDataSet lineDataSet = new LineDataSet(entries, "Daily Tasks");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Description du graphique de lignes (optionnel)
        Description lineDescription = new Description();
        lineDescription.setText("Daily Tasks Chart");
        lineChart.setDescription(lineDescription);

        // Configuration de l'axe X pour le graphique de lignes
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Évite de dupliquer les étiquettes

        // Configuration de l'axe Y pour le graphique de lignes
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f); // Évite de dupliquer les valeurs
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Désactive l'axe de droite

        // Mise à jour du graphique de lignes
        lineChart.invalidate();

        // Données aléatoires pour le graphique à colonnes
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 5)); // Jour 1
        barEntries.add(new BarEntry(1, 8)); // Jour 2
        barEntries.add(new BarEntry(2, 4)); // Jour 3
        barEntries.add(new BarEntry(3, 6)); // Jour 4
        barEntries.add(new BarEntry(4, 9)); // Jour 5
        barEntries.add(new BarEntry(5, 4)); // Jour 6
        barEntries.add(new BarEntry(6, 7)); // Jour 7

        BarDataSet barDataSet = new BarDataSet(barEntries, "Next Tasks");
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

// Description du graphique à colonnes (optionnel)
        Description barDescription = new Description();
        barDescription.setText("Next Tasks Chart");
        barChart.setDescription(barDescription);

// Configuration de l'axe X pour le graphique à colonnes
        XAxis xAxisBar = barChart.getXAxis();
        xAxisBar.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Jour 1", "Jour 2", "Jour 3", "Jour 4", "Jour 5", "Jour 6", "Jour 7"})); // Définit les étiquettes de l'axe X
        xAxisBar.setAxisMinimum(-0.5f); // Définit la valeur minimale de l'axe X pour éviter de couper la première barre
        xAxisBar.setAxisMaximum(6.6f); // Définit la valeur maximale de l'axe X pour éviter de couper la dernière barre
        xAxisBar.setLabelCount(7);

// Configuration de l'axe Y pour le graphique à colonnes
        YAxis yAxisLeftt = barChart.getAxisLeft(); // Récupère l'axe Y de gauche du graphique
        yAxisLeftt.setAxisMinimum(0f); // Définit la valeur minimale de l'axe Y à zéro
        yAxisLeft.setGranularity(1f); // Définit la plus petite différence entre les valeurs de l'axe Y

// Configuration de l'axe Y de droite pour le graphique à colonnes (si nécessaire)
        YAxis yAxisRightt = barChart.getAxisRight(); // Récupère l'axe Y de droite du graphique
        yAxisRightt.setEnabled(false); // Désactive l'axe Y de droite (si non utilisé)

    }
}
