package com.example.mybmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import de.nitri.gauge.Gauge;


public class more_details extends AppCompatActivity {
    TextView more_details;
    Gauge scale;
    Button bthealth;

    //------------admob-------------------
    private FrameLayout frameLayout;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    Double bmi;

    private static final String TAG = "more_details";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        more_details = findViewById(R.id.target);
        scale = findViewById(R.id.gauge);
        bthealth = findViewById(R.id.health);

        //-------------------------------------------admob----------------------------------
        frameLayout = findViewById(R.id.botem_banner_ad);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bottem_baner_ad));
        frameLayout.addView(adView);
        loadBanner();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener()
                                      {
                                          public void onAdClosed(){
                                              Intent go_health = new Intent(getApplicationContext(),health.class);
                                              go_health.putExtra("set_bmi",bmi);
                                              startActivity(go_health);
                                          }
                                      }
        );


        Intent intent = getIntent();

        String target = intent.getStringExtra("target");
        bmi = intent.getDoubleExtra("set_bmi",0);

        float bmif = bmi.floatValue();

        scale.setDeltaTimeInterval(30);
        String sbmi = String.format("%.2f", bmi);
        scale.setLowerText("BMI "+sbmi);
        scale.moveToValue(bmif);

        //int resID = getResources().getIdentifier(bmi, "mipmap", getPackageName());
        more_details.setText(target);
        more_details.setMovementMethod(new ScrollingMovementMethod());

        bthealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_health = new Intent(getApplicationContext(),health.class);
                go_health.putExtra("set_bmi",bmi);

                //-------------------------------------------admob----------------------------------
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(go_health);
                }

            }
        });




        
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
