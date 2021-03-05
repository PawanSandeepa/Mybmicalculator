package com.example.mybmicalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    Context context;
    DbHandler dbHandler;
    ListView listView;
    private List<model> models;

    //------------admob-------------------
    private FrameLayout frameLayout;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //--------------------admob----------------
        frameLayout = findViewById(R.id.botem_banner_ad);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bottem_baner_ad));
        frameLayout.addView(adView);
        loadBanner();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        context = this;
        dbHandler = new DbHandler(context);
        models = new ArrayList<>();

        listView = findViewById(R.id.table);

        models = dbHandler.history_data();
        historyAdaptor adapter = new historyAdaptor(context,R.layout.adapter_table,models);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final model model = models.get(position);
                //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //String now_id = model.getId().toString();

                //Toast.makeText(getApplicationContext(),""+now_id,Toast.LENGTH_LONG).show();
                builder.setTitle(model.getDate().toString());
                builder.setMessage(model.getBmi().toString()+" really do you want to delete this recode");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //-----------No----------------------

                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //--------------yes--------------------
                        //Toast.makeText(getApplicationContext(),""+model.getId(),Toast.LENGTH_LONG).show();
                        dbHandler.delete(model.getId());
                        //startActivity(new Intent(context,history.class));
                        recreate();
                    }
                });
                builder.show();
            }
        });

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
