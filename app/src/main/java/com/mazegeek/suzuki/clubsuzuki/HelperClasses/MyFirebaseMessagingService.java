package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.AllNotifications;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.R;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEW_FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String FCM1="",FCM2="";
    private static final String TAG = "MyFirebaseMsgService";
    FCM_TOKEN_HANDLING fcm_token_handling;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        LogPrint(TAG, "Refreshed token: " + token);
        LogPrint(TAG, "Refreshed token by FirebaseInstanceId: " + FirebaseInstanceId.getInstance().getInstanceId());
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        /*fcm_token_handling=new FCM_TOKEN_HANDLING();
        fcm_token_handling.fcmTokenReplace(this,token);*/
        setMyPreference(this,NEW_FCM_TOKEN,token);
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        LogPrint(TAG, "FROM: " + remoteMessage.getFrom());


        //check if message contains data
        if(remoteMessage.getData().size()>0){
            LogPrint(TAG, "Message Data: " + remoteMessage.getData());
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");
            String image_url;
            if(remoteMessage.getData().get("image").equals("null")){
                image_url="https://suzuki.mazegeek.com/";
            }
            else{
                image_url=remoteMessage.getData().get("image");
            }

            String id=remoteMessage.getData().get("id");
           sendNotificationFromDataMEssage(title,body,image_url,id);
        }

        //check if message contains notification
        if(remoteMessage.getNotification()!=null){
            LogPrint(TAG, "Message Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }


    }


    /**
     * Display the notification
     * @param body
     */
    private void sendNotification(String body) {
        Intent intent = new Intent(this,MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //Set sound for notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vib ={500,500};

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Learning today")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setVibrate(vib)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifiBuilder.build());

    }
    private void sendNotificationFromDataMEssage(String title,String body,String image,String id) {
    /*    Intent intent = new Intent(this,MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);





        //Set sound for notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vib ={500,500};
        long when = System.currentTimeMillis();

        Notification.Builder notifiBuilder = new Notification.Builder(this);
        notifiBuilder.setStyle(new Notification.BigTextStyle(notifiBuilder)
                .bigText(body)
                .setBigContentTitle(title)
                .setSummaryText("Big summary"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setVibrate(vib)
                .setWhen(when)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifiBuilder.build());*/


        new sendNotification(this)
                .execute(title,body,image,id);

    }
    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String title,body,image;
        int id;


        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            title = params[0] ;
            body=params[1];
            id=Integer.parseInt(params[3]);

            try {

                URL url = null;
                try {
                    url = new URL(params[2]);
                    image=params[2];
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    try {
                        connection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    in = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(in);
                    return myBitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }






            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                Intent intent = new Intent(MyFirebaseMessagingService.this,MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent,PendingIntent.FLAG_ONE_SHOT);





                //Set sound for notification
                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                long[] vib ={500,500};
                long when = System.currentTimeMillis();



                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder notifiBuilder;

                if(Build.VERSION.SDK_INT >= 26){
                    NotificationChannel channel = new NotificationChannel("default",
                            "Channel name",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Channel description");
                    notificationManager.createNotificationChannel(channel);
                    notifiBuilder = new Notification.Builder(MyFirebaseMessagingService.this,"default");
                }
                else{
                    notifiBuilder = new Notification.Builder(MyFirebaseMessagingService.this);
                }



               /* notifiBuilder.setStyle(new Notification.BigTextStyle(notifiBuilder)
                        .bigText(body)
                        .setBigContentTitle(title)
                        .setSummaryText("Big summary"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(notificationSound)
                        .setVibrate(vib)
                        .setWhen(when)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(result).build();*/



                if(body.length()<=70 && !image.equals("https://suzuki.mazegeek.com/")){
                   Notification.BigPictureStyle style = new Notification.BigPictureStyle()
                           .bigPicture(result)
                           .setSummaryText(body);

                   notifiBuilder.setStyle(style)
                           .setSmallIcon(R.drawable.s2)
                           .setContentTitle(title)
                           .setContentText(body)
                           .setAutoCancel(true)
                           .setSound(notificationSound)
                           .setVibrate(vib)
                           .setWhen(when)
                           .setContentIntent(pendingIntent).build();
               }
                else{
                    Notification.BigTextStyle style = new Notification.BigTextStyle()
                            .bigText(body)
                            .setBigContentTitle(title)
                            .setSummaryText("Rancon Motor Bikes LTD.");

                    notifiBuilder.setStyle(style)
                            .setSmallIcon(R.drawable.s2)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(notificationSound)
                            .setVibrate(vib)
                            .setWhen(when)
                            .setContentIntent(pendingIntent).build();
                }


                id=id%100;

                notificationManager.notify(id,notifiBuilder.build());






            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

