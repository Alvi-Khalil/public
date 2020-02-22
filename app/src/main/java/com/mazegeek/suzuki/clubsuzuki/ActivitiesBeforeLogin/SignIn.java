/*
 * Created by Alvi Khalil on 10/23/18 9:39 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/23/18 9:39 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.LogPrinter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.AUTHENTICATION_CODE_API;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LOGIN_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.hashedString;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class SignIn extends AppCompatActivity {

    CardView signIn;
    EditText editName,editPass;
    String myResponse,message,token;
    ImageView passEye;
    Boolean isHiddenPass=true;
    TextView forgetPass;


    private static final String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        signIn = findViewById(R.id.card);
        editName = findViewById(R.id.edit_name);
        editPass = findViewById(R.id.edit_pass);
        passEye = findViewById(R.id.eye);
        forgetPass = findViewById(R.id.forget_pass);


        passEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHiddenPass) {
                    // show password
                    editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editPass.setSelection(editPass.getText().length());
                    isHiddenPass=false;
                    passEye.setImageResource(R.drawable.eyex);

                } else {
                    // hide password

                    editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editPass.setSelection(editPass.getText().length());
                    passEye.setImageResource(R.drawable.eyex2);
                    isHiddenPass=true;
                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString().equals("")||editPass.getText().toString().equals("")){
                    Toast.makeText(SignIn.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else{



                    String encryptedString = hashedString(editPass.getText().toString());
                    loginApi(editName.getText().toString(),encryptedString);



                }

            }
        });

    forgetPass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SignIn.this, "Forget Password coming soon!", Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void loginApi(String phone,String pass) {





        final ProgressDialog progressDialogAPi = new ProgressDialog(this);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setMessage("Please wait ..");
        progressDialogAPi.show();

        RequestBody formBody = new FormBody.Builder()
                .add("user_name", phone)
                .add("password", pass)
                .build();

        getUrlInstance(LOGIN_API,formBody, new CustomCallBack(progressDialogAPi,this) {
            @Override
            public void sendResponse(Response response) {
                JSONObject responseJson=null;
                try {
                    myResponse = response.body().string();
                    responseJson = new JSONObject(myResponse);
                    message = responseJson.getString("message");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject dataObject=null;

                if (response.isSuccessful()) {
                    try {



                        Boolean success=responseJson.getBoolean("success");

                        if(success){
                            dataObject = responseJson.getJSONObject("data");
                            token = dataObject.getString("token");
                            setMyPreference(SignIn.this,TOKEN_OF_SEESION, token);

                            LogPrint("setol",token);
                        }
                        else{
                            token="";
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    SignIn.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            Toast.makeText(SignIn.this, message, Toast.LENGTH_SHORT).show();
                            if(!token.equals("")){
                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }



                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    backgroundThreadShortToast(SignIn.this,message);
                }
            }


        });
    }
}
