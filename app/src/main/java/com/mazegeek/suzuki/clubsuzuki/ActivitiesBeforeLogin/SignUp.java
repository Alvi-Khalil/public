/*
 * Created by Alvi Khalil on 10/22/18 1:46 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/22/18 1:46 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PASSWORD_SET_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.hashedString;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;



public class SignUp extends AppCompatActivity {

    CardView signUp;
    TextView skip;
    ImageView passEye,passConEye;
    EditText editPass,editPassCon,editName;
    Boolean isHiddenPass=true,isHiddenPassCon=true;
    String myResponse;
    String message;
    boolean alreadyApplied;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new);

        signUp = findViewById(R.id.card);
        skip = findViewById(R.id.skip);
        passEye = findViewById(R.id.eye);
        passConEye = findViewById(R.id.con_eye);
        editPass = findViewById(R.id.edit_pass);
        editPassCon = findViewById(R.id.edit_pass2);
        editName = findViewById(R.id.edit_name);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString().equals("")||editPass.getText().toString().equals("")||editPassCon.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!editPass.getText().toString().equals(editPassCon.getText().toString())){
                        Toast.makeText(SignUp.this, "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(editPass.getText().toString().length()<6){
                            Toast.makeText(SignUp.this, "Passwords length must be 6 or more!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String encryptedString = hashedString(editPass.getText().toString());
                            setNameAndPass(editName.getText().toString(),encryptedString);

                        }

                    }
                }

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchDismissDlg();
            }
        });

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

        passConEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHiddenPassCon) {
                    // show password
                    editPassCon.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editPassCon.setSelection(editPassCon.getText().length());
                    isHiddenPassCon=false;
                    passConEye.setImageResource(R.drawable.eyex);

                } else {
                    // hide password

                    editPassCon.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editPassCon.setSelection(editPassCon.getText().length());
                    passConEye.setImageResource(R.drawable.eyex2);
                    isHiddenPassCon=true;
                }

            }
        });
    }

    private void setNameAndPass(String name, String pass) {
        final ProgressDialog progressDialogAPi = new ProgressDialog(this);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setMessage("Getting ready ..");
        progressDialogAPi.show();

        String token = getMyPreference(SignUp.this,TOKEN_OF_SEESION);
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("password", pass)
                .build();

        getUrlInstance(PASSWORD_SET_API,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,this) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponse);

                        message = responseJson.getString("message");
                        dataObject = responseJson.getJSONObject("data");

                        alreadyApplied = dataObject.getBoolean("already_applied");




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    SignUp.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();

                            if(alreadyApplied){
                                Intent intent = new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else{
//                                Intent intent = new Intent(SignUp.this,AddBike.class);
//                                startActivity(intent);
                                Intent intent = new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);
                            }



                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    backgroundThreadShortToast(SignUp.this,"Error!");
                }
            }


        });
    }


    private void launchDismissDlg() {

        final Dialog dialog = new Dialog(SignUp.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView okBtn =  dialog.findViewById(R.id.yes);
        TextView cancelBtn =  dialog.findViewById(R.id.no);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);


            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });
        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
}
