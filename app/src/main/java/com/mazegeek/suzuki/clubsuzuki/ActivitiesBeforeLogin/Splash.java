/*
 * Created by Alvi Khalil on 10/22/18 1:44 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/22/18 1:40 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.BikeDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.PartsClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.AUTHENTICATION_CODE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PART_CATALOG_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PASSWORD_SET;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;


public class Splash extends AppCompatActivity {


    private final static String TAG = "Splash";

    ImageView bikeBackground;
    CardView getStarted;
    TextView alreadyUser,alreadyUser2;
    private AccessToken accessToken;
    public static int APP_REQUEST_CODE = 99;
    private String accountKitId, accountKitphoneNumberString, accountKitemail, accountKitToken;
    ProgressDialog progressDialog;
    String myResponse,message;
    String token="";
    Boolean pwSet=false;
    String login="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bikeBackground = findViewById(R.id.bike_background);
        getStarted = findViewById(R.id.card);
        alreadyUser = findViewById(R.id.already);
        alreadyUser2 = findViewById(R.id.already2);

        login=getMyPreference(Splash.this,TOKEN_OF_SEESION);
        LogPrint("ziahn",login);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

            if(login.equals("")||login==null){
                appear();
            }
            else {

                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }


            }

            }, 1500);


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMyDialog("Please wait ..");

                /*Intent intent = new Intent(Splash.this, SignUp.class);
                dismissMyDialog();
                startActivity(intent);*/

                AccountKit.logOut();
                startLoginPage(LoginType.PHONE);

            }
        });

        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Splash.this, SignIn.class);
                startActivity(intent);
            }
        });


    }

    private void showMyDialog(String s) {
        progressDialog = new ProgressDialog(Splash.this);
        progressDialog.setMessage(s);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    private void dismissMyDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

    }


    private void appear() {
        Animation fadeIn = AnimationUtils.loadAnimation(Splash.this,
                R.anim.fade_in);
        bikeBackground.setAnimation(fadeIn);
        getStarted.setAnimation(fadeIn);
        alreadyUser.setAnimation(fadeIn);
        alreadyUser2.setAnimation(fadeIn);

        bikeBackground.setVisibility(View.VISIBLE);
        getStarted.setVisibility(View.VISIBLE);
        alreadyUser.setVisibility(View.VISIBLE);
        alreadyUser2.setVisibility(View.VISIBLE);
    }

    private void startLoginPage(LoginType loginType) {
        if (loginType == LoginType.EMAIL) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.EMAIL,
                            AccountKitActivity.ResponseType.CODE); // Use Token when 'Enable client Access Token Flow' is YES


            //customizing the kit layout


            UIManager uiManager = new SkinManager(
                    SkinManager.Skin.CONTEMPORARY,
                    ContextCompat.getColor(this, R.color.colorAccentFacebookKit),
                    R.drawable.splash_bike,
                    SkinManager.Tint.BLACK,
                    0.55);
            configurationBuilder.setUIManager(uiManager);


            // ending customizing

            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());


            startActivityForResult(intent, APP_REQUEST_CODE);
        } else if (loginType == LoginType.PHONE) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.PHONE,
                            AccountKitActivity.ResponseType.CODE);// Use Token when 'Enable client Access Token Flow' is YES

            //customizing the kit layout


            UIManager uiManager = new SkinManager(
                    SkinManager.Skin.CONTEMPORARY,
                    ContextCompat.getColor(this, R.color.colorAccentFacebookKit),
                    R.drawable.splash_bike,
                    SkinManager.Tint.BLACK,
                    0.55);
            configurationBuilder.setUIManager(uiManager);


            // ending customizing


            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());


            startActivityForResult(intent, APP_REQUEST_CODE);
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request

            dismissMyDialog();

            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {


                toastMessage = loginResult.getError().getErrorType().getMessage();
                LogPrint(TAG, "1" + toastMessage);
                return;

                //showErrorActivity(loginResult.getError());

            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
                LogPrint(TAG, "2" + toastMessage);
                return;
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    LogPrint(TAG, "3" + toastMessage);
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                    LogPrint(TAG, "4" + toastMessage);
                }

                LogPrint("codenau",""+loginResult.getAuthorizationCode());
                String temp=""+loginResult.getAuthorizationCode();
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...

                /*Intent intent = new Intent(Splash.this, SignUp.class);
                startActivity(intent);*/

                AccountKit.logOut();
                registrationOrLoginForNoPasswordSet(temp);


                //goToMyLoggedInActivity();
            }

            // Surface the result to your user in an appropriate way.
            //Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }

    }

    private void registrationOrLoginForNoPasswordSet(String s) {
        final ProgressDialog progressDialogAPi = new ProgressDialog(this);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setMessage("Getting ready ..");
        progressDialogAPi.show();

        RequestBody formBody = new FormBody.Builder()
                .add("authorization_code", s)
                .build();

        getUrlInstance(AUTHENTICATION_CODE_API,formBody, new CustomCallBack(progressDialogAPi,this) {
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
                        LogPrint("lpihib",myResponse);
                        message = responseJson.getString("message");
                        dataObject = responseJson.getJSONObject("data");

                        token = dataObject.getString("token");
                        setMyPreference(Splash.this,TOKEN_OF_SEESION, token);
                        pwSet = dataObject.getBoolean("is_password_set");




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    Splash.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            Toast.makeText(Splash.this, message, Toast.LENGTH_SHORT).show();
                            if(pwSet){

                                Intent intent = new Intent(Splash.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{

                                Intent intent = new Intent(Splash.this, SignUp.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    backgroundThreadShortToast(Splash.this,"Error!");
                }
            }


        });
    }

    private void getUserInfo() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                accessToken = AccountKit.getCurrentAccessToken();
                accountKitToken = accessToken.getToken();
                accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String s = "";
                if (phoneNumber != null) {
                    accountKitphoneNumberString = phoneNumber.toString();
                    s = phoneNumber.getCountryCode();
                } else {
                    accountKitphoneNumberString = null;
                }

                // Get email
                //accountKitemail = account.getEmail();

                LogPrint(TAG, accountKitToken + " " + accountKitId + " " + accountKitphoneNumberString );


                /* Intent intent = new Intent(Splash.this, Main2Activity.class);
                intent.putExtra(USER_KIT_ID, accountKitId);
                intent.putExtra(USER_KIT_PHONE, accountKitphoneNumberString);
                intent.putExtra(USER_KIT_EMAIL, accountKitemail);
                intent.putExtra(USER_KIT_TOKEN, accountKitToken);
                startActivity(intent);*/
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
