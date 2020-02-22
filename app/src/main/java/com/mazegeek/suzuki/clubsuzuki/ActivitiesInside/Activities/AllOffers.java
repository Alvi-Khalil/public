package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.OffersRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.OffersFragment;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class AllOffers extends AppCompatActivity {


    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = OffersFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    RecyclerView recyclerView;
    Activity activity;
    ProgressDialog pdSos;
    String myResponse,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        activity=this;
        drawerOpener=findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.sos_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));




/*
        //static start

        List<SoSItems> arrayListOfSoS = new ArrayList<>();
        String [] numbers = new String[]{"999","777", "888"};
        String [] headers = new String[]{"FIRE SERVICE","POLICE", "MEDICAL"};
        Integer [] Images = new Integer[]{R.drawable.fire,R.drawable.alamr,R.drawable.health};


        pdSos.dismiss();
        for(int i=0;i<Images.length;i++){
            SoSItems soSItems= new SoSItems();

            soSItems.setTestImage(Images[i]);
            soSItems.setPhoneNumber(numbers[i]);
            soSItems.setHeader(headers[i]);
            soSItems.setImage(numbers[i]);

            //LogPrint("Kiklo",numbers[i]+" "+headers[i]+" "+Images[i]+"\n");

            arrayListOfSoS.add(soSItems);
        }


        SoSListRecyclerAdapter customAdapter = new SoSListRecyclerAdapter(activity, arrayListOfSoS);
        recyclerView.setAdapter(customAdapter);


        //static end
        */

    }

    private void loadAllOffers() {

        if(pdSos==null){
            pdSos = new ProgressDialog(activity);
            pdSos.setMessage("Loading .. ");
            pdSos.setCancelable(false);
            pdSos.setCanceledOnTouchOutside(false);
            pdSos.show();
        }

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        RequestBody formBody = new FormBody.Builder()
                .add("offer_type_id","1" )
                .build();


        getUrlInstance(ALL_OFFERS,formBody,"Authorization",token,new CustomCallBack(pdSos,activity) {
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
                    JSONArray OfferArray = null;
                    final List<OfferItem> arrayListOfOffer = new ArrayList<>();
                    try {
                        Log.d("collecd666", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        OfferArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < OfferArray.length(); i++) {

                            JSONObject OfferArrayObject = null;

                            OfferArrayObject = OfferArray.getJSONObject(i);
                            OfferItem offerItem= new OfferItem();
                            offerItem.setTitle(OfferArrayObject.getString("title"));
                            offerItem.setSubTitle(OfferArrayObject.getString("sub_title"));
                            offerItem.setDetails(OfferArrayObject.getString("description"));
                            offerItem.setDate(OfferArrayObject.getString("created_at"));
                            offerItem.setType(OfferArrayObject.getString("type"));
                            offerItem.setImage(OfferArrayObject.getString("image"));
                            offerItem.setUrl(OfferArrayObject.getString("url"));






                            arrayListOfOffer.add(offerItem);


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
                            OffersRecyclerAdapter customAdapter = new OffersRecyclerAdapter(activity, arrayListOfOffer);
                            recyclerView.setAdapter(customAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadAllOffers();
    }
}
