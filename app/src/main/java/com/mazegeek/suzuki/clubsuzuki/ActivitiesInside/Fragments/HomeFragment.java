/*
 * Created by Alvi Khalil on 10/24/18 11:31 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/11/18 12:36 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.SignUp;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.Splash;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.AllNotifications;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.RedeemList;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.RideCreation;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.RideHistoryActivity;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.ServiceSchedule;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.BikeAndColorRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.HomeBannarAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MyPagerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.NotificationRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeAndColor;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.BikeAndColorClicked;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConnectionDetector;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.DatePickerFragment;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.AddingChassis;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE_COLOR_LIST;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.COLOR;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_SURVICE_HEADER;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.FREE_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.HOME_BANNAR_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.HOME_PAGE_USER_INFO;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LATEST_NOTIFICATION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LATEST_NOTIFICATIONS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NO_INTERNET_CONNECTION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PAID_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.UPDATE_BIRTH_DATE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_DETAILS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;


public class HomeFragment extends Fragment implements BikeAndColorClicked {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = HomeFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    private HomeBannarAdapter homeBannarAdapter;
    //MainActivity activity;
    Activity activity;



    public final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 10000;
    public final static int FIRST_PAGE =  LOOPS / 2;

    public MyPagerAdapter adapter;
    public ViewPager pager;
    ProgressBar progressBar;
    String myResponseBikeColor="";

    String myResponse="",myResponse2="",message;
    Button scheduleButton;
    Button redeemBtn,allNoti;
    ConstraintLayout newRide,rideHistory;
    ProgressDialog pdLatestNotifications;
    RecyclerView recyclerView;

    ImageView quesRedeem,quesLevel;
    CardView cardViewFree,cardViewPaid;


    private static final int REDEEM_QUES=1;
    private static final int LEVEL_QUES=2;
    private static final int FREE_QUES=3;
    private static final int PAID_QUES=4;
    private static final int SCHEDULE=5;
    private static final String CLIENT="client";
    private static final String NONCLIENT="non_client";

    String userType="",unseen;
    boolean alreadyApplied;
    TextView textViewServiceDate,textViewServiveName,textViewCom1,textViewCom2,textViewFreePercent,textViewPaidPercent,textViewFreeRemain,textViewPaidRemain;
    ProgressBar progressBarFree,progressBarPaid;

    TextView textViewUnseen,textViewNotiSentence,textViewupcoming_service;
    ImageView imageViewRed;
    Integer integerFreePassed,integerPaidPassed;
    String stringServiceName,stringStartDate,stringEndDate;
    String type11;
    SwipeRefreshLayout pullToRefresh;

    HorizontalInfiniteCycleViewPager infiniteCycleViewPager;

    ProgressDialog progressDialogAPi;

    public ConnectionDetector connectionDetector;
    boolean loadAgain=true;

    boolean modelPicked=false,colorPicked=false,purchaseDatePicked=false;

    List<BikeAndColor> arrayListOfBikes=new ArrayList<>();

    JSONObject colorObject;

//    BikeAndColorRecyclerAdapter bikeAndColorRecyclerAdapter;
    RecyclerView recyclerViewBike;
    RecyclerView recyclerViewColor;


    ConstraintLayout Drop1,Drop2;
    TextView textViewModel,textViewColor,textViewPurchase;

    String bikeId="",colorId="",datePicked="";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //activity = (MainActivity) context;
        activity = (Activity) context;
        Log.i(TAG, FRAGMENT_NAME +" onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, FRAGMENT_NAME +" onCreate");
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, FRAGMENT_NAME +" onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        connectionDetector = new ConnectionDetector(activity);


        recyclerView = view.findViewById(R.id.recycle_latest_noti);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));

        infiniteCycleViewPager = (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.viewLo);

        drawerOpener=view.findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });



        progressBar = view.findViewById(R.id.bannar_progress);
        scheduleButton = view.findViewById(R.id.schedule_button);
        redeemBtn = view.findViewById(R.id.redeem_btn);
        rideHistory = view.findViewById(R.id.ride_history);
        newRide = view.findViewById(R.id.new_ride);
        allNoti = view.findViewById(R.id.allnoti);
        quesRedeem = view.findViewById(R.id.redeem_ques);
        quesLevel = view.findViewById(R.id.level_ques);
        cardViewFree = view.findViewById(R.id.free_card);
        cardViewPaid = view.findViewById(R.id.paid_card);
        textViewServiceDate = view.findViewById(R.id.service_date);
        textViewServiveName = view.findViewById(R.id.service_name);
        textViewCom1 = view.findViewById(R.id.com1);
        textViewCom2 = view.findViewById(R.id.com2);
        textViewFreePercent = view.findViewById(R.id.free_percent);
        textViewPaidPercent = view.findViewById(R.id.paid_percent);
        textViewFreeRemain = view.findViewById(R.id.free_remaining_text);
        textViewPaidRemain = view.findViewById(R.id.paid_remaining_text);
        progressBarFree = view.findViewById(R.id.free_service_progress_bar);
        progressBarPaid = view.findViewById(R.id.paid_service_progress_bar);
        textViewUnseen = view.findViewById(R.id.unseen_text);
        imageViewRed = view.findViewById(R.id.red_circle);
        textViewNotiSentence = view.findViewById(R.id.points5);
        textViewupcoming_service = view.findViewById(R.id.upcoming_service);



        pullToRefresh = view.findViewById(R.id.swipe);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInfo(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals(CLIENT)){
                    Intent intent = new Intent(activity, ServiceSchedule.class);
                    intent.putExtra(EXTRA_SURVICE_HEADER,ALL_SURVICES);
                    startActivity(intent);
                }
                else if(userType.equals(NONCLIENT)){
                    if(alreadyApplied){
                        Toast.makeText(activity, "You request has already been sent to admin for approval!", Toast.LENGTH_LONG).show();
                    }else{
                        launchDismissDlg(SCHEDULE);
                    }

                }



            }
        });

        allNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AllNotifications.class);
                startActivity(intent);
            }
        });
        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, RedeemList.class);
                startActivity(intent);
            }
        });

        rideHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RideHistoryActivity.class);
                startActivity(intent);
            }
        });

        newRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!connectionDetector.isConnected()) {
                    Toast.makeText(activity, NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){


                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                            }, 10);
                            return;
                        } else {

                            gotoStoragePermission();







                        }


                    } else {

                        gotoStoragePermission();



                    }
                }


            }
        });

        //loadInfo();
        //loadLatestNotifications();



        /*infiniteCycleViewPager.setScrollDuration(500);
        infiniteCycleViewPager.setInterpolator(AnimationUtils.loadInterpolator(activity, android.R.anim.overshoot_interpolator));
        infiniteCycleViewPager.setMediumScaled(true);
        infiniteCycleViewPager.setMaxPageScale(0.8F);
        infiniteCycleViewPager.setMinPageScale(0.5F);
        infiniteCycleViewPager.setCenterPageScaleOffset(30.0F);
        infiniteCycleViewPager.setMinPageScaleOffset(5.0F);
        infiniteCycleViewPager.setOnInfiniteCyclePageTransformListener(new OnInfiniteCyclePageTransformListener() {
            @Override
            public void onPreTransform(View page, float position) {

            }

            @Override
            public void onPostTransform(View page, float position) {

            }
        });*/



       //new one


