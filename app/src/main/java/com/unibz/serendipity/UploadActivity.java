package com.unibz.serendipity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        TextView t2 = (TextView) findViewById(R.id.link);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
