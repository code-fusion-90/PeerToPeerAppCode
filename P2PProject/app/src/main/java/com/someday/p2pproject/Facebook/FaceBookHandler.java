package com.someday.p2pproject.Facebook;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.someday.p2pproject.LoggedIn;
import com.someday.p2pproject.MainActivity;
import com.someday.p2pproject.R;
import com.someday.p2pproject.Retrofit.FacebookNodeJs;
import com.someday.p2pproject.Retrofit.RetrofitClient;
import com.someday.p2pproject.Retrofit.UserNodeJs;
import com.someday.p2pproject.Retrofit.observableWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class FaceBookHandler {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CallbackManager callbackManager;
    LoginButton loginButton;
    TextView userText;
    String[] permissions;
    String graphApiParams = "id, name, link, email, gender, hometown";
    FacebookNodeJs fbAPI;
    Context loggedinContext;

    //constructor to class fabcebookhandler
    public FaceBookHandler(LoginButton loginButton, TextView userText, Context loggedinContext){
        this.callbackManager = CallbackManager.Factory.create();
        this.loginButton = loginButton;
        this.userText = userText;
        this.loggedinContext = loggedinContext;

    }


    //Setup fb callback manager
    public CallbackManager facebook_handler(){
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_gender","user_hometown"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        System.out.println("Login SuccessFul");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        System.out.println("Login Canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        System.out.println(exception.toString());
                    }
                });

        return callbackManager;

    }




    //Calling acessTokenTracter method
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){

                System.out.println("User Logged out");
            }else {
                //get user data using graph API
                getUserFbData(currentAccessToken);
            }
        }
    };


    //get Data using graph API request
    private void getUserFbData(AccessToken userAccessToken){

        //Request object returned by method newMeRequest
        GraphRequest request = GraphRequest.newMeRequest(
                userAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject jsonobject,
                            GraphResponse response) {

                        // Application code
                        try {

                            commitFbData(jsonobject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", graphApiParams);
        request.setParameters(parameters);
        request.executeAsync();

    };

    private void commitFbData(JSONObject jsonobject){

       JsonParser jsonParser = new JsonParser();
       JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonobject.toString());

       //Log.i("gson", gsonObject.toString());

        //Get Retrofit instance
        Retrofit retrofit = RetrofitClient.getGsonInstance();

        //set retrofit to the request class
        fbAPI = retrofit.create(FacebookNodeJs.class);
        //Disposable observers for emitted data
        //try {
            compositeDisposable
                    .add(fbAPI.registerfbUser(gsonObject)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new observableWrapper<String>() {
                                @Override
                                protected void onSuccess(String s) {
                                    Log.i("JsonObjectIs", ""+s.toString());
                                  // Toast.makeText(loggedinContext,""+s.toString(),Toast.LENGTH_SHORT).show();
                                }
                            })
                    );

        //} catch (JSONException e) {
          //  e.printStackTrace();
       // }
        //Disposable Observer Ends Here
    }

}
