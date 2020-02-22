/*
 * Created by Alvi Khalil on 10/24/18 11:31 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/11/18 10:29 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MapListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LocationPoints;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConnectionDetector;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.MyLocation;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.DISTRICT_LIST_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.DISTRICT_LOCATIONS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEAREST_LOCATIONS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrintExcepWithThrow;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrintException;


public class LocationFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    boolean mLocationPermissionGranted = false;
    com.google.android.gms.maps.GoogleMap mMap;
    Location mLastKnownLocation;
    FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = LocationFragment.class.getSimpleName();

    private static final String TAG = "checka";

    ImageView drawerOpener,myMapLocationButton,search;
    Button dealerBtn,serviceBtn;
    TextView dealTxt,serviceTxt;
    Activity activity;
    AutoCompleteTextView autoCompleteTextView;
    LinearLayout linearLayout,blackLayout;
    Switch aSwitch;
    RecyclerView recyclerview;
    ProgressDialog progressDialog;
    String myResponse;
    String message;
    String[] names ;
    String[] banglaNames ;
    Integer[] ids;
    String pointType="dealer",districtId="",districtName="";
    String ApiSelector="";
    public static final String CURRENT_LOCATION_API="current_location_api";
    public static final String DISTRICT_API="district_api";
    private LocationManager locationManager;
    boolean already=false;
    private double latitude, longitude;

    private boolean newNetIsOn=false;
    public ConnectionDetector connectionDetector;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity =(Activity) context;
        Log.i(TAG, FRAGMENT_NAME +" onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, FRAGMENT_NAME +" onCreate");
    }


    private static final String[] DISTRICTS = new String[]{
            "Kushtia", "Khulna", "Dhaka","Sylhet","Chittagong","Mymensing"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, FRAGMENT_NAME +" onCreateView");
        View view = inflater.inflate(R.layout.fragment_location, container, false);


        connectionDetector = new ConnectionDetector(activity);

        if (connectionDetector.isConnected()) {
            newNetIsOn=true;
        }


        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        drawerOpener=view.findViewById(R.id.menu);
        dealerBtn=view.findViewById(R.id.dealers);
        serviceBtn=view.findViewById(R.id.service);
        dealTxt=view.findViewById(R.id.dealers_text);
        serviceTxt=view.findViewById(R.id.service_text);
        myMapLocationButton=view.findViewById(R.id.myMapLocationButton);
        autoCompleteTextView=view.findViewById(R.id.auto_com_text);
        search=view.findViewById(R.id.search_logo);
        linearLayout=view.findViewById(R.id.drop);
        blackLayout=view.findViewById(R.id.black_layout);
        aSwitch=view.findViewById(R.id.switch1);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(activity, 1));




        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                LogPrint(TAG, "isChecked " + isChecked);
                ((MainActivity) activity).hideKeyboard();
                if(isChecked){
                    disappear();



                }else{

                    appear();

                }
            }
        });
        serviceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointType="service";
                loadAnApi();
                //getLocationPoints(pointType,districtId);
                serviceBtn.setVisibility(View.VISIBLE);
                dealerBtn.setVisibility(View.GONE);

            }
        });
        dealTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointType="dealer";
                loadAnApi();
                //getLocationPoints(pointType,districtId);
                serviceBtn.setVisibility(View.GONE);
                dealerBtn.setVisibility(View.VISIBLE);

            }
        });
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();

            }
        });



        getLocationPermission();

        //map start
        mFusedLocationProviderClient = new FusedLocationProviderClient(activity);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myMapLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
                ApiSelector = CURRENT_LOCATION_API;
                loadAnApi();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 15));
            }
        });

        //map end




        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String input = autoCompleteTextView.getText().toString();

                for(int i=0;i<names.length;i++){
                    if(input.equalsIgnoreCase(names[i])){

                        //Toast.makeText(activity, ""+ids[i], Toast.LENGTH_SHORT).show();
                        districtId=""+ids[i];
                        districtName=names[i];
                        ApiSelector=DISTRICT_API;
                        loadAnApi();
                        //getLocationPoints(pointType,districtId);
                        break;
                    }
                }

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = autoCompleteTextView.getText().toString();
                if(input.equals("")){
                    Toast.makeText(activity, "Empty search bar!", Toast.LENGTH_SHORT).show();
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
                        ApiSelector=DISTRICT_API;
                        loadAnApi();
                        //getLocationPoints(pointType,districtId);
                    }
                    else {
                        Toast.makeText(activity, "Invalid district name!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




        return view;
    }

    private void districLoadingFunc() {

        if(progressDialog==null){
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait ..");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        getUrlInstance(DISTRICT_LIST_API, new CustomCallBack(progressDialog,activity) {
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
                    JSONArray districtArray = null;
                    try {
                        fileObject = new JSONObject(myResponse);
                        districtArray = fileObject.getJSONArray("data");
                        //pathN=""+pathName;

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        backgroundThreadShortToast(getActivity(), "Server Error!");
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
                            backgroundThreadShortToast(getActivity(), "Server Error!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.suggestions_item,R.id.text_suggest,names);
                            autoCompleteTextView.setAdapter(adapter);

                        }
                    });


                } else {
                    progressDialog.dismiss();
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

    }


    private void appear() {
        Animation topDown = AnimationUtils.loadAnimation(activity,
                R.anim.top_down);

        linearLayout.setAnimation(topDown);
        linearLayout.setVisibility(View.VISIBLE);


        Animation fadeIn = AnimationUtils.loadAnimation(activity,
                R.anim.fade_in2);

        blackLayout.setAnimation(fadeIn);
        blackLayout.setVisibility(View.VISIBLE);

        recyclerview.setAnimation(fadeIn);
        recyclerview.setVisibility(View.VISIBLE);

    }
    private void disappear() {
        Animation topUp = AnimationUtils.loadAnimation(activity,
                R.anim.top_up);

        linearLayout.setAnimation(topUp);

        linearLayout.setVisibility(View.GONE);

        Animation fadeOut = AnimationUtils.loadAnimation(activity,
                R.anim.fade_out);

        blackLayout.setAnimation(fadeOut);
        blackLayout.setVisibility(View.GONE);

        recyclerview.setAnimation(fadeOut);
        recyclerview.setVisibility(View.GONE);


    }


    public void getLocationPoints(String type,String districtID){

        final ProgressDialog pdgetLocationPoints = new ProgressDialog(activity);
        pdgetLocationPoints.setMessage("Loading "+type+" points in "+districtName+" ..");
        pdgetLocationPoints.setCancelable(false);
        pdgetLocationPoints.setCanceledOnTouchOutside(false);
        pdgetLocationPoints.show();


        if(mMap != null){
            mMap.clear();

            // add markers from database to the map
        }
        RequestBody formBody = new FormBody.Builder()
                .add("type", type)
                .add("district_id", districtID)
                .build();

        getUrlInstance(DISTRICT_LOCATIONS_API,formBody, new CustomCallBack(pdgetLocationPoints,activity) {
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
                    pdgetLocationPoints.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray LocationPointsArray = null;
                    final List<LocationPoints> arrayListOfLocationPoints = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        LocationPointsArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < LocationPointsArray.length(); i++) {

                            JSONObject locationPointsArrayObject = null;

                            locationPointsArrayObject = LocationPointsArray.getJSONObject(i);
                            LocationPoints singleLocationPoint = new LocationPoints();
                            singleLocationPoint.setHeader(locationPointsArrayObject.getString("title"));
                            singleLocationPoint.setBody(locationPointsArrayObject.getString("address"));
                            singleLocationPoint.setLogitude(locationPointsArrayObject.getDouble("longitude"));
                            singleLocationPoint.setLatitude(locationPointsArrayObject.getDouble("latitude"));
                            singleLocationPoint.setContact(locationPointsArrayObject.getString("contact_number"));
                            singleLocationPoint.setImageID(R.drawable.ban1);






                            arrayListOfLocationPoints.add(singleLocationPoint);


                        }


                    } catch (JSONException e) {
                        pdgetLocationPoints.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdgetLocationPoints.dismiss();
                            MapListRecyclerAdapter customAdapter = new MapListRecyclerAdapter(activity, arrayListOfLocationPoints);
                            recyclerview.setAdapter(customAdapter);
                            addMarkers(arrayListOfLocationPoints);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdgetLocationPoints.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });

    }


    public void getLocationPointsArroundMe(String type,String latitude,String longitude){

        final ProgressDialog getLocationPointsArroundMepd = new ProgressDialog(activity);
        getLocationPointsArroundMepd.setMessage("Loading nearest "+type+" points ..");
        getLocationPointsArroundMepd.setCancelable(false);
        getLocationPointsArroundMepd.setCanceledOnTouchOutside(false);
        getLocationPointsArroundMepd.show();


        if(mMap != null){
            mMap.clear();

            // add markers from database to the map
        }
        RequestBody formBody = new FormBody.Builder()
                .add("type", type)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .build();

        getUrlInstance(NEAREST_LOCATIONS_API,formBody, new CustomCallBack(getLocationPointsArroundMepd,activity) {
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
                    getLocationPointsArroundMepd.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray LocationPointsArray = null;
                    final List<LocationPoints> arrayListOfLocationPoints = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        LocationPointsArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < LocationPointsArray.length(); i++) {

                            JSONObject locationPointsArrayObject = null;

                            locationPointsArrayObject = LocationPointsArray.getJSONObject(i);
                            LocationPoints singleLocationPoint = new LocationPoints();
                            singleLocationPoint.setHeader(locationPointsArrayObject.getString("title"));
                            singleLocationPoint.setBody(locationPointsArrayObject.getString("address"));
                            singleLocationPoint.setLogitude(locationPointsArrayObject.getDouble("longitude"));
                            singleLocationPoint.setLatitude(locationPointsArrayObject.getDouble("latitude"));
                            singleLocationPoint.setContact(locationPointsArrayObject.getString("contact_number"));
                            singleLocationPoint.setImageID(R.drawable.ban1);






                            arrayListOfLocationPoints.add(singleLocationPoint);


                        }


                    } catch (JSONException e) {
                        getLocationPointsArroundMepd.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            getLocationPointsArroundMepd.dismiss();
                            MapListRecyclerAdapter customAdapter = new MapListRecyclerAdapter(activity, arrayListOfLocationPoints);
                            recyclerview.setAdapter(customAdapter);
                            addMarkers(arrayListOfLocationPoints);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    getLocationPointsArroundMepd.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });

    }

    private void addMarkers(List<LocationPoints> arrayListOfLocationPoints) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();



        if(pointType.equals("dealer")){
            for(int i=0;i<arrayListOfLocationPoints.size();i++){
                LocationPoints locationPoints = arrayListOfLocationPoints.get(i);

                builder.include(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locationPoints.getLatitude(), locationPoints.getLogitude()))
                        .title(locationPoints.getHeader())
                        .snippet(locationPoints.getBody())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location))).getPosition());


            }
        }
        else{
            for(int i=0;i<arrayListOfLocationPoints.size();i++){
                LocationPoints locationPoints = arrayListOfLocationPoints.get(i);

                builder.include(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locationPoints.getLatitude(), locationPoints.getLogitude()))
                        .title(locationPoints.getHeader())
                        .snippet(locationPoints.getBody())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_2))).getPosition());


            }
        }






        if(arrayListOfLocationPoints.size()>1){
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.20); // offset from edges of the map 10% of screen


            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.animateCamera(cu);
        }
        else if(arrayListOfLocationPoints.size()==1){
            LocationPoints locationPoints = arrayListOfLocationPoints.get(0);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationPoints.getLatitude(), locationPoints.getLogitude()), 15));

        }
        else{
            Toast.makeText(activity, "No locations found!", Toast.LENGTH_SHORT).show();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.791344, 90.402542), 10));
        }


    }


    private void getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            } else {

                mLocationPermissionGranted = true;







            }


        } else {

            mLocationPermissionGranted = true;



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mLocationPermissionGranted = true;
                    if(newNetIsOn){
                        twoFuncCaller();
                    }



                } else {


                    launchDismissDlg("");


                }

        }

    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        mMap = googleMap;
        LogPrint(TAG, "ready");

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            LogPrint(TAG, "Try");

            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_black));
            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_dark_blue));
            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_best));
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_silver));

            if (!success) {
                LogPrint(TAG, "Failed.");

                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            LogPrint(TAG, "Catch");

            Log.e(TAG, "Can't find style. Error: ", e);
        }


     /*   googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.791344, 90.404973))
                .title("Dealer1")
                .snippet("short address1")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_dealer)));


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.792383, 90.404080))
                .title("Dealer2")
                .snippet("short address2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_dealer)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.790658, 90.403710))
                .title("Dealer3")
                .snippet("short address3")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_dealer)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.792018, 90.401611))
                .title("Dealer4")
                .snippet("short address4")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_dealer)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.793615, 90.402542))
                .title("Dealer5")
                .snippet("short address5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_dealer)));*/

        //googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID);
        //googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE);
        //googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);


        // set marker at current location


        // set map layout initial position
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.791344, 90.402542), 5));

        // Turn on the My Location layer and the related control on the map.
        if(mLocationPermissionGranted && newNetIsOn){

            twoFuncCaller();



        }



    }

    private void twoFuncCaller() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !already) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            already=true;
            if(newNetIsOn){
                twoFuncCaller();
            }



        }
        else{
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        }
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                //myMapLocationButton.setVisibility(View.VISIBLE);
                ApiSelector = CURRENT_LOCATION_API;

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                //myMapLocationButton.setVisibility(View.GONE);
                ApiSelector=DISTRICT_API;


            }
        } catch (SecurityException e) {
            LogPrintException("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            //Toast.makeText(activity, "1", Toast.LENGTH_SHORT).show();
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(activity, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            LogPrint(TAG, "check : " + (mLastKnownLocation == null));
                            //LogPrint(TAG,"check : "+(mLastKnownLocation==null));

                            if (mLastKnownLocation != null){
                                //Toast.makeText(activity, "3", Toast.LENGTH_SHORT).show();
                                LogPrint(TAG, "moving camera");

                                latitude =mLastKnownLocation.getLatitude();
                                longitude =mLastKnownLocation.getLongitude();
                                ApiSelector = CURRENT_LOCATION_API;
                                loadAnApi();
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 5));

                            }
                            else {
                                //Toast.makeText(activity, "4", Toast.LENGTH_SHORT).show();
                                secondary();

                                //Toast.makeText(activity, "Null disse ken?", Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            LogPrint(TAG, "Current location is null. Using defaults.");
                            LogPrintExcepWithThrow(TAG, "Exception: %s", task.getException());

                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 5));
                            launchDismissDlg("Your GPS is not working properly!");
                            //ApiSelector=DISTRICT_API;
                            //loadAnApi();
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                });
            } else {
                LogPrint(TAG, "Current location permission is not given");
                ApiSelector=DISTRICT_API;
                loadAnApi();
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 5));

            }
        } catch (SecurityException e) {
            secondary();
            LogPrintException("Exception: %s", e.getMessage());
        }
    }


    private void secondary(){
        launchDismissDlg("Your GPS is not working properly!");
        /*MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                Toast.makeText(activity, "oma", Toast.LENGTH_SHORT).show();
                //Got the location!
                if (location == null) {
                    Toast.makeText(activity, "5", Toast.LENGTH_SHORT).show();
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    launchDismissDlg("Your GPS is not working properly!");


                } else {
                    Toast.makeText(activity, "6", Toast.LENGTH_SHORT).show();
                    location.getLongitude();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    ApiSelector = CURRENT_LOCATION_API;
                    loadAnApi();

                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(activity, locationResult);*/
    }

    private void loadAnApi() {

        ((MainActivity) activity).hideKeyboard();

        if(ApiSelector.equals(CURRENT_LOCATION_API) ){

                myMapLocationButton.setVisibility(View.VISIBLE);
                getLocationPointsArroundMe(pointType,""+latitude,""+longitude);



        }
        else if(ApiSelector.equals(DISTRICT_API)){
            if(districtId.equals("")){

                launchDismissDlg("");

            }
            else{
                getLocationPoints(pointType,districtId);
            }

        }

    }

    private void launchDismissDlg(String s) {

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_district_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView DoneBtn =  dialog.findViewById(R.id.yes);
        TextView BodyText =  dialog.findViewById(R.id.body);

        if(!s.equals("")){
            BodyText.setText(s);
        }

        final AutoCompleteTextView dialogAutoCompleteTextView = dialog.findViewById(R.id.auto_com_text_dialog);
        final ProgressDialog pd=new ProgressDialog(activity);
        pd.setMessage("Please wait ..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        getUrlInstance(DISTRICT_LIST_API, new CustomCallBack(pd,activity) {
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
                    JSONArray districtArray = null;
                    try {
                        fileObject = new JSONObject(myResponse);
                        districtArray = fileObject.getJSONArray("data");
                        //pathN=""+pathName;

                    } catch (JSONException e) {
                        pd.dismiss();
                        backgroundThreadShortToast(getActivity(), "Server Error!");
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
                            pd.dismiss();
                            backgroundThreadShortToast(getActivity(), "Server Error!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pd.dismiss();


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.suggestions_item,R.id.text_suggest,names);
                            dialogAutoCompleteTextView.setAdapter(adapter);

                        }
                    });


                } else {
                    pd.dismiss();
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = dialogAutoCompleteTextView.getText().toString();
                if(input.equals("")){
                    Toast.makeText(activity, "No district selected!", Toast.LENGTH_SHORT).show();
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
                        dialog.dismiss();
                        ApiSelector =DISTRICT_API;
                        loadAnApi();
                        //getLocationPoints(pointType,districtId);
                    }
                    else {
                        Toast.makeText(activity, "Invalid district name!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        districLoadingFunc();

    }
}
