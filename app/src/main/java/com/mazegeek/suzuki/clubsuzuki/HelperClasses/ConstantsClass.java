/*
 * Created by Alvi Khalil on 10/22/18 1:08 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/5/18 4:57 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.Survey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class ConstantsClass {
    public static final String BASE_URL="https://suzuki.com.bd/api/";
    
    
    public static final String USER_KIT_ID="user_kit_id";
    public static final String USER_KIT_PHONE="user_kit_phone";
    public static final String USER_KIT_EMAIL="user_kit_email";
    public static final String USER_KIT_TOKEN="user_kit_token";
    public static final String LOGIN_LAYOUT="login";
    public static final String SIGNIN_LAYOUT="signin";
    public static final String LAYOUT_TYPE="layout_type";
    public static final String TOKEN_OF_SEESION="token of session";
    public static final String FCM_TOKEN="fcm token";
    public static final String NEW_FCM_TOKEN="new fcm token";
    public static final String NOTHING="nothing";
    public static final String REDEEM_TYPE="redeem type";
    public static final String PARCEL="parcel";
    public static final String LATEST_NOTIFICATION="lateste notification";
    public static final String ALL_NOTIFICATION ="image notification";
    public static final String EXTRA_JSON ="extra json";
    public static final String EXTRA_SURVICE_HEADER ="extra service header";
    public static final String ALL_SURVICES ="Scheduled Services";
    public static final String FREE_SURVICES ="Scheduled Free Services";
    public static final String PAID_SURVICES ="Scheduled Paid Services";
    public static final String EXTRA_PREVIOUS ="extra previous";
    public static final String BANNER ="banners";
    public static final String OFFERS ="eoffrs";
    public static final String BIKE ="BIKE";
    public static final String COLOR ="COLOR";
    public static final String NO_INTERNET_CONNECTION ="No Internet Connection!";


    public static final String PASSWORD_SET="pw set";
    public static final String DISTRICT_LOCATIONS_API = BASE_URL +"points-by-district";
    public static final String NEAREST_LOCATIONS_API = BASE_URL +"nearest-points";
    public static final String DISTRICT_LIST_API = BASE_URL +"districts";
    public static final String HOME_BANNAR_API = BASE_URL +"slider-images";
    public static final String BIKE_CATALOG_WITH_TYPE_API = BASE_URL +"bike-catalog";
    public static final String BIKE_DETAILS_WITH_ID = BASE_URL +"bike-details-by-model";
    public static final String OIL_CATALOG_API = BASE_URL +"oil-catalog";
    public static final String OIL_CATALOG_BIKE_ID_API = BASE_URL +"oil-catalog-by-model";
    public static final String PART_CATALOG_API = BASE_URL +"parts-catalog";
    public static final String PART_CATALOG_BIKE_ID_API = BASE_URL +"parts-catalog-by-model";
    public static final String ECOMMERCE_API = BASE_URL +"ecommerce-information";
    public static final String TIP_API = BASE_URL +"tip-of-the-day";
    public static final String SOS_API = BASE_URL +"sos-information";
    public static final String CLUB_API = BASE_URL +"club-information";
    public static final String AUTHENTICATION_CODE_API = BASE_URL +"user/register";
    public static final String PASSWORD_SET_API  = BASE_URL +"user/set-credential";
    public static final String LOGIN_API = BASE_URL +"user/login";
    public static final String LOGOUT_API = BASE_URL +"user/logout";
    public static final String UPDATE_NAME_API = BASE_URL +"user/update-name";
    public static final String UPDATE_AGE_API = BASE_URL +"user/update-age";
    public static final String UPDATE_BIRTH_DATE_API = BASE_URL +"user/update-birthday";
    public static final String UPDATE_MAIL_API = BASE_URL +"user/update-mail";
    public static final String UPDATE_USER_LOCATION_API = BASE_URL +"user/update-location";
    public static final String UPDATE_IMAGE_API = BASE_URL +"user/update-image";
    public static final String UPDATE_PASSWORD_API = BASE_URL +"user/update-password";
    public static final String USER_DETAILS_API = BASE_URL +"user";
    public static final String NOTIFICAION_TOKEN_UPDATE_API = BASE_URL +"user/notification-token";
    public static final String NEW_RIDE_ENTRY = BASE_URL +"user/ride-history/upload";
    public static final String USER_RIDE_HISTORY = BASE_URL +"user/ride-history";
    public static final String SURVEY_QUESTIONS = BASE_URL +"user/survey";
    public static final String SURVEY_ANSWER_POSTING = BASE_URL +"user/survey/answer/post";
    public static final String LATEST_NOTIFICATIONS_API = BASE_URL +"user/notification-latest";
    public static final String ALL_NOTIFICATIONS_API = BASE_URL +"user/notification-all";
    public static final String ADD_CHASSIS = BASE_URL +"user/chassis-number/create";
    public static final String USER_SPECIFIC_BIKE = BASE_URL +"bike-details-by-chassis";
    public static final String SERVICE_LIST_ALL = BASE_URL +"user/service-list";
    public static final String SERVICE_LIST_SPECIFIC = BASE_URL +"user/specific-service-list";
    public static final String RIDE_HISTORY_DELETE = BASE_URL +"user/ride-history/delete";
    public static final String ALL_OFFERS = BASE_URL +"user/offer-list";
    public static final String HOME_PAGE_USER_INFO = BASE_URL +"user/special-information";
    public static final String USER_OFFER_AVAILABILITY = BASE_URL +"user/get-offer";
    public static final String USER_OFFER_CONFIRMATION = BASE_URL +"user/get-offer-generate";
    public static final String USER_LOYALTY_HISTORY = BASE_URL +"user/get-loyalty-history";

    public static final String BIKE_COLOR_LIST = BASE_URL +"bike-color-list";
    public static final String NEW_REQUEST_API = BASE_URL +"user/new-chassis-number/create";


    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String hashedString(String s){
        MessageDigest messageDigest = null;
        String hashOutput="";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes());
            String encryptedString = new String(messageDigest.digest());
            hashOutput=encryptedString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashOutput;
    }

    public static void AddingChassis(final Activity activity,String chassisNumber,String modelId,String colorId, String purchaseDate) {


        final ProgressDialog progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setMessage("Requesting approval ..");
        progressDialogAPi.show();


        RequestBody formBody = new FormBody.Builder()
                .add("chassis_number", chassisNumber)
                .add("model_id", modelId)
                .add("color_id", colorId)
                .add("date", purchaseDate)
                .build();

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);


        getUrlInstance(NEW_REQUEST_API,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {



                JSONObject responseJson=null;
                JSONObject dataObject=null;
                if (response.isSuccessful()) {
                    try {

                        String temp="";
                        try {
                            final String myResponse = response.body().string();
                            temp=myResponse;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        responseJson = new JSONObject(temp);

                        temp = responseJson.getString("message");
                        Boolean success=responseJson.getBoolean("success");






                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();

                            Toast.makeText(activity, "Sent to admin for approval", Toast.LENGTH_LONG).show();



                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    backgroundThreadShortToast(activity,"Error!");
                }
            }


        });
    }


}
