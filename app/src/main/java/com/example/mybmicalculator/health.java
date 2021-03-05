package com.example.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class health extends AppCompatActivity {

    //------------admob-------------------
    private FrameLayout frameLayout;
    private AdView adView;

    TextView healthtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        //--------------------admob----------------
        frameLayout = findViewById(R.id.botem_banner_ad);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bottem_baner_ad));
        frameLayout.addView(adView);
        loadBanner();

        healthtxt = findViewById(R.id.health);

        Intent intent = getIntent();

        Double bmi = intent.getDoubleExtra("set_bmi",0);
        String good_health;

        if(bmi<19){
            //low weight-------------
            good_health = "* Determining the right weight depends on what workout you’re doing — whether it’s something you’ve been doing for a while or it’s brand new to you. If you’ve never done an exercise before, Sulaver recommends a warmup self-assessment.\n" +
                    "\"Start with 50 percent less than what you might expect to lift, and do a few reps with that,\" he says. For example, if you normally lift 20-pound weights, start with 10s.\n" +
                    "“That should feel easy, so really make sure your form is spot-on. Then gradually work your way up in weight, doing a few reps at a time,” Sulaver suggests.\n" +
                    "Once you hit a weight that feels challenging, where you need to slow down to complete your reps with good form, use that for your first set.\n" +
                    "Bonus: Not only do you now have a good idea of the weight you should use, but you’re also warmed up for your first set.\n"+
            "\n* If you’re more experienced with dumbbells, kettlebells, or a barbell, the question is when and how to add more weight.\n" +
                    "So, if you’re ready to add more weight, take a look at (1) the speed of the lift, (2) your form, and (3) how you feel after you complete your sets, Sulaver suggests. Those are good indications of whether you should be going heavier.\n" +
                    "\"If your last couple reps are slow and uber-strenuous, leaving you sweaty and short of breath, then you’re using the right weight,\" he says. \"If you’re performing the last couple reps easily at normal speed, you could probably go heavier.\""+
            "\n* When we say that it’s time to up the numbers, we’re not talking humongous increases.\n" +
                    "If you’re over here thinking you need to lift the weight of a bus, take heart. A 2019 study found that as long as you lift until “failure” (as in, total muscle fatigue), it really doesn’t matter how much weight you’re lifting.Trusted Source\n" +
                    "However, if you want to see gains and create that lean yet strong physique, you can’t push the same weight week in and week out and expect not to plateau. Sulaver recommends adding weight every week.\n" +
                    "\"But in baby steps — sometimes it’s only 2.5 percent heavier than the prior week,\" he says. There’s a balance between pushing yourself and listening to your body’s limits that you’ll be able to find as you start lifting more."+
            "\n Resources www.greatist.com\n";
        }else if(bmi<25){
            //normal-----------------
            good_health = "You have good health but we recommend\n"+
            "\n* Drinking water, coffee or tea can help boost your metabolic rate and assist with weight loss. Caffeine and EGCG have been shown to promote fat burning.\n"+
                    "\n Resources www.healthline.com";
        }else{
            //over weight------------
            good_health = "* Research has found that low-carb diets help control hunger, provide feelings of fullness and promote long-term weight loss.\n"+
            "\n* Performing exercise, especially strength training, can help offset the drop in metabolic rate that occurs during weight loss.\n"+
            "\n* Tracking your calorie and macronutrient intakes can provide accountability and help you see whether you need to make some dietary adjustments in order to begin losing weight again.\n"+
            "\n* Increasing protein intake can help reverse a weight loss stall by boosting metabolism, reducing hunger and preventing muscle mass loss.\n"+
            "\n* The increased cortisol production that's associated with stress can interfere with weight loss. Stress-reduction strategies may help promote weight loss.\n"+
            "\n* Intermittent fasting may help you consume fewer calories, maintain muscle mass and preserve your metabolic rate during weight loss.\n"+
            "\n* Alcohol may interfere with weight loss by providing empty calories, making it easier to overeat and increasing belly fat storage.\n"+
            "\n* Fiber promotes weight loss by slowing the movement of food through your digestive tract, decreasing appetite and reducing the number of calories your body absorbs from food.\n"+
            "\n* Drinking water, coffee or tea can help boost your metabolic rate and assist with weight loss. Caffeine and EGCG have been shown to promote fat burning.\n"+
            "\n* To boost your metabolic rate and promote weight loss, include at least 20 grams of protein at each meal.\n"+
            "\n* Insufficient sleep can interfere with weight loss by reducing your metabolic rate and shifting your hormone levels to promote hunger and fat storage.\n"+
            "\n* Increasing your daily non-exercise physical activity can help boost your metabolic rate and promote weight loss.\n"+
            "\n* Vegetables are loaded with important nutrients, yet low in calories and carbs. Including them at every meal may help you reverse a weight loss plateau.\n"+
            "\n* Your scale weight may not reflect a loss of body fat, especially if you work out or experience fluid retention. Evaluate how you feel, how your clothes fit and whether your measurements have changed instead.\n"+
            "\n Resources www.healthline.com";
        }

        healthtxt.setText(good_health);
        healthtxt.setMovementMethod(new ScrollingMovementMethod());
    }

    //----------------------------------end of oncreate---------------------------------------------

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
