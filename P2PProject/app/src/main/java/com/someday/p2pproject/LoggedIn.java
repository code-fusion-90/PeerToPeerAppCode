package com.someday.p2pproject;

import android.content.Context;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.someday.p2pproject.Facebook.FaceBookHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoggedIn extends AppCompatActivity {


    TextView userText;
    LoginButton loginFbButton;
    CallbackManager fbCallbackManager;


    //******************************************* a test button
    public void test_button(View view) {
        Toast.makeText(this, "Linkedin Login", Toast.LENGTH_SHORT).show();
    }


    //******************************************* On creatte method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // Parse Data recieved by inetent
        Bundle userObject = getIntent().getExtras();
        if(userObject==null){
            return;
        }
        String userinfo = userObject.getString("UserObject");
        userText = (TextView)findViewById(R.id.userObjectText);
        try {
            JSONObject obj = new JSONObject(userinfo);
            userText.setText("Welcome "+obj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoggedIn.this, ""+e, Toast.LENGTH_SHORT).show();
        }

        //Facebook Login Procedure
        loginFbButton = (LoginButton) findViewById(R.id.login_button);
        Context loggedinContext = LoggedIn.this.getApplicationContext();
        FaceBookHandler faceBookHandler =  new FaceBookHandler(loginFbButton, userText, loggedinContext);
        fbCallbackManager = faceBookHandler.facebook_handler();

    }


    //******************************************* On activity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
