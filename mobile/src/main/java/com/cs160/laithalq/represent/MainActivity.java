package com.cs160.laithalq.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

/*
2012 Presidential Vote (or something labeling this view)
State
County
Obama xx% of vote
Romney xx% of vote


If a user shakes the watch, it randomly selects a location 
*/

public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private Button mZipButton;
    private Button mLocationButton;
    private EditText mZipCodeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff009688));
        getActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">Represent</font>")));

        mZipButton = (Button) findViewById(R.id.zipcode_btn);
        mLocationButton = (Button) findViewById(R.id.location_btn);
        mZipCodeField = (EditText) findViewById(R.id.input_zipcode);

        mZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCodeStr = mZipCodeField.getText().toString();
                if(zipCodeStr == null || zipCodeStr.length() != 5){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid 5 digit zipcode.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    int enteredZipCode = Integer.parseInt(zipCodeStr);
                    //Update the watch of the new candidates
                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent.putExtra("By Location", false);
                    sendIntent.putExtra("Zip Code", enteredZipCode);
                    ArrayList<String> candidateInfo = new ArrayList<>();
                    candidateInfo.add("Hillary Clinton");
                    candidateInfo.add("Democrat");
                    candidateInfo.add("Barack Obama");
                    candidateInfo.add("Democrat");
                    candidateInfo.add("Donald Trump");
                    candidateInfo.add("Republican");
                    sendIntent.putStringArrayListExtra("candidates", candidateInfo);
                    startService(sendIntent);

                    Intent allCandidatesIntent = new Intent(getBaseContext(), AllCandidatesActivity.class);
                    allCandidatesIntent.putExtra("By Location", false);
                    allCandidatesIntent.putExtra("Zip Code", enteredZipCode);
                    startActivity(allCandidatesIntent);
                }
            }
        });

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update the watch of the new candidates
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("By Location", true);
                ArrayList<String> candidateInfo = new ArrayList<>();
                candidateInfo.add("Hillary Clinton");
                candidateInfo.add("Democrat");
                candidateInfo.add("Barack Obama");
                candidateInfo.add("Democrat");
                candidateInfo.add("Donald Trump");
                candidateInfo.add("Republican");
                sendIntent.putStringArrayListExtra("candidateInfo", candidateInfo);
                startService(sendIntent);

                //Jump to the Candidate Overview activity
                Intent allCandidatesIntent = new Intent(getBaseContext(), AllCandidatesActivity.class);
                allCandidatesIntent.putExtra("By Location", true);
                startActivity(allCandidatesIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
