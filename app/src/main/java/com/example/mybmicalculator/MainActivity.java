package com.example.mybmicalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.nitri.gauge.Gauge;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    //import class...
    Button calculat,go_more,clear;
    EditText heighttxt,height_pointtxt,weighttxt,weight_pointtxt;
    RadioGroup gendertxt,modeltxt;
    TextView answertxt,statetxt;
    FloatingActionButton floting_button;
    DbHandler dbHandler;
    Context context;
    Gauge scale;
    SimpleDateFormat sfd = new SimpleDateFormat("mm:ss");


    Double height,height_point,weight,weight_point;
    String person_target = "",sbmi;
    Double bmi;
    String gender,model;
    Double total_height=0.0,total_weight=0.0;

    //------------admob-------------------
    private FrameLayout frameLayout;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set reference...
        calculat = findViewById(R.id.button);
        heighttxt = findViewById(R.id.height);
        height_pointtxt = findViewById(R.id.height_point);
        weighttxt = findViewById(R.id.weight);
        weight_pointtxt = findViewById(R.id.weight_point);
        gendertxt = findViewById(R.id.gender);
        modeltxt = findViewById(R.id.model);
        answertxt = (TextView)findViewById(R.id.answer);
        statetxt = findViewById(R.id.state);
        go_more = findViewById(R.id.go_more);
        clear = findViewById(R.id.clear);
        floting_button = findViewById(R.id.floatingActionButton);
        context = this;
        dbHandler = new DbHandler(context);
        scale = findViewById(R.id.gauge);

        //---------------------------------------------admob---------------------------------------
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
                                              Intent go_more_details = new Intent(getApplicationContext(),more_details.class);
                                              go_more_details.putExtra("target",person_target);
                                              go_more_details.putExtra("set_bmi",bmi);
                                              startActivity(go_more_details);
                                              person_target = "";
                                          }
                                      }
        );


        //set hint.........
        modeltxt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                model = ((RadioButton)findViewById(modeltxt.getCheckedRadioButtonId())).getText().toString();
                Toast.makeText(getBaseContext(), "you selected "+model, Toast.LENGTH_SHORT).show();
                if (model.equals("Imperial")){
                    heighttxt.setHint("foot");
                    height_pointtxt.setHint("inch");
                    weighttxt.setHint("stones");
                    weight_pointtxt.setHint("pounds");
                }else{
                    heighttxt.setHint("meter");
                    height_pointtxt.setHint("centimeter");
                    weighttxt.setHint("kilogramme");
                    weight_pointtxt.setHint("gramme");
                }
            }
        });

        //calculating process....
        calculat.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("WrongConstant")
            public void onClick(View v){
                model = ((RadioButton)findViewById(modeltxt.getCheckedRadioButtonId())).getText().toString();
                String heights = heighttxt.getText().toString();
                String heights_point = height_pointtxt.getText().toString();
                String weights = weighttxt.getText().toString();
                String weights_point = weight_pointtxt.getText().toString();

                if (!heights.isEmpty()&&!weights.isEmpty()){
                    //height = Double.parseDouble(heights);
                    //cast string to integer....
                    height = Double.parseDouble(heights);
                    weight = Double.parseDouble(weights);
                    if (!heights_point.isEmpty()){
                        height_point = Double.parseDouble(heights_point);
                    }else {
                        height_point = 0.0;
                    }
                    if (!weights_point.isEmpty()){
                        weight_point = Double.parseDouble(weights_point);
                    }else{
                        weight_point = 0.0;
                    }
                    //get gender...
                    int selectedId = gendertxt.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                    gender = selectedRadioButton.getText().toString();

                    //convert if input data model is imperial, to metric....
                    if (model.equals("Imperial")){
                        total_height = (height*12+height_point)*0.0254;
                        total_weight = (weight*14+weight_point)*0.453592;
                    }else{
                        total_height = height+(height_point/100);
                        total_weight = weight+(weight_point/1000);
                    }

                    bmi=total_weight/(total_height*total_height);
                    sbmi = String.format("%.2f", bmi);
                    //set answer...
                    answertxt.setText("Your BMI value is "+ sbmi);

                    //get and set person state
                    String state = check_state();
                    statetxt.setText("You are "+state);

                    go_more.setVisibility(View.VISIBLE);
                    floting_button.setVisibility(View.VISIBLE);
//                    targettxt.setText("you have change your weight "+weight_target+" kg(s) for be normal your BMI\n" +
//                            "else you can change your height "+height_target+" m(s) for be normal your BMI\n" +
//                            "");
                    //Toasty.success(getApplicationContext(),"your bmi value is "+bmi+" you are "+gender, Toast.LENGTH_LONG).show();
                }else{
                    Toasty.error(getApplicationContext(),"please enter all filed", Toast.LENGTH_LONG).show();
                }

//                Toasty.error(getApplicationContext(),"please enter all filed", Toast.LENGTH_LONG).show();
            }
        });

        go_more.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent go_more_details = new Intent(getApplicationContext(),more_details.class);
                go_more_details.putExtra("target",person_target);
                go_more_details.putExtra("set_bmi",bmi);

