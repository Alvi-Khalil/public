package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Options;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Questions;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.AboutFragment;
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

public class SurveyStart extends AppCompatActivity {


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

    //boolean createdBefore=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_start);


        activity=this;

        arrayListOfQuestions = new ArrayList<>();
        constraintLayoutWelcome=findViewById(R.id.welcome);
        textViewSurveyTitle=findViewById(R.id.survey_title);
        textViewSurveyDescription=findViewById(R.id.survey_description);
        textViewWelcome=findViewById(R.id.survey_welcome);
        startSurvey=findViewById(R.id.start_survey);





        drawerOpener=findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //createdBefore=true;
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






    }

    private void loadQuestions() {
        if(progressDialogAPi==null){
            progressDialogAPi = new ProgressDialog(activity);
            progressDialogAPi.setMessage("Please wait .. ");
            progressDialogAPi.setCanceledOnTouchOutside(false);
            progressDialogAPi.setCancelable(false);
            progressDialogAPi.show();
        }


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

                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

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
        super.onResume();
            arrayListOfQuestions = new ArrayList<>();
            loadQuestions();


    }
}
