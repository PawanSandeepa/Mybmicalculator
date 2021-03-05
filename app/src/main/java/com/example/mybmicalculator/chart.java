package com.example.mybmicalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jjoe64.graphview.series.LineGraphSeries;


public class chart extends AppCompatActivity {

    Context context;
    DbHandler dbHandler;
    private LineChart bmi_chart;
    //GraphView graphView;
    LineGraphSeries graphSeries;

    //------------admob-------------------
    private FrameLayout frameLayout;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //--------------------admob----------------
        frameLayout = findViewById(R.id.botem_banner_ad);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bottem_baner_ad));
        frameLayout.addView(adView);
        loadBanner();


        context = this;
        dbHandler = new DbHandler(context);
        bmi_chart = (LineChart)findViewById(R.id.line_chart);

        bmi_chart.setDragEnabled(true);
        bmi_chart.setScaleEnabled(false);

        //-----------------create guide line--------------------------------
        LimitLine normal_upper = new LimitLine(25f,"Over weight");
        normal_upper.setLineWidth(1f);
        normal_upper.enableDashedLine(10f,5f,0f);
        normal_upper.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        normal_upper.setTextSize(10f);
        normal_upper.setLineColor(Color.BLUE);

        LimitLine normal_lower = new LimitLine(19f,"Under weight");
        normal_lower.setLineWidth(1f);
        normal_lower.enableDashedLine(10f,5f,0f);
        normal_lower.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        normal_lower.setTextSize(10f);
        normal_lower.setLineColor(Color.BLUE);

        LimitLine Obesity = new LimitLine(30f,"Obesity");
        Obesity.setLineWidth(1f);
        Obesity.enableDashedLine(10f,5f,0f);
        Obesity.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        Obesity.setTextSize(10f);
        Obesity.setLineColor(Color.YELLOW);

        LimitLine severe_Obesity = new LimitLine(40f,"severe Obesity");
        severe_Obesity.setLineWidth(1f);
        severe_Obesity.enableDashedLine(10f,5f,0f);
        severe_Obesity.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        severe_Obesity.setTextSize(10f);
        severe_Obesity.setLineColor(Color.RED);

        //-----------------------------set guid line-----------------------------------------
        YAxis leftAxis = bmi_chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(normal_upper);
        leftAxis.addLimitLine(normal_lower);
        leftAxis.addLimitLine(Obesity);
        leftAxis.addLimitLine(severe_Obesity);
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(10f);
        leftAxis.enableGridDashedLine(10f,10f,0);
        leftAxis.setDrawLimitLinesBehindData(true);

        LineData data = dbHandler.create_chart();
        bmi_chart.getDescription().setText("Date format is year.day of year");
        bmi_chart.getDescription().setTextSize(10f);
        bmi_chart.animateY(3000);
        bmi_chart.setData(data);

    }

    //----------------------------end of oncreate-------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Toast.makeText(this,"Move to calculator",Toast.LENGTH_LONG).show();
                Intent main = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main);
                break;
            case R.id.chart:
                Toast.makeText(this,"Move to chart",Toast.LENGTH_LONG).show();
                Intent chart = new Intent(getApplicationContext(),chart.class);
                startActivity(chart);
                break;
            case R.id.history:
                Toast.makeText(this,"Move to history",Toast.LENGTH_LONG).show();
                Intent history = new Intent(getApplicationContext(),history.class);
                startActivity(history);
                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }



    //-------------------------------admob----------------------------------------------------------
    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
