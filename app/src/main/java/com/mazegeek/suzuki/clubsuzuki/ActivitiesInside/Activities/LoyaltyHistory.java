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
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.LoyaltyHistoryRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.OffersRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LoyaltyHistoryItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.OffersFragment;
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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_LOYALTY_HISTORY;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class LoyaltyHistory extends AppCompatActivity {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = OffersFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    RecyclerView recyclerView;
    Activity activity;
    ProgressDialog pdSos;
    String myResponse="",message;
    TextView textViewNoHistory;
    boolean moreThanOne=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_history);


        activity=this;
        drawerOpener=findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textViewNoHistory = findViewById(R.id.no_history);

        recyclerView = findViewById(R.id.sos_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));
    }


    private void loadHistory() {

        if(pdSos==null){
            pdSos = new ProgressDialog(activity);
            pdSos.setMessage("Loading .. ");
            pdSos.setCancelable(false);
            pdSos.setCanceledOnTouchOutside(false);
            pdSos.show();
        }

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);



        getUrlInstance(USER_LOYALTY_HISTORY,"Authorization",token,new CustomCallBack(pdSos,activity) {
            @Override
            public void sendResponse(Response response) {
                try {
                    if (response.body() != null) {
                        myResponse = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject jsonObject = new JSONObject(myResponse);
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    pdSos.dismiss();
                    LogPrint("hjjhfrf",""+e+"  response : "+myResponse);
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray LoyaltyHistoryArray = null;
                    final List<LoyaltyHistoryItem> arrayListOfLoyaltyHistory = new ArrayList<>();
                    try {
                        LogPrint("loyalty_history", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        LoyaltyHistoryArray = fileObject.getJSONArray("data");

                        if(LoyaltyHistoryArray.length()>0){
                            moreThanOne=true;
                            for (int i = 0; i < LoyaltyHistoryArray.length(); i++) {

                                JSONObject LoyaltyHistoryArrayObject = null;

                                LoyaltyHistoryArrayObject = LoyaltyHistoryArray.getJSONObject(i);
                                LoyaltyHistoryItem loyaltyHistoryItem= new LoyaltyHistoryItem();
                                loyaltyHistoryItem.setOfferName(LoyaltyHistoryArrayObject.getString("offer_title"));
                                loyaltyHistoryItem.setOfferCode(LoyaltyHistoryArrayObject.getString("offer_code"));
                                loyaltyHistoryItem.setShopName(LoyaltyHistoryArrayObject.getString("shop_name"));
                                loyaltyHistoryItem.setSalesPhone(LoyaltyHistoryArrayObject.getString("shop_contact"));
                                loyaltyHistoryItem.setUsageTime(LoyaltyHistoryArrayObject.getString("created_at"));
                                loyaltyHistoryItem.setUserQuota(LoyaltyHistoryArrayObject.getString("total Quota"));
                                loyaltyHistoryItem.setUsageNumber(LoyaltyHistoryArrayObject.getString("count"));






                                arrayListOfLoyaltyHistory.add(loyaltyHistoryItem);


                            }
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
                            if(moreThanOne){
                                textViewNoHistory.setVisibility(View.GONE);
                            }

                            LoyaltyHistoryRecyclerAdapter loyaltyHistoryRecyclerAdapter = new LoyaltyHistoryRecyclerAdapter(activity, arrayListOfLoyaltyHistory);
                            recyclerView.setAdapter(loyaltyHistoryRecyclerAdapter);
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
        loadHistory();
    }


}
