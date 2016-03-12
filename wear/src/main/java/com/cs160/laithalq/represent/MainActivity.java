package com.cs160.laithalq.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFeedBtn = (Button) findViewById(R.id.feed_btn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String candidateName = extras.getString("Candidates");
            mFeedBtn.setText(candidateName);
        }

        mFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                Log.d("T", "in MainActivity, sending to phone: " + mFeedBtn.getText().toString());
                sendIntent.putExtra("ChosenCandidate", mFeedBtn.getText().toString());
                startService(sendIntent);
            }
        });
    }
}
