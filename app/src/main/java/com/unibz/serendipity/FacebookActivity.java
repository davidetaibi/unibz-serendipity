package com.unibz.serendipity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;

public class FacebookActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, this);
        Log.d("on", "create");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //manage login result
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // App code

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://ihatedatabases.com"))
                .build();


        ShareButton shareButton = (ShareButton) findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);

        LikeView likeView = (LikeView) findViewById(R.id.fb_like_button);
        likeView.setObjectIdAndType("https://www.facebook.com/ApeMichael/", LikeView.ObjectType.PAGE);
    }


    @Override
    public void onError(FacebookException exception) {
        // App code
        Log.e("FacebookException", exception.getMessage());
    }

}