/*       FragmentManager fragmentManager = getFragmentManager();

        pager = (ViewPager) view.findViewById(R.id.view_pager);

        adapter = new MyPagerAdapter(activity, fragmentManager);
        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-200);*/


        quesRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDismissDlg(REDEEM_QUES);
            }
        });

        quesLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDismissDlg(LEVEL_QUES);
            }
        });

        cardViewPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userType.equals(CLIENT)){
                    Intent intent = new Intent(activity, ServiceSchedule.class);
                    intent.putExtra(EXTRA_SURVICE_HEADER,PAID_SURVICES);
                    startActivity(intent);
                }
                else if(userType.equals(NONCLIENT)){
                    launchDismissDlg(PAID_QUES);
                }

            }
        });

        cardViewFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals(CLIENT)){
                    Intent intent = new Intent(activity, ServiceSchedule.class);
                    intent.putExtra(EXTRA_SURVICE_HEADER,FREE_SURVICES);
                    startActivity(intent);
                }
                else if(userType.equals(NONCLIENT)){
                    launchDismissDlg(FREE_QUES);
                }

            }
        });



        return view;
    }

    private void clientViews() {
        quesRedeem.setVisibility(View.GONE);
        quesLevel.setVisibility(View.GONE);
        textViewServiceDate.setVisibility(View.VISIBLE);
        textViewCom1.setVisibility(View.VISIBLE);
        textViewCom2.setVisibility(View.VISIBLE);
        //textViewServiveName.setText("Free Service 2");
        scheduleButton.setText("Service Schedule");
        textViewServiveName.setText(stringServiceName);
        textViewServiceDate.setText("( "+stringStartDate+" to "+stringEndDate+" )");
        textViewupcoming_service.setText(type11+" Service");
        int free=complete(integerFreePassed);
        int paid=complete(integerPaidPassed);

        textViewFreePercent.setText(""+free+"%");
        textViewPaidPercent.setText(""+paid+"%");

        String temp1="Remaining : "+(6-integerFreePassed)+" / 6";
        textViewFreeRemain.setText(temp1);
        String temp2="Remaining : "+(6-integerPaidPassed)+" / 6";
        textViewPaidRemain.setText(temp2);
        progressBarFree.setProgress(free);
        progressBarPaid.setProgress(paid);

    }

    private int complete(Integer integer) {
        int i =(integer*100)/6;
        return i;
    }


    private void nonClientViews() {

        if(alreadyApplied){
            scheduleButton.setText("Already Applied");
        }

    }




    private void gotoStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 11);
                return;
            } else {

                gotoNewRide();







            }


        } else {

            gotoNewRide();



        }
    }


    private void gotoNewRide() {
        Intent intent = new Intent(activity, RideCreation.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gotoStoragePermission();


                } else {


                    Toast.makeText(activity, "Yot can't start a new ride without location Permission!", Toast.LENGTH_LONG).show();


                }
                return;

            case 11:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gotoNewRide();


                } else {


                    Toast.makeText(activity, "Yot can't start a new ride without storage Permission!", Toast.LENGTH_LONG).show();


                }
                return;

        }

    }

    private void launchDismissDlg(int i) {

        modelPicked=false;colorPicked=false;
        purchaseDatePicked=false;

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_add_bike);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final TextView headerText =  dialog.findViewById(R.id.header);
        final TextView bodyText =  dialog.findViewById(R.id.body);
        final Button addChassis =  dialog.findViewById(R.id.add_chassis);

        final ConstraintLayout approval =  dialog.findViewById(R.id.request_btn);
        final TextView addText =  dialog.findViewById(R.id.add_text);
        final EditText editChassis =  dialog.findViewById(R.id.edit_chassis);

        ConstraintLayout BikeSelect = dialog.findViewById(R.id.bike_select_con);
        ConstraintLayout ColorSelect = dialog.findViewById(R.id.color_select_con);
        ConstraintLayout DateSelect = dialog.findViewById(R.id.date_select_con);
        final ConstraintLayout NewLay = dialog.findViewById(R.id.new_lay);
        Drop1 = dialog.findViewById(R.id.drop1);
        Drop2 = dialog.findViewById(R.id.drop2);

        recyclerViewBike = dialog.findViewById(R.id.bike_recycler);
        recyclerViewColor = dialog.findViewById(R.id.color_recycler);

        textViewModel = dialog.findViewById(R.id.model_name);
        textViewColor = dialog.findViewById(R.id.color_name);
        textViewPurchase = dialog.findViewById(R.id.purchase_date);


        recyclerViewColor.setLayoutManager(new GridLayoutManager(activity, 1));
        recyclerViewBike.setLayoutManager(new GridLayoutManager(activity, 1));

        loadBikesForRequest();

        if(i==REDEEM_QUES){
            headerText.setText("Add bike to earn points!");
            bodyText.setText("Start earning points by adding your bike's chassis number. Later, redeem those points by enjoying various offers!");
        }
        else if(i==LEVEL_QUES){
            headerText.setText("Add bike to Upgrade Level!");
            bodyText.setText("Start upgrading your level by adding your chassis number first! Then earn point by executing various tasks given by Suzuki team!");
        }
        else if(i==PAID_QUES){
            headerText.setText("Paid Service is available for only Suzuki Bikers!");
            bodyText.setText("To maintain the physical condition of your bike, you must take scheduled paid services regularly. Add your bike to check your upcoming scheduled paid service dates.");
        }
        else if(i==FREE_QUES){

            headerText.setText("Free Service is available for only Suzuki Bikers!");
            bodyText.setText("To maintain the physical condition of your bike, you must take scheduled free services regularly. Add your bike to check your upcoming scheduled free service dates.");
        }
        else if(i==SCHEDULE){

            headerText.setVisibility(View.GONE);
            bodyText.setVisibility(View.GONE);
            addChassis.setVisibility(View.GONE);

            approval.setVisibility(View.VISIBLE);
            addText.setVisibility(View.VISIBLE);
            editChassis.setVisibility(View.VISIBLE);
            NewLay.setVisibility(View.VISIBLE);

        }


        if(alreadyApplied){
            addChassis.setText("Already Applied");
        }

        addChassis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alreadyApplied){
                    dialog.dismiss();
                    Toast.makeText(activity, "You request has already been sent to admin for approval!", Toast.LENGTH_LONG).show();
                }
                else{
                    addChassis.setVisibility(View.GONE);

                    approval.setVisibility(View.VISIBLE);
                    addText.setVisibility(View.VISIBLE);
                    editChassis.setVisibility(View.VISIBLE);
                    NewLay.setVisibility(View.VISIBLE);
                }



            }
        });

        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editChassis.getText().toString().trim().isEmpty()){
                    if (editChassis.getText().toString().length()>16){

                        if(modelPicked){
                            if(colorPicked){
                                if(purchaseDatePicked){


                                    dialog.dismiss();
                                    AddingChassis(activity,editChassis.getText().toString(),bikeId,colorId,datePicked);

                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {


                                            loadInfo();

                                        }

                                    }, 500);


                                    //Toast.makeText(activity, "Sent to admin for approval", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(activity, "Set your bike purchase date!", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(activity, "Pick your bike color!", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else{
                            Toast.makeText(activity, "Pick your bike model!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(activity, "Chassis number length must be 17 or more!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(activity, "Empty Chassis Number!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        BikeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drop2.setVisibility(View.GONE);

                if(Drop1.getVisibility()==View.VISIBLE){
                    Drop1.setVisibility(View.GONE);
                }
                else{
                    Drop1.setVisibility(View.VISIBLE);
                }

            }
        });

        ColorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modelPicked){
                    if(Drop2.getVisibility()==View.VISIBLE){
                        Drop2.setVisibility(View.GONE);
                    }
                    else{
                        Drop2.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    Toast.makeText(activity, "Select bike first!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        DateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        Calendar cal = Calendar.getInstance();

                        if(before(calendar,cal)){
                            String s1 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

                            textViewPurchase.setText(s1);
                            purchaseDatePicked=true;
                            datePicked=s1;

                        }
                        else
                        {
                            Toast.makeText(activity, "You can't pick a date before than today!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });





        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void loadBikesForRequest() {


        getUrlInstance(BIKE_COLOR_LIST,new CustomCallBack(progressBar,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponseBikeColor = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {




                    JSONObject fileObject = null;
                    JSONObject dataObject = null;
                    JSONArray BikesArray = null;
                    arrayListOfBikes = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponseNow " + myResponseBikeColor);
                        fileObject = new JSONObject(myResponseBikeColor);
                        dataObject = fileObject.getJSONObject("data");

                        BikesArray = dataObject.getJSONArray("Bikes");

                        colorObject = dataObject.getJSONObject("color");



                        for (int i = 0; i < BikesArray.length(); i++) {

                            JSONObject BikesArrayObject = null;

                            BikesArrayObject = BikesArray.getJSONObject(i);
                            BikeAndColor bikeAndColor = new BikeAndColor();
                            bikeAndColor.setId(BikesArrayObject.getString("id"));
                            bikeAndColor.setName(BikesArrayObject.getString("name"));
                            bikeAndColor.setType(BIKE);


                            arrayListOfBikes.add(bikeAndColor);


                        }


                    } catch (JSONException e) {

                        progressBar.setVisibility(View.GONE);
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            BikeAndColorRecyclerAdapter bikeAndColorRecyclerAdapter = new BikeAndColorRecyclerAdapter(activity,arrayListOfBikes,HomeFragment.this);

                            recyclerViewBike.setAdapter(bikeAndColorRecyclerAdapter);




                        }
                    });


                } else {

                    if(progressBar!=null){
                        //progressBar.setVisibility(View.GONE);
                    }

                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

    }

    private void loadBanner() {

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        RequestBody formBody = new FormBody.Builder()
                .add("offer_type_id","2" )
                .build();


        getUrlInstance(ALL_OFFERS,formBody,"Authorization",token,new CustomCallBack(progressBar,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {




                    JSONObject fileObject = null;
                    JSONArray BannarImagesArray = null;
                    final List<BannarImages> arrayListOfBannarImages = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        BannarImagesArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < BannarImagesArray.length(); i++) {

                            JSONObject BannarImagesArrayObject = null;

                            BannarImagesArrayObject = BannarImagesArray.getJSONObject(i);
                            BannarImages singleBannarImage = new BannarImages();
                            singleBannarImage.setBannar(BannarImagesArrayObject.getString("image"));
                            singleBannarImage.setBannarText(BannarImagesArrayObject.getString("description"));
                            singleBannarImage.setBannarTitle(BannarImagesArrayObject.getString("title"));
                            singleBannarImage.setBannarSubtitle(BannarImagesArrayObject.getString("sub_title"));
                            singleBannarImage.setBannarTime(BannarImagesArrayObject.getString("created_at"));
                            singleBannarImage.setBannarUrl(BannarImagesArrayObject.getString("url"));



                            /*singleBannarImage.setBannarText("Nunc de hominis summo bono quaeritur; Minime vero istorum quidem, inquit. Quid in isto egregio tuo officio et tanta fide-sic enim existimo-ad corpus refers? Nos vero, inquit ille; Nos vero, inquit ille; Qui ita affectus, beatum esse numquam probabis; Que Manilium, ab iisque M.\n" +
                                    "\n" +
                                    "In primo enim ortu inest teneritas ac mollitia quaedam, ut nec res videre optimas nec agere possint. Inde sermone vario sex illa a Dipylo stadia confecimus. Ergo adhuc, quantum equidem intellego, causa non videtur fNunc de hominis summo bono quaeritur; Minime vero istorum quidem, inquit. Quid in isto egregio tuo officio et tanta fide-sic enim existimo-ad corpus refers? Nos vero, inquit ille; Nos vero, inquit ille; Qui ita affectus, beatum esse numquam probabis; Que Manilium, ab iisque M.\\n\" +\n" +
                                    "                                    \"\\n\" +\n" +
                                    "                                    \"In primo enim ortu inest teneritas ac mollitia quaedam, ut nec res videre optimas nec agere possint. Inde sermone vario sex illa a Dipylo stadia confecimus. Ergo adhuc, quantum equidem intellego, causa non videtur fuisuisse mutandi nominis. Vitiosum est enim in dividendo partem in genere numerare.");
                            //singleBannarImage.setBannarText("null");
                            singleBannarImage.setBannarTitle("New Suzuki Intruder");
                            singleBannarImage.setBannarSubtitle("Brand New Cruiser");
                            singleBannarImage.setBannarTime("Offer Started at : "+"18 December, 2018 12:00 am");
*/






                            arrayListOfBannarImages.add(singleBannarImage);


                        }


                    } catch (JSONException e) {

                        progressBar.setVisibility(View.GONE);
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            homeBannarAdapter = new HomeBannarAdapter(activity,arrayListOfBannarImages);

                            infiniteCycleViewPager.setAdapter(homeBannarAdapter);
                            loadAgain=false;

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    progressBar.setVisibility(View.GONE);
                                    //progressDialog.dismiss();

                                }

                            }, 500);



                        }
                    });


                } else {

                    if(progressBar!=null){
                        //progressBar.setVisibility(View.GONE);
                    }

                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });
    }
    private void loadInfo() {
        if(progressDialogAPi==null){
            progressDialogAPi = new ProgressDialog(activity);
            progressDialogAPi.setMessage("Please wait .. ");
            progressDialogAPi.setCanceledOnTouchOutside(false);
            progressDialogAPi.setCancelable(false);
            progressDialogAPi.show();
        }




        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(HOME_PAGE_USER_INFO,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse2 = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONObject dataObject=null;
                JSONObject upcomungObject=null;
                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponse2);
                        LogPrint("finalll",myResponse2);
                        message = responseJson.getString("message");
                        dataObject = responseJson.getJSONObject("data");
                        //upcomungObject = dataObject.getJSONObject("upcoming");

                        userType = dataObject.getString("user_type");
                        unseen = dataObject.getString("count");
                        alreadyApplied = dataObject.getBoolean("already_applied");

                        if(userType.equals(CLIENT)){
                            integerFreePassed = dataObject.getInt("free");
                            integerPaidPassed = dataObject.getInt("paid");

                            type11 = dataObject.getString("type");

                            if(type11.equals("UpComing")){
                                upcomungObject = dataObject.getJSONObject("UpComing");
                            }
                            else{
                                upcomungObject = dataObject.getJSONObject("Running");
                            }

                            stringStartDate=upcomungObject.getString("start_date");
                            stringEndDate=upcomungObject.getString("end_date");
                            stringServiceName=upcomungObject.getString("name");

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();


                            if(userType.equals(CLIENT)){
                                LogPrint("kidddd",userType);
                                clientViews();
                            }
                            else if(userType.equals(NONCLIENT)){
                                LogPrint("kidddd",userType);
                                nonClientViews();
                            }
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    progressDialogAPi.dismiss();

                                }

                            }, 300);



                            String s;
                            imageViewRed.setVisibility(View.VISIBLE);
                            textViewUnseen.setVisibility(View.VISIBLE);
                        if(unseen.equals("0")){
                            textViewUnseen.setVisibility(View.GONE);
                            imageViewRed.setVisibility(View.GONE);
                            s="You have no new Notification.";
                        }
                        else if(unseen.equals("1")){

                            textViewUnseen.setText(unseen);
                            s="You have 1 new Notification!";
                        }
                        else{
                            textViewUnseen.setText(unseen);
                            s="You have "+unseen+" new Notifications!";
                        }

                            textViewNotiSentence.setText(s);


                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(activity,"Error!");
                }
            }


        });
    }


    public void stopAppfromFragment_self_implemented() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if(loadAgain){
            loadBanner();
        }
        loadInfo();
        super.onResume();
    }




    private void loadTheColors(String id) {



        List<BikeAndColor> arrayListOfColors = new ArrayList<>();
        try {


            JSONArray colorArray = colorObject.getJSONArray(id);

            for (int i = 0; i < colorArray.length(); i++) {

                JSONObject ColorArrayObject = null;

                ColorArrayObject = colorArray.getJSONObject(i);
                BikeAndColor bikeAndColor = new BikeAndColor();
                bikeAndColor.setId(ColorArrayObject.getString("color_id"));
                bikeAndColor.setName(ColorArrayObject.getString("color_name"));
                bikeAndColor.setColor(ColorArrayObject.getString("color_code"));
                bikeAndColor.setType(COLOR);


                arrayListOfColors.add(bikeAndColor);


            }


        } catch (JSONException e) {

            progressBar.setVisibility(View.GONE);
            backgroundThreadShortToast(getActivity(), ""+e);
            e.printStackTrace();
        }

        BikeAndColorRecyclerAdapter bikeAndColorRecyclerAdapter2 = new BikeAndColorRecyclerAdapter(activity,arrayListOfColors,HomeFragment.this);

        recyclerViewColor.setAdapter(bikeAndColorRecyclerAdapter2);


    }

    @Override
    public void onSelected(BikeAndColor bikeAndColor) {
        if(bikeAndColor.getType().equals(BIKE)){

            bikeId=bikeAndColor.getId();
            modelPicked=true;
            textViewModel.setText(bikeAndColor.getName());
            Drop1.setVisibility(View.GONE);

            colorId="";
            colorPicked=false;
            textViewColor.setText("Set bike color");
            Drop2.setVisibility(View.GONE);




            loadTheColors(bikeAndColor.getId());
        }
        else if(bikeAndColor.getType().equals(COLOR)){

            colorId=bikeAndColor.getId();
            colorPicked=true;
            textViewColor.setText(bikeAndColor.getName());
            Drop2.setVisibility(View.GONE);

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
                return c1Day<=c2Day;
            }
        }
    }
}
