package com.example.christopher.pneuvida;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class VitalsGraph extends AppCompatActivity {

    LineChart vitalsGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals_graph);

        //vitals intent receiver
        Bundle vitalsIntent = getIntent().getExtras();
        final String  vitalType = vitalsIntent.getString("vital");

        //title
        TextView graphTitle = (TextView) findViewById(R.id.vital_title);
        graphTitle.setText(vitalType);

        //sets graph options
        vitalsGraph = (LineChart) findViewById(R.id.vitalGraph);
        vitalsGraph.setDragEnabled(true);
        vitalsGraph.setTouchEnabled(true);
        vitalsGraph.setScaleEnabled(false);
        vitalsGraph.setPinchZoom(true);
        vitalsGraph.setBackgroundColor(Color.LTGRAY);
        vitalsGraph.setHorizontalScrollBarEnabled(true);
        vitalsGraph.setScrollContainer(true);
        //adds data to graph
        LineData data = new LineData();
        vitalsGraph.setData(data);

        Legend l = vitalsGraph.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        //set up x and y axis
        XAxis x1 = vitalsGraph.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(true);
        x1.setAvoidFirstLastClipping(true);
        x1.setGranularity(.5f);


        YAxis y1 = vitalsGraph.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setAxisMaximum(2f);
        y1.setAxisMinimum(-2f);
        y1.setDrawGridLines(true);

        YAxis y12 = vitalsGraph.getAxisRight();
        y12.setEnabled(false);

    }

    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(float i = 0; i < 100; i = i + .1f) {
                    final float x = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDataEntry(x);
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }

    private void addDataEntry(float x) {
        LineData data = vitalsGraph.getData();

        if(data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            Float sinFunction = Float.parseFloat(String.valueOf(Math.sin(x)));
            data.addEntry(new Entry(x, sinFunction, set.getEntryCount()), 0);
            vitalsGraph.notifyDataSetChanged();
            vitalsGraph.setVisibleXRangeMaximum(5f);
            vitalsGraph.moveViewToX(x-.5f);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Sample Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setCubicIntensity(0.2f);
        set.setColor(Color.BLUE);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.rgb(224, 117, 177));
        set.setDrawValues(false);
        return set;
    }
}
