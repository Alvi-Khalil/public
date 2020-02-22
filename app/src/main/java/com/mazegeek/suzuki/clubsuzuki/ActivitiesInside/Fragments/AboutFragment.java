/*
 * Created by Alvi Khalil on 10/24/18 3:24 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/24/18 3:24 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.Survey;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Options;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Questions;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PARCEL;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SURVEY_QUESTIONS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;


public class AboutFragment extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = AboutFragment.class.getSimpleName();

    private static final String TAG = "check";
    Activity activity;
    ImageView drawerOpener;

    ProgressDialog progressDialogAPi;

    String myResponse,message,stringTitle,stringDescription,stringWelcomeText;

    ConstraintLayout constraintLayoutWelcome;
    TextView textViewSurveyTitle,textViewSurveyDescription,textViewWelcome;
    Button startSurvey;

    List<Questions> arrayListOfQuestions;

    boolean createdBefore=false;


    TextView textViewAboutUs,textViewWarranty,textViewLegal,textViewTerms,textViewContact;
    ImageView fb,twit,insta,youtube;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        arrayListOfQuestions = new ArrayList<>();
        constraintLayoutWelcome=view.findViewById(R.id.welcome);
        textViewSurveyTitle=view.findViewById(R.id.survey_title);
        textViewSurveyDescription=view.findViewById(R.id.survey_description);
        textViewWelcome=view.findViewById(R.id.survey_welcome);
        startSurvey=view.findViewById(R.id.start_survey);


        textViewAboutUs=view.findViewById(R.id.about_us);
        textViewWarranty=view.findViewById(R.id.warranty_policy);
        textViewLegal=view.findViewById(R.id.legal_policy);
        textViewTerms=view.findViewById(R.id.terms);
        textViewContact=view.findViewById(R.id.contact);


        fb=view.findViewById(R.id.fb);
        twit=view.findViewById(R.id.twit);
        insta=view.findViewById(R.id.insta);
        youtube=view.findViewById(R.id.youtube);

      /*  progressDialogAPi = new ProgressDialog(activity);
        progressDialogAPi.setMessage("Please wait .. ");
        progressDialogAPi.setCanceledOnTouchOutside(false);
        progressDialogAPi.setCancelable(false);
        progressDialogAPi.show();*/



        drawerOpener=view.findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });

        //loadQuestions();
        textViewAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.suzuki.com.bd/about-us";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textViewWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.suzuki.com.bd/warranty-policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textViewLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.suzuki.com.bd/legal-page";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.suzuki.com.bd/terms-condition";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.suzuki.com.bd/contact-us";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/suzukibd/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/suzukibd";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/suzuki.bd/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/channel/UCkjU67Cy-W7r3nhRm1AKHMA";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createdBefore=true;
                Intent intent= new Intent(activity,Survey.class);
                intent.putParcelableArrayListExtra(PARCEL, (ArrayList<? extends Parcelable>) arrayListOfQuestions);
                //intent.putExtra(REDEEM_TYPE, (Parcelable) arrayListOfQuestions);
                startActivity(intent);

                /*Animation animation = AnimationUtils.loadAnimation(activity, R.anim.outer_right_to_left);
                con1.setAnimation(animation);
                Animation animation2 = AnimationUtils.loadAnimation(activity, R.anim.inner_right_to_left);
                constraintLayoutWelcome.setAnimation(animation2);

                con1.setVisibility(View.VISIBLE);
                constraintLayoutWelcome.setVisibility(View.GONE);
*/

            }
        });







        return view;
    }

    private void loadQuestions() {


        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(SURVEY_QUESTIONS,"Authorization",token, new CustomCallBack(progressDialogAPi,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse = response.body().string();
                    LogPrint("balshoos",myResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject responseJson=null;
                JSONArray questionsArray=null;

                if (response.isSuccessful()) {
                    try {
                        responseJson = new JSONObject(myResponse);
                        LogPrint("zihanbhai",myResponse);

                        message = responseJson.getString("message");
                        Boolean success=responseJson.getBoolean("success");

                        if(success){
                            stringTitle=responseJson.getString("title");
                            stringDescription=responseJson.getString("description");
                            stringWelcomeText=responseJson.getString("welcome_text");
                            questionsArray = responseJson.getJSONArray("questions");

                            for (int i = 0; i < questionsArray.length(); i++) {

                                JSONObject questionsArrayObject = null;

                                questionsArrayObject = questionsArray.getJSONObject(i);

                                Questions questions= new Questions();

                                questions.setQuestionId(questionsArrayObject.getString("id"));
                                questions.setSurveyId(questionsArrayObject.getString("survey_id"));
                                questions.setQuestion(questionsArrayObject.getString("question_title"));
                                questions.setQuestionType(questionsArrayObject.getString("question_type"));

                                List<Options> optionsList = GetOptionsArrayList(questionsArrayObject.getJSONArray("options"));
                                questions.setOptions(optionsList);



                                arrayListOfQuestions.add(questions);
                        }



                        }else{
                            stringTitle="No Survey Available!";
                            stringDescription="At the moment, there is no active surveys! Thank you for your query. Do check from time to time to participate in surveys, as surveys help you earn loyalty points!";
                            stringWelcomeText="";
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                            textViewSurveyTitle.setText(stringTitle);
                            textViewSurveyDescription.setText(stringDescription);
                            textViewWelcome.setText(stringWelcomeText);
                            if(stringWelcomeText.equals("")){
                                startSurvey.setVisibility(View.GONE);
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


    private List<Options> GetOptionsArrayList(JSONArray optionsArray) {
        final List<Options> arrayListOfOptions = new ArrayList<>();
        for (int i = 0; i < optionsArray.length(); i++) {

            JSONObject optionArrayObject = null;

            try {
                optionArrayObject = optionsArray.getJSONObject(i);
                Options options= new Options();

                options.setOptionText(optionArrayObject.getString("body"));
                options.setOptionId(optionArrayObject.getString("sequence"));
                options.setSelected(false);


                arrayListOfOptions.add(options);

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
        return arrayListOfOptions;
    }

    @Override
    public void onResume() {
        if(createdBefore){
            arrayListOfQuestions = new ArrayList<>();
            loadQuestions();
        }
        super.onResume();
    }
}
