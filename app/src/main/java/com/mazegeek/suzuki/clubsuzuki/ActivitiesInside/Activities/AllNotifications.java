package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.NotificationRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications;
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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_NOTIFICATION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_NOTIFICATIONS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class AllNotifications extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog pdAllNotifications;
    String myResponse,message;
    ImageView backBtn;
    int pageNum=1;

    List<Notifications> globalAarrayListOfNotifications;
    NotificationRecyclerAdapter myCustomAdapter;

    boolean hasMore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recycler_all_noti);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        globalAarrayListOfNotifications = new ArrayList<>();
        myCustomAdapter = new NotificationRecyclerAdapter(this, globalAarrayListOfNotifications,ALL_NOTIFICATION);
        recyclerView.setAdapter(myCustomAdapter);

        loadLatestNotifications2(pageNum);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findLastVisibleItemPosition() > ((pageNum*10)-2)) {

                    if(hasMore){
                        pageNum++;
                        loadLatestNotifications2(pageNum);
                    }

                    Log.d("SCROLL","SCROLLINGDOWN");
                } else {

                    Log.d("SCROLLP","SCROLLINGUP");
                }
            }
        });

    }

    private void loadLatestNotifications2(int pageNum) {

        if(pdAllNotifications==null){
            pdAllNotifications = new ProgressDialog(this);
            pdAllNotifications.setMessage("Loading .. ");
            pdAllNotifications.setCancelable(false);
            pdAllNotifications.setCanceledOnTouchOutside(false);
            pdAllNotifications.show();
        }


        String token = getMyPreference(this,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        RequestBody formBody = new FormBody.Builder()
                .add("page_number", ""+pageNum)
                .add("per_page", "10")
                .build();

        getUrlInstance(ALL_NOTIFICATIONS_API,formBody,"Authorization",token,new CustomCallBack(pdAllNotifications,this) {
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
                    pdAllNotifications.dismiss();
                    backgroundThreadShortToast(AllNotifications.this, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray NotificationArray = null;
                    final List<com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications> arrayListOfNotifications = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        NotificationArray = fileObject.getJSONArray("data");
                        JSONObject paginationObject = new JSONObject(fileObject.getString("pagination"));
                        hasMore =paginationObject.getBoolean("has_more_pages");


                        //pathN=""+pathName;
                        for (int i = 0; i < NotificationArray.length(); i++) {

                            JSONObject NotificationsArrayObject = null;

                            NotificationsArrayObject = NotificationArray.getJSONObject(i);
                            com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications notifications= new com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications();
                            notifications.setTitle(NotificationsArrayObject.getString("title"));
                            notifications.setImage(NotificationsArrayObject.getString("image"));
                            notifications.setDescription(NotificationsArrayObject.getString("description"));
                            notifications.setDate(NotificationsArrayObject.getString("created_at"));






                            arrayListOfNotifications.add(notifications);


                        }


                    } catch (JSONException e) {
                        pdAllNotifications.dismiss();
                        backgroundThreadShortToast(AllNotifications.this, ""+e);
                        e.printStackTrace();
                    }


                    AllNotifications.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdAllNotifications.dismiss();
                            globalAarrayListOfNotifications.addAll(arrayListOfNotifications);
                            myCustomAdapter.notifyDataSetChanged();
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdAllNotifications.dismiss();

                    backgroundThreadShortToast(AllNotifications.this, message);
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
