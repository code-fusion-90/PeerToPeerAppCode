package com.someday.p2pproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.someday.p2pproject.Retrofit.UserNodeJs;
import com.someday.p2pproject.Retrofit.RetrofitClient;
import com.someday.p2pproject.Retrofit.observableWrapper;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    UserNodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_email, edt_password;
   Button register_button, login_button;


    //****************************************call to register button event which calls the register method
    public void register_button(View view) {
        register(edt_email.getText().toString(), edt_password.getText().toString());
    }

    //*******************************************call to login button event which call a login method
    public void login_button(View view) {
        login(edt_email.getText().toString(), edt_password.getText().toString());

    }


    //On create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Retrofit instance
        Retrofit retrofit = RetrofitClient.getInstance();

        //set retrofit to the request class
        myAPI = retrofit.create(UserNodeJs.class);

        //Email & Password Textboxes
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);

    }


    //*******************************************Method to call backend login and throw error if bacend problems
    public void login(String email, String password){

        //Disposable observers for emitted data
        compositeDisposable
                .add(myAPI
                        .loginUser(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new observableWrapper<String>() {
                                @Override
                                protected void onSuccess(String s) {
                                    if(s.contains("email")) {
                                        Toast.makeText(MainActivity.this, "login successful " + s, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MainActivity.this,LoggedIn.class);
                                        intent.putExtra("UserObject", s);
                                        startActivity(intent);
                                    }
                                    else
                                        Toast.makeText(MainActivity.this, " "+s, Toast.LENGTH_SHORT).show();
                                    }
                                }));
        //Disposable Observer Ends Here
    }


    //******************************************* Register user using retrofit & RxJava
    private void register(String email, String password) {
        //Disposable observers for emitted data
        compositeDisposable
                .add(myAPI
                        .registerUser(email,"Test", password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new observableWrapper<String>() {
                            @Override
                            protected void onSuccess(String s) {
                                if(s.contains("User Already Exists")){
                                    Toast.makeText(MainActivity.this, " "+s, Toast.LENGTH_SHORT).show();
                                }else if(s.contains("User registered Sucessfully")){
                                    Toast.makeText(MainActivity.this, " "+s, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }



    //******************************************* On Stop method
    @Override
    protected void onStop() {
        //All observables disposed
        compositeDisposable.clear();
        super.onStop();
    }

    //******************************************* On Destrop method
    @Override
    protected void onDestroy() {
        //All observables disposed
        compositeDisposable.clear();
        super.onDestroy();
    }

}
