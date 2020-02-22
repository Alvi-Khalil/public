package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.Splash;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Tip;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.ProfileFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.DatePickerFragment;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.FCM_TOKEN_HANDLING;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.DISTRICT_LIST_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_JSON;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LOGOUT_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PASSWORD_SET_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TIP_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_AGE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_BIRTH_DATE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_IMAGE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_MAIL_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_NAME_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_PASSWORD_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_USER_LOCATION_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_DETAILS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_SPECIFIC_BIKE;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.hashedString;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.isEmailValid;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class Profile_Edit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = ProfileFragment.class.getSimpleName();

    private static final String TAG = "check";

    Activity activity;
    ImageView drawerOpener,tips,tipsBackgroung;
    ProgressBar progressBarTips;
//    String myResponse,message,myResponse2,message2;
    String myResponseBike,messageBike;
    String myResponseApiCaller,messageApiCaller;
    String myResponseUserDetails,messageUserDetails;
    String myResponseDList,messageDList;

    List<Tip> arrayListOfTip;
    int index;
    int size;
    TextView textViewName;
    ImageView editName;
    LinearLayout locationSet,linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6;

    private ConstraintLayout bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout blackLayout;

    TextView textViewTopBS;
    EditText editTextBS1,editTextBS2,editTextBS3;
    TextView showPass1,showPass2,showPass3;
    CardView cardViewBS,logOut;
    AutoCompleteTextView autoCompleteTextViewBS;
    Button leftBtn,rightBtn,camera,gallery;
    ImageView changePic;
    Button buttonDetails;

    public static final String ONE_EDIT ="one edit";
    public static final String TWO_EDIT ="two edit";
    public static final String THREE_EDIT ="three edit";
    public static final String ONE_CARD ="one card";
    public static final String TWO__PICTURE_BUTTONS ="two picture buttons";

    private static final int PICK_IMAGE_REQUEST=2;
    private static final int CAMERA_REQUEST_CODE=1;

    private Uri filePath;
    private File finalFile;
    private String currentApi="";

    private TextView txtName,txtLocation,txtAge,txtPhone,txtMail,txtPass;
    private String stringName="",stringLocation="",stringAge="",stringPhone="",stringMail="",stringPhoto="",userType="";
    private boolean pwSet=false;


    String[] names ;
    String[] banglaNames ;
    Integer[] ids;

    View v1,v2,v3;
    String districtId="",districtName="",errors;
    ImageView profileImage;
    Boolean isHiddenPass=true,isHiddenPass2=true,isHiddenPass3=true;
    FCM_TOKEN_HANDLING fcm_token_handling;

    TextView textViewBikeName;
    ImageView imageViewMyBikeImage;

    String name,image,JsonString;
    private static final String NONCLIENT="non_client";
    CardView cardViewBike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        activity=this;
        fcm_token_handling = new FCM_TOKEN_HANDLING();
        drawerOpener=findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tips =findViewById(R.id.tips);
        tipsBackgroung = findViewById(R.id.tips_back);
        progressBarTips = findViewById(R.id.progressbars_tips);
        progressBarTips.setVisibility(View.GONE);

        editName =findViewById(R.id.name_edit);

        locationSet = findViewById(R.id.location_set);
        linearLayout1 =findViewById(R.id.linear_1);
        linearLayout2 =findViewById(R.id.linear_2);
        linearLayout3 = findViewById(R.id.linear_3);
        linearLayout4 = findViewById(R.id.linear_4);
        linearLayout5 = findViewById(R.id.linear_5);
        linearLayout6 = findViewById(R.id.linear_6);


        textViewTopBS = findViewById(R.id.edit_item);
        editTextBS1 = findViewById(R.id.edit1);
        editTextBS2 = findViewById(R.id.edit2);
        editTextBS3 = findViewById(R.id.edit3);
        cardViewBS =findViewById(R.id.card_district);
        autoCompleteTextViewBS = findViewById(R.id.auto_com_text);
        leftBtn = findViewById(R.id.left_btn);
        rightBtn = findViewById(R.id.right_btn);
        changePic = findViewById(R.id.change_pic);
        camera = findViewById(R.id.camera_btn);
        gallery = findViewById(R.id.gallery_btn);
        logOut = findViewById(R.id.logoutbar);

        txtName = findViewById(R.id.name);
        txtLocation = findViewById(R.id.location);
        txtAge = findViewById(R.id.age);
        txtPhone = findViewById(R.id.phone_number);
        txtMail = findViewById(R.id.mail);
        txtPass = findViewById(R.id.pass_text);
        profileImage = findViewById(R.id.profile_image);
        buttonDetails = findViewById(R.id.details_button);

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);


        showPass1 = findViewById(R.id.show1);
        showPass2 = findViewById(R.id.show2);
        showPass3 = findViewById(R.id.show3);


        textViewBikeName = findViewById(R.id.bike_name);
        imageViewMyBikeImage = findViewById(R.id.my_bike_image);
        cardViewBike = findViewById(R.id.my_bike);




        //loadTips();

        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(activity,Mybikedetails.class);
                intent.putExtra(EXTRA_JSON,myResponseBike);
                startActivity(intent);

                //loadMyBike();
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideEverything();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentApi.equals(UPDATE_NAME_API)){
                    if(!editTextBS1.getText().toString().equals("")){
                        if(!editTextBS1.getText().toString().equals(stringName)){
                            RequestBody formBody = new FormBody.Builder()
                                    .add("name", editTextBS1.getText().toString())
                                    .build();
                            //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                            apiCaller(currentApi,formBody);
                        }
                        else{
                            Toast.makeText(activity, "No change in name!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "Name field is empty!", Toast.LENGTH_SHORT).show();
                    }


                }
                else if(currentApi.equals(UPDATE_AGE_API)){
                    if (editTextBS1.getText().toString().matches("[0-9]+") && editTextBS1.getText().toString().length() > 0){
                        if(!editTextBS1.getText().toString().equals(stringAge)){
                            RequestBody formBody = new FormBody.Builder()
                                    .add("age", editTextBS1.getText().toString())
                                    .build();
                            //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                            apiCaller(currentApi,formBody);
                        }
                        else {
                            Toast.makeText(activity, "No change in age!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "Age can't be empty or text!", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(currentApi.equals(UPDATE_MAIL_API)){
                    if(!editTextBS1.getText().toString().equals("")){
                        if(isEmailValid(editTextBS1.getText().toString())){
                            if(!editTextBS1.getText().toString().equals(stringMail)){
                                RequestBody formBody = new FormBody.Builder()
                                        .add("email", editTextBS1.getText().toString())
                                        .build();
                                //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                                apiCaller(currentApi,formBody);
                            }
                            else{
                                Toast.makeText(activity, "No change in email!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(activity, "Invalid email format!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "Email field is empty!", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(currentApi.equals(UPDATE_USER_LOCATION_API)){
                    String input = autoCompleteTextViewBS.getText().toString();
                    if(input.equals("")){
                        Toast.makeText(activity, "Empty location field!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean isPresent = false;
                        int index=0;
                        for(int i=0;i<names.length;i++){
                            if(input.equalsIgnoreCase(names[i])){
                                isPresent=true;
                                index=i;
                                break;
                            }
                        }
                        if(isPresent){
                            //Toast.makeText(activity, ""+ids[index], Toast.LENGTH_SHORT).show();
                            districtId=""+ids[index];
                            districtName=names[index];
                            RequestBody formBody = new FormBody.Builder()
                                    .add("location_id",districtId )
                                    .build();
                            //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                            apiCaller(currentApi,formBody);
                        }
                        else {
                            Toast.makeText(activity, "Invalid district name!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else if(currentApi.equals(PASSWORD_SET_API)){
                    if(!editTextBS1.getText().toString().equals("")&&!editTextBS2.getText().toString().equals("")){
                        if(editTextBS1.getText().toString().equals(editTextBS2.getText().toString())){

                            if(editTextBS1.getText().toString().length()>5){

                                String encrypted = hashedString(editTextBS1.getText().toString());
                                RequestBody formBody = new FormBody.Builder()
                                        .add("password", encrypted)
                                        .build();
                                //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                                apiCaller(currentApi,formBody);
                            }
                            else{
                                Toast.makeText(activity, "Password length must be more than 5!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(activity, "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "One of the fields is empty!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(currentApi.equals(UPDATE_PASSWORD_API)){
                    if(!editTextBS1.getText().toString().equals("")&&!editTextBS2.getText().toString().equals("")&&!editTextBS3.getText().toString().equals("")){
                        if(editTextBS2.getText().toString().equals(editTextBS3.getText().toString())){

                            if(editTextBS2.getText().toString().length()>5){

                                String encrypted = hashedString(editTextBS1.getText().toString());
                                String encrypted2 = hashedString(editTextBS2.getText().toString());
                                RequestBody formBody = new FormBody.Builder()
                                        .add("old_password", encrypted)
                                        .add("new_password", encrypted2)
                                        .build();
                                //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                                apiCaller(currentApi,formBody);
                            }
                            else{
                                Toast.makeText(activity, "New password length must be more than 5!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(activity, "New passwords doesn't match!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "One of the fields is empty!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fcm_token_handling.fcmTokenDelete(activity);
                RequestBody formBody = new FormBody.Builder()
                        .build();
                apiCaller(LOGOUT_API,formBody);
            }
        });


        arrayListOfTip = new ArrayList<>();

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDismissDlg();
            }
        });


        blackLayout =findViewById(R.id.black_layout);

        blackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEverything();


            }
        });

        bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTopBS.setText("Edit Your Name");
                editTextBS1.setHint("Your name");
                editTextBS1.setText(stringName);
                editTextBS1.setSelection(editTextBS1.getText().length());
                currentApi=UPDATE_NAME_API;

                expandBottomSheet(ONE_EDIT);


            }
        });

        locationSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTopBS.setText("Edit Your Default District");
                currentApi=UPDATE_USER_LOCATION_API;
                expandBottomSheet(ONE_CARD);


            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogFragment=new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"date picker");

               /* textViewTopBS.setText("Edit Your Age");
                editTextBS1.setHint("Your age");
                editTextBS1.setText(stringAge);
                editTextBS1.setSelection(editTextBS1.getText().length());
                currentApi=UPDATE_AGE_API;
                expandBottomSheet(ONE_EDIT);*/


            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "You can't change your phone number!", Toast.LENGTH_LONG).show();


            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTopBS.setText("Edit Your Mail");
                editTextBS1.setHint("Your mail address");
                editTextBS1.setText(stringMail);
                editTextBS1.setSelection(editTextBS1.getText().length());
                currentApi=UPDATE_MAIL_API;
                expandBottomSheet(ONE_EDIT);


            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pwSet){
                    showPass1.setVisibility(View.VISIBLE);
                    showPass2.setVisibility(View.VISIBLE);
                    showPass3.setVisibility(View.VISIBLE);
                    editTextBS1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    textViewTopBS.setText("Edit Your Password");
                    editTextBS1.setHint("Old password");
                    editTextBS2.setHint("New password");
                    editTextBS3.setHint("Confirm new password");
                    currentApi = UPDATE_PASSWORD_API;
                    expandBottomSheet(THREE_EDIT);
                }
                else{
                    showPass1.setVisibility(View.VISIBLE);
                    showPass2.setVisibility(View.VISIBLE);
                    editTextBS1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    textViewTopBS.setText("Set Your Password");
                    editTextBS1.setHint("New password");
                    editTextBS2.setHint("Confirm new password");
                    currentApi = PASSWORD_SET_API;
                    expandBottomSheet(TWO_EDIT);
                }



            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Coming soon!", Toast.LENGTH_LONG).show();


            }
        });
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Coming soon!", Toast.LENGTH_LONG).show();


            }
        });

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTopBS.setText("Upload Profile Picture");
                currentApi=UPDATE_IMAGE_API;
                expandBottomSheet(TWO__PICTURE_BUTTONS);

            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 10);
                        return;
                    } else {
                        showFileChooser();
                    }


                } else {
                    showFileChooser();
                }

            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                        }, 11);
                        return;
                    } else {
                        takePicture();
                    }


                } else {
                    takePicture();
                }

            }
        });




        showPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHiddenPass) {
                    // show password
                    editTextBS1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editTextBS1.setSelection(editTextBS1.getText().length());
                    isHiddenPass=false;
                    showPass1.setText("hide");

                } else {
                    // hide password

                    editTextBS1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS1.setSelection(editTextBS1.getText().length());
                    isHiddenPass=true;
                    showPass1.setText("show");
                }
            }
        });
        showPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHiddenPass2) {
                    // show password
                    editTextBS2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editTextBS2.setSelection(editTextBS2.getText().length());
                    isHiddenPass2=false;
                    showPass2.setText("hide");

                } else {
                    // hide password

                    editTextBS2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS2.setSelection(editTextBS2.getText().length());
                    isHiddenPass2=true;
                    showPass2.setText("show");
                }
            }
        });
        showPass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHiddenPass3) {
                    // show password
                    editTextBS3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editTextBS3.setSelection(editTextBS3.getText().length());
                    isHiddenPass3=false;
                    showPass3.setText("hide");

                } else {
                    // hide password

                    editTextBS3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextBS3.setSelection(editTextBS3.getText().length());
                    isHiddenPass3=true;
                    showPass3.setText("show");
                }
            }
        });



    }

    private void loadTips() {
//        getUrlInstance(TIP_API,new CustomCallBack(progressBarTips,activity) {
//            @Override
//            public void sendResponse(Response response) {
//                try {
//                    myResponse = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(myResponse);
//                    message = jsonObject.getString("message");
//                } catch (JSONException e) {
//                    //progressBarTips.setVisibility(View.GONE);
//                    backgroundThreadShortToast(activity, "Server Error!");
//                    e.printStackTrace();
//                }
//
//
////                    Log.d("collecd", "onResponse " + myResponse);
//                if (response.isSuccessful()) {
//
//                    JSONObject fileObject = null;
//                    JSONArray TipsArray = null;
//
//                    try {
//                        Log.d("collecd", "onResponse " + myResponse);
//                        fileObject = new JSONObject(myResponse);
//                        TipsArray = fileObject.getJSONArray("data");
//
//
//
//                        //pathN=""+pathName;
//                        for (int i = 0; i < TipsArray.length(); i++) {
//
//                            JSONObject TipArrayObject = null;
//
//                            TipArrayObject = TipsArray.getJSONObject(i);
//                            Tip singleTip = new Tip();
//                            singleTip.setHeader(TipArrayObject.getString("head"));
//                            singleTip.setBody(TipArrayObject.getString("description"));
//
//
//
//
//
//
//                            arrayListOfTip.add(singleTip);
//
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        // progressBarTips.setVisibility(View.GONE);
//                        backgroundThreadShortToast(activity, ""+e);
//                        e.printStackTrace();
//                    }
//
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            tips.setVisibility(View.VISIBLE);
//                            tipsBackgroung.setVisibility(View.VISIBLE);
//                            //progressBarTips.setVisibility(View.GONE);
//                            size = arrayListOfTip.size();
//                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
//
//
//                        }
//                    });
//                } else {
//                    //progressBarTips.setVisibility(View.GONE);
//
//                    backgroundThreadShortToast(activity, message);
//                }
//            }
//
//
//        });
    }

    private void loadInfo() {
        final ProgressDialog progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();
        progressDialogAPi.dismiss();

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(USER_DETAILS_API,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseUserDetails = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                JSONObject locationObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponseUserDetails);

                        messageUserDetails = responseJson.getString("message");
                        dataObject = responseJson.getJSONObject("data");

                        stringName = dataObject.getString("name");
                        stringAge = dataObject.getString("birth_date");
                        stringMail = dataObject.getString("email");
                        stringPhone = dataObject.getString("contact_number");
                        stringPhoto = dataObject.getString("image");

                        userType = dataObject.getString("user_type");

                        locationObject = dataObject.getJSONObject("location");
                        stringLocation = locationObject.getString("name");
                        pwSet = dataObject.getBoolean("is_password_set");







                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            if(!stringName.equals("")&&!stringName.equals("null")){
                                txtName.setText(stringName);
                            }
                            else{
                                stringName="";
                            }

                            if(!stringAge.equals("0000-00-00")&&!stringAge.equals("null")){
                                txtAge.setText(stringAge);
                            }
                            else{
                                stringAge="";
                            }

                            if(!stringMail.equals("")&&!stringMail.equals("null")){
                                txtMail.setText(stringMail);
                            }else{
                                stringMail="";
                            }

                            if(!stringPhone.equals("")&&!stringPhone.equals("null")){
                                txtPhone.setText(stringPhone);
                            }
                            else{
                                stringPhone="";
                            }

                            if(!stringLocation.equals("")&&!stringLocation.equals("null")){
                                String temp=stringLocation+", Bangladesh";
                                txtLocation.setText(temp);
                            }
                            else{
                                stringLocation="";
                            }

                            if(!stringPhoto.equals("")&&!stringPhoto.equals("null")){
                                gluideLoader(activity,stringPhoto,profileImage);
                            }

                            if(!pwSet){
                                txtPass.setText("set password");
                            }
                            else{

                                txtPass.setText("\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF");
                            }



                            if(userType.equals(NONCLIENT)){
                                cardViewBike.setVisibility(View.GONE);
                            }
                            else{
                                loadMyBike();
                            }


                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(activity,"Error!");
                }
            }


        });
    }

    private void hideEverything() {

        hideKeyboard();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        textViewTopBS.setVisibility(View.GONE);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        blackLayout.setVisibility(View.GONE);
        editTextBS1.setVisibility(View.GONE);
        editTextBS1.setText("");
        editTextBS2.setVisibility(View.GONE);
        editTextBS2.setText("");
        editTextBS3.setVisibility(View.GONE);
        editTextBS3.setText("");
        cardViewBS.setVisibility(View.GONE);
        autoCompleteTextViewBS.setText("");
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
        showPass1.setVisibility(View.GONE);
        showPass2.setVisibility(View.GONE);
        showPass3.setVisibility(View.GONE);
        editTextBS1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editTextBS2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editTextBS3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        showPass1.setText("show");
        showPass2.setText("show");
        showPass3.setText("show");
    }


    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    private void takePicture() {

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showFileChooser();
                }
                return;
            case 11:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    takePicture();
                }
                return;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);
                filePath=getImageUri(activity.getApplicationContext(),bitmap);

                finalFile = new File(getRealPathFromURI(filePath));
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", finalFile.getName(),RequestBody.create(MediaType.parse("image/jpeg"),finalFile))
                        .build();

                hideEverything();
                apiCaller(currentApi,formBody);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            filePath=getImageUri(activity.getApplicationContext(),photo);

            finalFile = new File(getRealPathFromURI(filePath));

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", finalFile.getName(),RequestBody.create(MediaType.parse("image/jpeg"),finalFile))
                    .build();

            hideEverything();
            apiCaller(currentApi,formBody);
        }

    }

    private void expandBottomSheet(String string) {
        textViewTopBS.setVisibility(View.VISIBLE);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        v3.setVisibility(View.VISIBLE);
        if (string==ONE_EDIT){
            editTextBS1.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);
        }
        if (string==TWO_EDIT){
            editTextBS1.setVisibility(View.VISIBLE);
            editTextBS2.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);
        }
        else if(string==THREE_EDIT){
            editTextBS1.setVisibility(View.VISIBLE);
            editTextBS2.setVisibility(View.VISIBLE);
            editTextBS3.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);
        }
        else if(string==ONE_CARD){
            cardViewBS.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);
        }

        else if(string==TWO__PICTURE_BUTTONS){
            camera.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);

        }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        blackLayout.setVisibility(View.VISIBLE);
    }

    private void launchDismissDlg() {

        index =0;


        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_tips);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView nextBtn =  dialog.findViewById(R.id.yes);
        final TextView dismissBtn =  dialog.findViewById(R.id.no);
        final TextView header =  dialog.findViewById(R.id.header);
        final TextView body =  dialog.findViewById(R.id.body);


        if(index<size);
        {
            Tip tip = arrayListOfTip.get(index);
            String s="Tip of the day : "+tip.getHeader();
            header.setText(s);
            body.setText(tip.getBody());

            if(index+1==size){
                nextBtn.setText("Dismiss");
                dismissBtn.setText("");
            }

        }


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index++;
                if(index<size)
                {
                    Tip tip = arrayListOfTip.get(index);
                    String s="Tip of the day : "+tip.getHeader();
                    header.setText(s);
                    body.setText(tip.getBody());
                    if(index+1==size){
                        nextBtn.setText("Dismiss");
                        dismissBtn.setText("");
                    }
                }
                else{
                    dialog.dismiss();
                }



            }
        });


        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Uri imageUri=null;
        if(path!=null){

            imageUri= Uri.parse(path);

        }
        return imageUri;
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (activity.getContentResolver() != null) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public void apiCaller(final String URL,RequestBody formBody){

        final ProgressDialog progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();
        progressDialogAPi.dismiss();



        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(URL,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseApiCaller = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponseApiCaller);

                        messageApiCaller = responseJson.getString("message");
                        if(responseJson.has("errors")){
                            errors=responseJson.getString("errors");
                            LogPrint("wwhat",errors);
                        }







                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            Toast.makeText(activity, messageApiCaller, Toast.LENGTH_SHORT).show();
                            if(URL.equals(LOGOUT_API)){

                                setMyPreference(activity,TOKEN_OF_SEESION, "");

                                Intent intent = new Intent(activity, Splash.class);
                                startActivity(intent);
                                ((MainActivity) activity).MainActivityEnd();


                            }
                            else{
                                hideEverything();
                                loadInfo();
                            }



                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(activity,"Error!");
                }
            }


        });

    }

    public void getDistrictId(){
        final ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Please wait ..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getUrlInstance(DISTRICT_LIST_API, new CustomCallBack(progressDialog,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseDList = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray districtArray = null;
                    try {
                        fileObject = new JSONObject(myResponseDList);
                        districtArray = fileObject.getJSONArray("data");
                        //pathN=""+pathName;

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        backgroundThreadShortToast(activity, "Server Error!");
                        e.printStackTrace();
                    }

                    names= new String[districtArray.length()];
                    banglaNames= new String[districtArray.length()];
                    ids= new Integer[districtArray.length()];

                    for (int i = 0; i < districtArray.length(); i++) {



                        JSONObject districtArrayObject = null;
                        try {
                            //Toast.makeText(getActivity(), pathN, Toast.LENGTH_SHORT).show();
                            districtArrayObject = districtArray.getJSONObject(i);


                            names[i]=districtArrayObject.getString("name");
                            banglaNames[i]=districtArrayObject.getString("bangla_name");
                            ids[i]=districtArrayObject.getInt("id");
                            //LogPrint(TAG",names[i]);


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            backgroundThreadShortToast(activity, "Server Error!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.suggestions_item,R.id.text_suggest,names);
                            autoCompleteTextViewBS.setAdapter(adapter);

                        }
                    });


                } else {
                    progressDialog.dismiss();
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });
    }
    private void loadMyBike() {

        final ProgressDialog pdLatestNotifications = new ProgressDialog(activity);
        pdLatestNotifications.setMessage("Loading .. ");
        pdLatestNotifications.setCancelable(false);
        pdLatestNotifications.setCanceledOnTouchOutside(false);
        pdLatestNotifications.show();
        pdLatestNotifications.dismiss();

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);


        getUrlInstance(USER_SPECIFIC_BIKE,"Authorization",token,new CustomCallBack(pdLatestNotifications,activity) {
            @Override
            public void sendResponse(Response response) {
                try {
                    myResponseBike = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }



//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONObject dataObject = null;






                    try {
                        fileObject = new JSONObject(myResponseBike);
                        messageBike = fileObject.getString("message");
                        dataObject = new JSONObject(fileObject.getString("data"));


                        name=dataObject.getString("title");
                        image = dataObject.getString("bike_image");




                    } catch (JSONException e) {
                        pdLatestNotifications.dismiss();
                        ApiHelper.backgroundThreadShortToast(activity, ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdLatestNotifications.dismiss();
                            textViewBikeName.setText(name);

                            gluideLoader(activity,image,imageViewMyBikeImage);



                        }
                    });
                } else {
                    pdLatestNotifications.dismiss();

                    backgroundThreadShortToast(activity, messageBike);
                }
            }


        });
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {



        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Calendar cal = Calendar.getInstance();

        if(before(calendar,cal)){
            String s1 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            RequestBody formBody = new FormBody.Builder()
                    .add("birth_date", s1)
                    .build();
            //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
            apiCaller(UPDATE_BIRTH_DATE_API,formBody);
            //String s2=""+calendar.getTime();
            //String s=DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

            //Toast.makeText(activity, s1, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(activity, "You can't pick a date before than today!", Toast.LENGTH_SHORT).show();
        }


    }


    public static boolean before(Calendar c1, Calendar c2){
        int c1Year = c1.get(Calendar.YEAR);
        int c1Month = c1.get(Calendar.MONTH);
        int c1Day = c1.get(Calendar.DAY_OF_MONTH);

        int c2Year = c2.get(Calendar.YEAR);
        int c2Month = c2.get(Calendar.MONTH);
        int c2Day = c2.get(Calendar.DAY_OF_MONTH);

        if(c1Year<c2Year){
            return true;
        }else if (c1Year>c2Year){
            return false;
        }else{
            if(c1Month>c2Month){
                return false;
            }else if(c1Month<c2Month){
                return true;
            }else{
                return c1Day<c2Day;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
        getDistrictId();
    }
}
