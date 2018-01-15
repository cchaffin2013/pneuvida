package com.example.christopher.pneuvida;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class VitalsGraph extends AppCompatActivity {

    LineChart vitalsGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals_graph);

        //sets up graph
        vitalsGraph = (LineChart) findViewById(R.id.vitalGraph);

        ArrayList<String> xAxis = new ArrayList<>();//time
        ArrayList<Entry> yAxis = new ArrayList<>();//value

        //temporary values, will later be replaced by recorded values
        double x = 0;
        int numOfDataPoints = 1000;

        for (int i = 0; i < numOfDataPoints; i++) {
            float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            x = x + 0.1;

            xAxis.add(i, String.valueOf(x));
            yAxis.add(new Entry(i, sinFunction));
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();//for multiple data sets on same plot use ILineDataSet

        LineDataSet lineDataSet1 = new LineDataSet(yAxis, "sin");
        lineDataSet1.setDrawCircles(false); //turns off dots drawn for each point
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet1.setDrawValues(false);
        lineDataSets.add(lineDataSet1);

        vitalsGraph.setData(new LineData(lineDataSets));

        vitalsGraph.setVisibleXRangeMaximum(65f);
    }
}
