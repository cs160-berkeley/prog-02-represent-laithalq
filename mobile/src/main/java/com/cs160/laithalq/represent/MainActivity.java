package com.cs160.laithalq.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/*
2012 Presidential Vote (or something labeling this view)
State
County
Obama xx% of vote
Romney xx% of vote


If a user shakes the watch, it randomly selects a location 
*/

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    //

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qnVgWe7e7Szd8YNm7SXNBBRh7";
    private static final String TWITTER_SECRET = "dKwCZmy0RHnQNKOLDaQO4OZAopo2a5HT1mrvYjXpyK9hZ7Agrv";

    private Button mZipButton;
    private Button mLocationButton;
    private EditText mZipCodeField;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff009688));
        getActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">Represent</font>")));
        queue = Volley.newRequestQueue(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
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
                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    Intent allCandidatesIntent = new Intent(getBaseContext(), AllCandidatesActivity.class);
                    int enteredZipCode = Integer.parseInt(zipCodeStr);
                    buildIntents(sendIntent, allCandidatesIntent, false, enteredZipCode);
                    startService(sendIntent);
                    startActivity(allCandidatesIntent);
                }
            }
        });

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                Intent allCandidatesIntent = new Intent(getBaseContext(), AllCandidatesActivity.class);

                try {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                }
                catch(SecurityException e){
                    Toast toast = Toast.makeText(getApplicationContext(), "Couldn't fetch location data.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                buildIntents(sendIntent, allCandidatesIntent, true, -1);
                startService(sendIntent);
                startActivity(allCandidatesIntent);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    /*
     * Arguments:
     *  sendIntent - the intent to communicate new representatives to the watch
     *  allCandidatesIntent - the intent to open and dispaly the all candidates view
     *  useLocation - whether we are building the intent on location (stored in mLastLocation)
     *  zipcode - value ignored when using location, otherwise this is the value fetched from the zipcode field
     */
    private void buildIntents(Intent sendIntent, Intent allCandidatesIntent, boolean useLocation, int zipcode){
        String url = "http://google.com";
        String congress_url ="http://congress.api.sunlightfoundation.com/legislators/locate?latitude="+"&longitude="+"&apikey=3dad148f06b8495b868f6f542487f4a0";
        String geocoding_url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zipcode + "&key=AIzaSyBZTIBX6xkiomDzCPtbfIwerBHS2xaAQes"
        // TODO: Use a more specific parent
        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
        // TODO: Base this Tweet ID on some data from elsewhere in your app
        long tweetId = 631879971628183552L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(MainActivity.this, result.data);
                parentView.addView(tweetView);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("T", "Sunlight API Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to fetch county info", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("T", "Sunlight API Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to fetch county info", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    // Add the request to the RequestQueue.
        queue.add(stringRequest);

        sendIntent.putExtra("By Location", true);
        ArrayList<String> candidateInfo = new ArrayList<>();
        candidateInfo.add("Hillary Clinton");
        candidateInfo.add("Democrat");
        candidateInfo.add("Barack Obama");
        candidateInfo.add("Democrat");
        candidateInfo.add("Donald Trump");
        candidateInfo.add("Republican");
        sendIntent.putStringArrayListExtra("candidateInfo", candidateInfo);

        //Jump to the Candidate Overview activity

        allCandidatesIntent.putExtra("By Location", true);
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

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) throws SecurityException{

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation == null) {
            mLocationButton.setEnabled(false);  //couldnt fetch user location - disable button appropriately
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Couldn't fetch location data - connection failed.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