//                startActivity(go_more_details);
//                person_target = "";

                //-------------------------------------------admob----------------------------------
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(go_more_details);
                    person_target = "";
                }


            }
        });

        clear.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                heighttxt.setText("");
                height_pointtxt.setText("");
                weighttxt.setText("");
                weight_pointtxt.setText("");
                person_target = "";
                statetxt.setText("");
                answertxt.setText("Enter your details");
                go_more.setVisibility(View.INVISIBLE);
                floting_button.setVisibility(View.INVISIBLE);
                scale.setLowerText("");
                scale.moveToValue(0);
            }
        });

        floting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //long time = System.currentTimeMillis();
                Date c = Calendar.getInstance().getTime();
                //Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yy.DDD");
                String formattedDate = df.format(c);
                String user_name = "user";

                model model = new model(user_name,gender,total_weight,total_height,bmi,formattedDate);
                dbHandler.add_bmi(model);
            }

        });





    }
//-----------------------------------------------------end of onCreate----------------------------


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
                Toast.makeText(this,"Move to home",Toast.LENGTH_LONG).show();

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

    private String check_state() {
        float bmif = bmi.floatValue();

        scale.setDeltaTimeInterval(30);
        scale.setLowerText("BMI "+sbmi);
        scale.moveToValue(bmif);
        if (gender.equals("male")||gender.equals("Male")){
            if(bmi<20){
                person_target = "Hi thin boy...\n" +
                        "* you have increase your weight "+
                        String.format("%.2f", ((20*(total_height*total_height))-total_weight))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can decrease your height "+
                        String.format("%.2f", (total_height-(Math.sqrt(total_weight/20))))+
                        " m(s) for be normal your BMI\n";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (20*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (25*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";


                return ("Under weight");
            }else if(bmi<26){
                person_target = "Congratulations... Your BMI value is good";


                return ("Normal");
            }else if(bmi<31){
                person_target = "Hi fat boy...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(25*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/25))-total_height))+
                        " m(s) for be normal your BMI";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (20*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (25*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";

                return ("Over weight");
            }else if(bmi<40){
                person_target = "Hi fat boy...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(25*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/25))-total_height))+
                        " m(s) for be normal your BMI\n";
                person_target = person_target+"\nand if you can decrease your weight about "+
                        String.format("%.2f", (total_weight-(30*(total_height*total_height))))+
                        "kg(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nelse if you can increase your height about "+
                        String.format("%.2f", ((Math.sqrt(total_weight/30))-total_height))+
                        " m(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (20*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (25*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";

                return ("Obesity");
            }else{
                person_target = "Hi fat boy...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(25*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/25))-total_height))+
                        " m(s) for be normal your BMI\n";
                person_target = person_target+"\nand if you can decrease your weight about "+
                        String.format("%.2f", (total_weight-(30*(total_height*total_height))))+
                        "kg(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nelse if you can increase your height about "+
                        String.format("%.2f", ((Math.sqrt(total_weight/30))-total_height))+
                        " m(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (20*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (25*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";
                person_target = person_target+"\nWe recommend you get medical advice about your weight\n";

                return ("Severe Obesity");
            }
        }else{
            //----------------------------------for girls...-------------------------------------
            if(bmi<19){
                person_target = "Hi thin girl...\n" +
                        "you have increase your weight "+
                        String.format("%.2f", ((19*(total_height*total_height))-total_weight))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can decrease your height "+
                        String.format("%.2f", (total_height-(Math.sqrt(total_weight/19))))+
                        " m(s) for be normal your BMI";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (19*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (24*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";
                person_target = person_target + "\nIf you want, your weight move to normal weight "+
                        "you can eat cereal, dates and honey";

                return ("Under weight");
            }else if(bmi<25){
                person_target = "Congratulations... Your BMI value is good";


                return ("Normal");
            }else if(bmi<30){
                person_target = "Hi fat girl...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(24*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/24))-total_height))+
                        " m(s) for be normal your BMI";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (19*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (24*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";

                return ("Over weight");
            }else if(bmi<40){
                person_target = "Hi fat boy...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(24*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/24))-total_height))+
                        " m(s) for be normal your BMI\n";
                person_target = person_target+"\nand if you can decrease your weight about "+
                        String.format("%.2f", (total_weight-(29*(total_height*total_height))))+
                        "kg(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nelse if you can increase your height about "+
                        String.format("%.2f", ((Math.sqrt(total_weight/29))-total_height))+
                        " m(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (19*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (24*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";

                return ("Obesity");
            }else{
                person_target = "Hi fat boy...\n" +
                        "you have decrease your weight "+
                        String.format("%.2f", (total_weight-(24*(total_height*total_height))))+
                        "kg(s) for be normal your BMI\n";
                person_target = person_target+"\nelse you can increase your height "+
                        String.format("%.2f", ((Math.sqrt(total_weight/24))-total_height))+
                        " m(s) for be normal your BMI\n";
                person_target = person_target+"\nand if you can decrease your weight about "+
                        String.format("%.2f", (total_weight-(29*(total_height*total_height))))+
                        "kg(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nelse if you can increase your height about "+
                        String.format("%.2f", ((Math.sqrt(total_weight/29))-total_height))+
                        " m(s) your BMI will be Over weight aria\n";
                person_target = person_target+"\nYou should maintain your weight between "+
                        String.format("%.2f", (19*(total_height*total_height)))+
                        " and "+
                        String.format("%.2f", (24*(total_height*total_height)))+
                        " for normal BMI, under the this height.\n";
                person_target = person_target+"\nWe recommend you get medical advice about your weight\n";

                return ("Severe Obesity");
            }
        }
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
