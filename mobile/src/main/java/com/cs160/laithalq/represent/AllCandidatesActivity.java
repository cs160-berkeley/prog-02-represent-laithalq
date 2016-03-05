package com.cs160.laithalq.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class AllCandidatesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_candidates);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff009688));
        Intent myIntent = getIntent();
        if(!myIntent.getBooleanExtra("By Location", true)){
            Integer zipCode = myIntent.getIntExtra("Zip Code", 0);
            if(getActionBar() != null)
                getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">Representatives by zip: " + zipCode.toString() + "</font>"));
        }
        else
            if(getActionBar() != null)
                getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">Representatives by location</font>"));
    }

    public void showDetailedCandidateView1(View myView){
        //Jump to the Candidate Overview activity
        Intent candidateDetailIntent = new Intent(getBaseContext(), CandidateDetailActivity.class);
        candidateDetailIntent.putExtra("Candidate Name", "Hillary Clinton");
        candidateDetailIntent.putExtra("Party Name", "Democrat");
        candidateDetailIntent.putExtra("Candidate Image Id", R.mipmap.hillary);
        startActivity(candidateDetailIntent);
    }

    public void showDetailedCandidateView2(View myView){
        //Jump to the Candidate Overview activity
        Intent candidateDetailIntent = new Intent(getBaseContext(), CandidateDetailActivity.class);
        candidateDetailIntent.putExtra("Candidate Name", "Barack Obama");
        candidateDetailIntent.putExtra("Party Name", "Democrat");
        candidateDetailIntent.putExtra("Candidate Image Id", R.mipmap.obama);
        startActivity(candidateDetailIntent);
    }

    public void showDetailedCandidateView3(View myView){
        //Jump to the Candidate Overview activity
        Intent candidateDetailIntent = new Intent(getBaseContext(), CandidateDetailActivity.class);
        candidateDetailIntent.putExtra("Candidate Name", "Donald Trump");
        candidateDetailIntent.putExtra("Party Name", "Republican");
        candidateDetailIntent.putExtra("Candidate Image Id", R.mipmap.trump);
        startActivity(candidateDetailIntent);
    }
}
