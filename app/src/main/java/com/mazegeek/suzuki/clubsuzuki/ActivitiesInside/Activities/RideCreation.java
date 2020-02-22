package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.Splash;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LocationPoints;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Tip;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.MyLocation;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.NetworkStateReceiver;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.Stopwatch;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LOGOUT_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEW_FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEW_RIDE_ENTRY;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrintExcepWithThrow;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class RideCreation extends AppCompatActivity implements OnMapReadyCallback,NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;

    SupportMapFragment mapFragment;
    com.google.android.gms.maps.GoogleMap mMap;
    Location mLastKnownLocation;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "checka";
    private static final String TAG2 = "checkaaaarr";
    private static final String TAG3 = "checkaaaarr3";
    private LocationManager locationManager;
    Dialog dialog;
    private double latitude=0, longitude=0;
    CardView startRide, pauseRide, stopRide;
    //TextView textViewTime;
    private Chronometer chronometer;
    private long pauseOffset = 0;
    private boolean running = false;
    TextView textViewPause;
    private LatLng firstPoint,secondPoint;
    boolean firstSet=false;
    ConstraintLayout constraintLayoutLater;
    TextView startLocationText,endLocationText;
    List<LatLng> arrayListOfPoints;
    float distance;
    String startTime,endTime,startLocation,endLocation,totalDistancce;
    File finalFile;
    ConstraintLayout rideEndDetails;
    TextView textViewStartTime,textViewStartLoc,textViewEndTime,textViewEndLoc,textViewDistance;
    Button buttonDone;
    String myResponse,message,errors;

    ConstraintLayout constraintLayoutRide;
    /*final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;

    Stopwatch timer = new Stopwatch();
    final int REFRESH_RATE = 100;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    textViewTime.setText(""+ timer.getElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    textViewTime.setText(""+ timer.getElapsedTime());
                    break;

                default:
                    break;
            }
            return true;
        }
    });
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_creation);

        startRide = findViewById(R.id.ride_start);
        pauseRide = findViewById(R.id.ride_pause);
        stopRide = findViewById(R.id.ride_stop);
        //textViewTime = findViewById(R.id.stop_time);
        textViewPause = findViewById(R.id.pause_text);
        constraintLayoutLater = findViewById(R.id.later_appear);
        startLocationText = findViewById(R.id.start_loc_text);
        endLocationText = findViewById(R.id.end_loc_text);
        rideEndDetails = findViewById(R.id.ride_end);

        textViewStartLoc = findViewById(R.id.ride_start_history_location);
        textViewStartTime = findViewById(R.id.ride_start_history_time);
        textViewEndLoc = findViewById(R.id.ride_end_history_location);
        textViewEndTime = findViewById(R.id.ride_end_history_time);
        textViewDistance = findViewById(R.id.distance_text);
        buttonDone = findViewById(R.id.done);



        constraintLayoutRide = findViewById(R.id.no_internet);





        chronometer = findViewById(R.id.chronometer);
        chronometer.setText("00:00:00");

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/poppins_light.ttf");
        chronometer.setTypeface(font, Typeface.NORMAL);
        // chronometer.setBase(SystemClock.elapsedRealtime());
        //      chronometer.setBase(System.currentTimeMillis());

        startRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latitude!=0&&longitude!=0){

                pauseRide.setVisibility(View.VISIBLE);
                stopRide.setVisibility(View.VISIBLE);
                startRide.setVisibility(View.GONE);
                setStartLocation();
                Animation fadeIn = AnimationUtils.loadAnimation(RideCreation.this, R.anim.fade_in);
                constraintLayoutLater.setAnimation(fadeIn);
                constraintLayoutLater.setVisibility(View.VISIBLE);

                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    //chronometer.setBase(SystemClock.elapsedRealtime()-3599000);
                    //                   chronometer.setBase(System.currentTimeMillis());
                    chronometer.start();
                    arrayListOfPoints = new ArrayList<>();
                    distance = 0;
                    startTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    running = true;


                }
                //mHandler.sendEmptyMessage(MSG_START_TIMER);
            }
            else{
                    Toast.makeText(RideCreation.this, "Your GPS is not working!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pauseRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = chronometer.getBase() - SystemClock.elapsedRealtime();
//                    pauseOffset =   System.currentTimeMillis() - chronometer.getBase();
                    running = false;
                    textViewPause.setText("Resume Ride");
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime() + pauseOffset);
//                    chronometer.setBase(System.currentTimeMillis()+ pauseOffset);
                    chronometer.start();
                    textViewPause.setText("Pause Ride");
                    running = true;
                }
            }
        });
        stopRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.setText("00:00:00");
//                chronometer.setBase(System.currentTimeMillis());
                pauseOffset = 0;
                running = false;
                textViewPause.setText("Pause Ride");
                pauseRide.setVisibility(View.GONE);
                stopRide.setVisibility(View.GONE);
                //startRide.setVisibility(View.VISIBLE);
                //Animation fadeOut = AnimationUtils.loadAnimation(RideCreation.this, R.anim.fade_out);
                //constraintLayoutLater.setAnimation(fadeOut);
               // constraintLayoutLater.setVisibility(View.GONE);
                endTime=java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                getDestination();
                //takeScreenshot();
                captureScreen();
                firstSet=false;


            }
        });


        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                /*long time = SystemClock.elapsedRealtime() - chronometer.getBase();
 //               long time = System.currentTimeMillis() - chronometer.getBase();
                Date date = new Date(time);
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String dateFormatted = formatter.format(date);
                chronometer.setText(dateFormatted);
//                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 100000) {
//                    chronometer.setBase(SystemClock.elapsedRealtime());
//                    Toast.makeText(RideCreation.this, "Bing!", Toast.LENGTH_SHORT).show();
//                }*/
                CharSequence text = chronometer.getText();
                if (text.length() == 5) {
                    chronometer.setText("00:" + text);
                } else if (text.length() == 7) {
                    chronometer.setText("0" + text);
                }
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", finalFile.getName(),RequestBody.create(MediaType.parse("image/jpeg"),finalFile))
                        .addFormDataPart("start_location",startLocation)
                        .addFormDataPart("start_time",startTime)
                        .addFormDataPart("end_location",endLocation)
                        .addFormDataPart("end_time",endTime)
                        .addFormDataPart("distance",totalDistancce)
                        .build();

                apiCaller(formBody);

            }
        });

        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null&&running) {
                        LogPrint(TAG2,"61");
                        //Toast.makeText(RideCreation.this, "hosse", Toast.LENGTH_SHORT).show();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //loadMylocation();

                        if(!firstSet){
                            firstPoint = new LatLng(latitude,longitude);
                            arrayListOfPoints.add(firstPoint);
                            LogPrint(TAG2,"fir");
                            firstSet=true;
                        }
                        else{
                            secondPoint = new LatLng(latitude,longitude);
                            arrayListOfPoints.add(secondPoint);
                            LogPrint(TAG2,"sec");
                            drawLine(firstPoint,secondPoint);
                        }

                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null&&running) {
                        LogPrint(TAG2,"62");
                        Toast.makeText(RideCreation.this, "hosse", Toast.LENGTH_SHORT).show();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //loadMylocation();

                        if(!firstSet){
                            firstPoint = new LatLng(latitude,longitude);
                            LogPrint(TAG2,"fir");
                            arrayListOfPoints.add(firstPoint);
                            firstSet=true;
                        }
                        else{
                            secondPoint = new LatLng(latitude,longitude);
                            LogPrint(TAG2,"sec");
                            arrayListOfPoints.add(secondPoint);
                            drawLine(firstPoint,secondPoint);
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));



    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    private void getDestination() {

        String cityName="";
        Geocoder geocoder = new Geocoder(RideCreation.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.get(0).getAddressLine(0) != null)
                cityName = addresses.get(0).getAddressLine(0);

            if (addresses.get(0).getAddressLine(1) != null)
                cityName = cityName + " " + addresses.get(0).getAddressLine(1);

            if (addresses.get(0).getAddressLine(2) != null)
                cityName = cityName + " " + addresses.get(0).getAddressLine(2);

            endLocationText.setText(cityName);

        } catch (IOException e) {
            e.printStackTrace();
            startLocationText.setText("Longitude : "+longitude+" & Latitude : "+latitude);
        }



    }

    private void setStartLocation() {




        String cityName="";
        Geocoder geocoder = new Geocoder(RideCreation.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.get(0).getAddressLine(0) != null)
                cityName = addresses.get(0).getAddressLine(0);

            if (addresses.get(0).getAddressLine(1) != null)
                cityName = cityName + " " + addresses.get(0).getAddressLine(1);

            if (addresses.get(0).getAddressLine(2) != null)
                cityName = cityName + " " + addresses.get(0).getAddressLine(2);

            startLocationText.setText(cityName);
        } catch (IOException e) {
            e.printStackTrace();
            startLocationText.setText("Longitude : "+longitude+" & Latitude : "+latitude);
        }






    }

    private void drawLine(LatLng firstPointParam, LatLng secondPointParam) {
        PolylineOptions polylineOptions = new PolylineOptions().add(firstPointParam).add(secondPointParam).width(5).color(ContextCompat.getColor(this, R.color.colorAccent)).geodesic(true);
        mMap.addPolyline(polylineOptions);
        firstPoint=secondPoint;


        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for(int i=0;i<arrayListOfPoints.size();i++){
            LatLng locationPoint = arrayListOfPoints.get(i);

            builder.include(locationPoint);


        }
        if(arrayListOfPoints.size()>1){
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.20); // offset from edges of the map 10% of screen


            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.animateCamera(cu);
        }
        else if(arrayListOfPoints.size()==1){
            LatLng locationPoint = arrayListOfPoints.get(0);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 18));

        }
        else{
            Toast.makeText(this, "No locations found!", Toast.LENGTH_SHORT).show();
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.791344, 90.402542), 10));
        }



        distance = distance+locationCalculate(firstPointParam,secondPointParam);
    }

    private float locationCalculate(LatLng firstPointParam, LatLng secondPointParam) {
        Location locationA = new Location("point A");

        locationA.setLatitude(firstPointParam.latitude);
        locationA.setLongitude(firstPointParam.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(secondPointParam.latitude);
        locationB.setLongitude(secondPointParam.longitude);

        float value = locationA.distanceTo(locationB)/1000;

        return value;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LogPrint(TAG, "ready");

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            LogPrint(TAG, "Try");

            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_black));
            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_dark_blue));
            //boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.map_best));
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_silver));

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


        gpsOnCheck();


    }

    private void gpsOnCheck() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            if (dialog == null) {
                LogPrint("kioo", "dhu");
                dialog = new Dialog(this, android.R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_box_tips);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView nextBtn = dialog.findViewById(R.id.yes);
                final TextView dismissBtn = dialog.findViewById(R.id.no);
                final TextView header = dialog.findViewById(R.id.header);
                final TextView body = dialog.findViewById(R.id.body);


                dismissBtn.setVisibility(View.GONE);

                header.setText("GPS is not working!");

                body.setText("Please check your GPS settings and see if GPS is turned on or not.");
                nextBtn.setText("Settings");

                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                });

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            } else {

            }


        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
            loadMylocation();

        }
    }


    @SuppressLint("MissingPermission")
    private void loadMylocation() {

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        Task locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();
                    // Set the map's camera position to the current location of the device.
                    mLastKnownLocation = (Location) task.getResult();
                    LogPrint(TAG, "check : " + (mLastKnownLocation == null));
                    //LogPrint(TAG,"check : "+(mLastKnownLocation==null));

                    if (mLastKnownLocation != null) {
                        //Toast.makeText(activity, "3", Toast.LENGTH_SHORT).show();
                        LogPrint(TAG, "moving camera");

                        latitude = mLastKnownLocation.getLatitude();
                        longitude = mLastKnownLocation.getLongitude();

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));

                    } else {
                        //Toast.makeText(RideCreation.this, "", Toast.LENGTH_SHORT).show();
                        secondary();

                    }


                } else {
                    backgroundThreadShortToast(RideCreation.this, "Your GPS is not working properly!");
                    LogPrint(TAG, "Current location is null. Using defaults.");
                    LogPrintExcepWithThrow(TAG, "Exception: %s", task.getException());
                }
            }
        });
    }

    private void secondary() {

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Toast.makeText(RideCreation.this, "oma", Toast.LENGTH_SHORT).show();
                //Got the location!
                if (location == null) {
                    backgroundThreadShortToast(RideCreation.this, "Your GPS is not working properly!");


                } else {
                    //Toast.makeText(RideCreation.this, "6", Toast.LENGTH_SHORT).show();
                    location.getLongitude();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(RideCreation.this, locationResult);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            loadMylocation(); // we don't want user location onResume every time as, if gps provided from before, than map may not be ready yet with
            // onResume called with the creation of the activity. Only when dialog is been shown, that time we want user location,
            // as map will already have been ready
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

  /*  @Override
    public void onLocationChanged(Location location) {
        try {
            LogPrint(TAG2,"1");
            // getting GPS status
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                LogPrint(TAG2,"2");
                if (location != null) {

                    LogPrint(TAG2,"3");
                    if (locationManager != null) {
                        LogPrint(TAG2,"4");
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LogPrint(TAG2,"5");
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000,
                                10, (android.location.LocationListener) this);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            LogPrint(TAG2,"6");
                            Toast.makeText(this, "hosse", Toast.LENGTH_SHORT).show();
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogPrint(TAG2,"7");
            e.printStackTrace();
        }
    }*/


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            LogPrint(TAG3,"ashse");
            // image naming and path  to include sd card  appending name you choose for file
            //String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            String mPath = Environment.getDataDirectory().toString() + "/" + now + ".jpg";
            LogPrint(TAG3,"ashse 2");
            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            Uri filePath;
            filePath=getImageUri(this.getApplicationContext(),bitmap);
            File finalFile = new File(getRealPathFromURI(filePath));
            /*File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);*/
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            LogPrint(TAG3,"ashse catch "+e);
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
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
        if (this.getContentResolver() != null) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    public void captureScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {

            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;

                OutputStream fout = null;

                String filePath = System.currentTimeMillis() + ".jpeg";


                Uri filePath1;
                filePath1=getImageUri(RideCreation.this,bitmap);
                finalFile = new File(getRealPathFromURI(filePath1));


                startLocation=startLocationText.getText().toString();
                endLocation =endLocationText.getText().toString();

                textViewStartTime.setText("Time : "+startTime);
                textViewStartLoc.setText("Location : "+startLocation);
                textViewEndTime.setText("Time : "+endTime);
                textViewEndLoc.setText("Location : "+endLocation);


                String temp=""+distance;
                int index=temp.indexOf('.');
                String s;
                if(distance>0){
                    s=temp.substring(0,index+4);
                }
                else {
                    s=temp;
                }

                totalDistancce =s;

                textViewDistance.setText(s+ " K.M.");


                Animation fadeIn = AnimationUtils.loadAnimation(RideCreation.this, R.anim.fade_in2);
                rideEndDetails.setAnimation(fadeIn);
                rideEndDetails.setVisibility(View.VISIBLE);



                if(mMap != null){
                    mMap.clear();
                    loadMylocation();
                    // add markers from database to the map
                }


            }
        };

        mMap.snapshot(callback);
    }

    public void apiCaller(RequestBody formBody){

        final ProgressDialog progressDialogAPi = new ProgressDialog(this);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();


        String token = getMyPreference(this,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(NEW_RIDE_ENTRY,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,this) {
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
                        if(responseJson.has("errors")){
                            errors=responseJson.getString("errors");
                            LogPrint("wwhat",errors);
                        }







                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    RideCreation.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            progressDialogAPi.dismiss();
                            Toast.makeText(RideCreation.this, message, Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(getIntent());


                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    ApiHelper.backgroundThreadShortToast(RideCreation.this,"Error!");
                }
            }


        });

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
    public void networkAvailable() {
        constraintLayoutRide.setVisibility(View.GONE);
    }

    @Override
    public void networkUnavailable() {
        constraintLayoutRide.setVisibility(View.VISIBLE);
    }
}
