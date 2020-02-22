/*
 * Created by Alvi Khalil on 10/23/18 3:06 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/22/18 8:56 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.AboutFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments.CatalogsFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.ClubFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.EcommerceFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.HomeFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.ProfileFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.OffersFragment;
import com.mazegeek.suzuki.clubsuzuki.BuildConfig;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.FCM_TOKEN_HANDLING;
import com.mazegeek.suzuki.clubsuzuki.Interfaces.FragmentChangeListener;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEW_FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_DETAILS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;



public class MainActivity extends AppCompatActivity implements FragmentChangeListener {


    private static final String COMMON_TAG = "CombinedLifeCycle";

    private static final String TAG = "MainActivity";

    RelativeLayout home,catalog,location,ecommerce,offers,sos,help,club,profileTop,about,tentheHiddenLayout;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    ConstraintLayout mainView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView profileImageDrawer;
    String myResponse,message,stringName="",stringPhoto="",stringLocation="";
    TextView nameTxt,locationTxt;
    FCM_TOKEN_HANDLING fcm_token_handling;
    ProgressDialog pdSos;
    Activity activity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textViewVersion=findViewById(R.id.version);
        textViewVersion.setText("v"+BuildConfig.VERSION_NAME);
        activity=this;
        fcm_token_handling = new FCM_TOKEN_HANDLING();
        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                fcmToken= instanceIdResult.getToken();
                LogPrint(TAG, "FCM2: " + fcmToken);


            }
        });*/

        String fcmExists=getMyPreference(this,FCM_TOKEN);
        String newFcmExists=getMyPreference(this,NEW_FCM_TOKEN);

        if(fcmExists.equals("")){
            fcm_token_handling.fcmTokenAdd(this);
        }

        if(!newFcmExists.equals("")){
            fcm_token_handling.fcmTokenReplace(this,newFcmExists);
        }



        home=findViewById(R.id.one);
        catalog=findViewById(R.id.two);
        location=findViewById(R.id.three);
        ecommerce=findViewById(R.id.four);
        offers=findViewById(R.id.five);
        sos=findViewById(R.id.eight);
        help=findViewById(R.id.seven);
        club=findViewById(R.id.six);
        about=findViewById(R.id.nine);
        tentheHiddenLayout=findViewById(R.id.ten);
        profileImageDrawer=findViewById(R.id.profile_image_drawer);
        nameTxt=findViewById(R.id.name_text);
        locationTxt=findViewById(R.id.location_text);
        profileTop=findViewById(R.id.profile_top);




        mDrawerLayout = findViewById(R.id.drawer_layout);
        mainView = findViewById(R.id.maincons);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open,R.string.close) {
            public void onDrawerClosed(View view) {
                MainActivity.this.supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                MainActivity.this.supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(-(slideOffset * drawerView.getWidth()));
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);




        fragmentManager=getSupportFragmentManager();


        HomeFragment homeFragment=new HomeFragment();
        addFragment(homeFragment);
        selected(home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment=new HomeFragment();
                replaceFragment(homeFragment);
                selected(home);
            }
        });

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogsFragment catalogsFragment = new CatalogsFragment();
                replaceFragment(catalogsFragment);
                selected(catalog);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationFragment locationFragment=new LocationFragment();
                replaceFragment(locationFragment);
                selected(location);
            }
        });
        ecommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EcommerceFragment ecommerceFragment = new EcommerceFragment();
                replaceFragment(ecommerceFragment);
                selected(ecommerce);
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OffersFragment offersFragment=new OffersFragment();
                replaceFragment(offersFragment);
                selected(offers);
            }
        });

        profileTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment=new ProfileFragment();
                replaceFragment(profileFragment);
                selected(tentheHiddenLayout);
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                loadSoS();
                /*OffersFragment offersFragment = new OffersFragment();
                replaceFragment(offersFragment);
                selected(sos);*/
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                launchHelp();
                /**/
            }
        });
        club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClubFragment clubFragment=new ClubFragment();
                replaceFragment(clubFragment);
                selected(club);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutFragment surveyFragment=new AboutFragment();
                replaceFragment(surveyFragment);
                selected(about);
            }
        });

        //loadInfo();
    }


    public void drawerOpen(){

        hideKeyboard();
        loadInfo();
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    private void addFragment(Fragment fragment) {

        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        LogPrint(TAG,"add 1st: "+fragmentManager.getBackStackEntryCount());
    }

    @Override
    public void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d(TAG,"replace: "+fragmentManager.getBackStackEntryCount());
    }

    private void selected(RelativeLayout relativeLayout){

        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        home.setSelected(false);
        catalog.setSelected(false);
        location.setSelected(false);
        ecommerce.setSelected(false);
        offers.setSelected(false);
        sos.setSelected(false);
        about.setSelected(false);
        club.setSelected(false);

        relativeLayout.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
        if (f instanceof HomeFragment)
            // do something with f
            ((HomeFragment) f).stopAppfromFragment_self_implemented();


        else {

            HomeFragment homeFragment=new HomeFragment();
            replaceFragment(homeFragment);
            selected(home);


        }
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

    private void loadInfo() {
        final ProgressDialog progressDialogAPi = new ProgressDialog(this);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
//        progressDialogAPi.show();
  //      progressDialogAPi.dismiss();


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

                        stringPhoto = dataObject.getString("image");


                        locationObject = dataObject.getJSONObject("location");
                        stringLocation = locationObject.getString("name");










                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();
                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            if(!stringName.equals("")&&!stringName.equals("null")){
                                nameTxt.setText(stringName);
                            }
                            else{
                                stringName="";
                            }



                            if(!stringLocation.equals("")&&!stringLocation.equals("null")){
                                String temp=stringLocation+", Bangladesh";
                                locationTxt.setText(temp);
                            }
                            else{
                                stringLocation="";
                            }


                            if(!stringPhoto.equals("")&&!stringPhoto.equals("null")){
                                gluideLoader(MainActivity.this,stringPhoto,profileImageDrawer);
                            }








                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(MainActivity.this,"Error!");
                }
            }


        });
    }
    public void MainActivityEnd(){
        finish();
    }



    private void loadSoS(){
        pdSos = new ProgressDialog(activity);
        pdSos.setMessage("Loading .. ");
        pdSos.setCancelable(false);
        pdSos.setCanceledOnTouchOutside(false);
        pdSos.show();
        getUrlInstance(SOS_API,new CustomCallBack(pdSos,activity) {
            @Override
            public void sendResponse(Response response) {
                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject jsonObject = new JSONObject(myResponse);
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    pdSos.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray SoSArray = null;
                    final List<SoSItems> arrayListOfSoS = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        SoSArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < SoSArray.length(); i++) {

                            JSONObject SOSArrayObject = null;

                            SOSArrayObject = SoSArray.getJSONObject(i);
                            SoSItems soSItems= new SoSItems();
                            soSItems.setPhoneNumber(SOSArrayObject.getString("sos_number"));
                            soSItems.setImage(SOSArrayObject.getString("image"));
                            soSItems.setHeader(SOSArrayObject.getString("name"));






                            arrayListOfSoS.add(soSItems);


                        }


                    } catch (JSONException e) {
                        pdSos.dismiss();
                        backgroundThreadShortToast(activity, ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdSos.dismiss();
                            SoSListRecyclerAdapter customAdapter = new SoSListRecyclerAdapter(activity, arrayListOfSoS);
                            launchSoS(customAdapter);

                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdSos.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });

    }

    private void launchSoS(SoSListRecyclerAdapter customAdapter) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_sos);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView recyclerView= dialog.findViewById(R.id.recycler_dialog_sos);

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManagerHorizontal);
        recyclerView.setAdapter(customAdapter);

        ImageView imageViewCross=dialog.findViewById(R.id.cross);

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
/*
        TextView okBtn =  dialog.findViewById(R.id.yes);
        TextView cancelBtn =  dialog.findViewById(R.id.no);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RequestBody formBody = new FormBody.Builder()
                        .add("id",id )
                        .build();
                func2(formBody);
                //Toast.makeText(RideHistoryActivity.this, id, Toast.LENGTH_SHORT).show();
                // call delection api
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });*/
        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }


    private void launchHelp() {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_help_line);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        CardView cardViewPhone = dialog.findViewById(R.id.phone);

        CardView cardViewEmail = dialog.findViewById(R.id.email);



        cardViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"suzuki@rangs.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Seeking Help from Club Suzuki App");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                //intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                //dialog.dismiss();
            }
        });

        cardViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"+8801755662288"));
                startActivity(intent);
                //dialog.dismiss();
            }
        });



        ImageView imageViewCross=dialog.findViewById(R.id.cross);

        imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
/*
        TextView okBtn =  dialog.findViewById(R.id.yes);
        TextView cancelBtn =  dialog.findViewById(R.id.no);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RequestBody formBody = new FormBody.Builder()
                        .add("id",id )
                        .build();
                func2(formBody);
                //Toast.makeText(RideHistoryActivity.this, id, Toast.LENGTH_SHORT).show();
                // call delection api
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });*/
        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    protected void onResume() {
        loadInfo();
        super.onResume();
    }
}
