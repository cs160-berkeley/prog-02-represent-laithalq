package com.cs160.laithalq.represent;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateDetailActivity extends Activity {

    TextView mCandidateName, mParty;
    ImageView mCandidateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_detail);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff009688));

        mCandidateName = (TextView) findViewById(R.id.candidate_name);
        mParty = (TextView) findViewById(R.id.party_name);
        mCandidateImage = (ImageView) findViewById(R.id.candidate_image);

        Intent myIntent = getIntent();
        String candidateName = myIntent.getStringExtra("Candidate Name");
        String partyName = myIntent.getStringExtra("Party Name");
        Integer candidateImageId = myIntent.getIntExtra("Candidate Image Id", R.mipmap.bilbo);
        if(getActionBar() != null)
            getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">About " + candidateName + "</font>"));

        mCandidateName.setText(candidateName);
        mParty.setText(partyName);
        if(partyName.equals("Republican"))
            mParty.setTextColor(0xffd62828);
        else if(partyName.equals("Democrat"))
            mParty.setTextColor(0xff33b5e5);
        else
            mParty.setTextColor(0xffdedede);
        mCandidateImage.setImageResource(candidateImageId);
    }
}
