package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.Splash;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LOGOUT_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.REDEEM_TYPE;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_DETAILS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_OFFER_AVAILABILITY;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_OFFER_CONFIRMATION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class ScanQR extends AppCompatActivity {

    Button buttonScan;
    //Button buttonScanAgain,buttonCheck;
    ImageView backBtn;
    String myResponse,message;
    String myResponseAvailable,messageAvailable,errorAvailable;
    String myResponseConfirm,messageConfirm;
    String redeemType="",stringName="",stringPhone="";
    TextView textViewHeader,textViewName,textViewphone,textViewRedeemType,textViewShopName,textViewShopPhone,textViewOffercode,textViewAvailability;
    boolean available=false;
    ProgressDialog progressDialogAPi;
    Activity activity;
    String offerToken,mobileOfShop,offerCode;
    TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        activity=this;



        backBtn = findViewById(R.id.back);
        //buttonScanAgain = findViewById(R.id.button_scan_again);
        buttonScan = findViewById(R.id.button_scan);
        //buttonCheck = findViewById(R.id.done);
        textViewName = findViewById(R.id.customer_name);
        textViewphone = findViewById(R.id.customer_phone);
        textViewRedeemType = findViewById(R.id.redeem_type);
        textViewHeader = findViewById(R.id.header_text);
        textViewShopName = findViewById(R.id.shop_name);
        textViewShopPhone = findViewById(R.id.shop_phone);
        textViewOffercode = findViewById(R.id.offer_code);
        textViewAvailability = findViewById(R.id.availability);
        textViewError = findViewById(R.id.error_text);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(available){
                    ConfirmAvailOffer();
                }
                else{
                    scanStart();
                }


            }
        });
/*

        buttonScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(available){
                    finish();
                }
                else{
                    scanStart();
                }

            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(available){
                    Toast.makeText(ScanQR.this, "Calling SMS Api", Toast.LENGTH_LONG).show(); //call SMS api
                }
                else{
                    String stringFromApi="Yes"; //call zihan api to check availability

                    setQRtext(textViewAvailability,stringFromApi);
                    if(stringFromApi.equals("Yes")){

                        buttonScanAgain.setText("Cancel");
                        buttonCheck.setText("Avail Offer");
                        available=true;
                    }
                    else if(stringFromApi.equals("No")){

                        buttonScanAgain.setVisibility(View.GONE);
                        buttonCheck.setVisibility(View.GONE);
                        Toast.makeText(ScanQR.this, "You have already availed this offer!", Toast.LENGTH_SHORT).show();
                        available=false;
                    }
                }

            }
        });
*/

        Intent intent=getIntent();
        redeemType=intent.getStringExtra(REDEEM_TYPE);
        textViewHeader.setText(redeemType);
        textViewRedeemType.setText(redeemType);



    }

    private void ConfirmAvailOffer() {
        final ProgressDialog progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();
        progressDialogAPi.dismiss();

        RequestBody formBody = new FormBody.Builder()
                .add("offer_code", offerCode)
                .add("mobile_number", mobileOfShop)
                .add("token", offerToken)
                .build();

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(USER_OFFER_CONFIRMATION,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseConfirm = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponseConfirm);

                        LogPrint("checkingthisapi"," final :"+myResponseConfirm);
                        messageConfirm = responseJson.getString("message");




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();

                            Toast.makeText(activity, messageConfirm, Toast.LENGTH_SHORT).show();
                            finish();



                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(activity,"Error!");
                }
            }


        });
    }

    private void scanStart() {
        IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result!=null){
            if(result.getContents()!=null){
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                JSONObject QRJson=null;
                String jsonString=result.getContents();
                /*buttonScan.setVisibility(View.GONE);
                buttonScanAgain.setVisibility(View.VISIBLE);
                buttonCheck.setVisibility(View.VISIBLE);*/
                try {
                    QRJson=new JSONObject(jsonString);

                    String shopName = QRJson.getString("shop_name");
                    setQRtext(textViewShopName,shopName);
                    mobileOfShop = QRJson.getString("shop_phone");
                    setQRtext(textViewShopPhone,mobileOfShop);
                    offerCode = QRJson.getString("offer_code");
                    setQRtext(textViewOffercode,offerCode);
                    CallApiForAvailability(offerCode);
                    

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Not a Suzuki QR code!", Toast.LENGTH_LONG).show();
                }


            }
            else{
                Toast.makeText(this, "Scanning cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }



    }

    private void CallApiForAvailability(String offerCode) {
        final ProgressDialog progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();
        progressDialogAPi.dismiss();

        RequestBody formBody = new FormBody.Builder()
                .add("offer_code", offerCode)
                .build();

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(USER_OFFER_AVAILABILITY,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseAvailable = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponseAvailable);

                        LogPrint("checkingthisapi",myResponseAvailable);
                        messageAvailable = responseJson.getString("message");

                        if(messageAvailable.equals("Yes")){
                            dataObject =responseJson.getJSONObject("data");
                            offerToken =dataObject.getString("token");
                        }
                        else{
                            errorAvailable = responseJson.getString("errors");
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            if(messageAvailable.equals("Yes")){
                                buttonScan.setText("Avail Offer");
                                available=true;
                                setQRtext(textViewAvailability,messageAvailable);

                            }
                            else{
                                buttonScan.setVisibility(View.GONE);
                                textViewError.setVisibility(View.VISIBLE);
                                textViewError.setText(errorAvailable);
                                setQRtext(textViewAvailability,messageAvailable);
                            }
                            //Toast.makeText(activity, messageAvailable, Toast.LENGTH_SHORT).show();




                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(activity,"Error!");
                }
            }


        });
    }


    public void setQRtext(TextView textView,String string){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/avenir_medium.otf");
        textView.setTypeface(font, Typeface.NORMAL);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        textView.setText(string);
    }
    private void loadInfo() {

        if(progressDialogAPi==null){
            progressDialogAPi = new ProgressDialog(this);
            progressDialogAPi.setMessage("Please wait .. ");
            progressDialogAPi.setCanceledOnTouchOutside(false);
            progressDialogAPi.setCancelable(false);
            progressDialogAPi.show();
        }

        String token = getMyPreference(this,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(USER_DETAILS_API,"Authorization",token, new CustomCallBack(progressDialogAPi,this) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                JSONObject locationObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponse);

                        message = responseJson.getString("message");
                        dataObject = responseJson.getJSONObject("data");

                        stringName = dataObject.getString("name");

                        stringPhone = dataObject.getString("contact_number");










                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    ScanQR.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            if(!stringName.equals("")&&!stringName.equals("null")){
                                textViewName.setText(stringName);
                            }
                            else{
                                stringName="";
                            }



                            if(!stringPhone.equals("")&&!stringPhone.equals("null")){

                                textViewphone.setText(stringPhone);
                            }
                            else{
                                stringPhone="";
                            }










                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(ScanQR.this,"Error!");
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }
}
