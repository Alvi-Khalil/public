package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LogPrinter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.SignIn;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MultipleListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Options;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.QuesAndAns;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Questions;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.OptionSelectionOfSurvey;
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

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LOGIN_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PARCEL;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SURVEY_ANSWER_POSTING;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SURVEY_QUESTIONS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class Survey extends AppCompatActivity implements OptionSelectionOfSurvey {

    RecyclerView recyclerViewMultiple;
    ConstraintLayout con1,con2;
    List<Questions> arrayListOfQuestions;
    List<QuesAndAns> arrayListOfQuesAndAns;
    List<String> selectedOptionsIDs;
    String selectedOptionsIDsString="";
    TextView buttonContinue,buttonBack,questionNameMultiple,questionNameText;
    TextView textViewQuesNo1,textViewQuesNo2;
    EditText editTextAnswer;

    private int questionNumber,currentQustion=0;
    private String currentQuestionType,currentQustionId,currentSingleSelected="";

    Button buttonDone;

    public static final String TEXT="TEXT";
    public static final String SINGLE_CHOICE="SINGLE_CHOICE";
    public static final String MULTIPLE_CHOICE="MULTI_CHOICE";
    private String surveyID ="";
    private String myResponse,message;
    String json;

    ProgressDialog progressDialogAPi;
    boolean pressed =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);


        arrayListOfQuesAndAns=new ArrayList<>();
        Intent intent=getIntent();

        arrayListOfQuestions=intent.getParcelableArrayListExtra(PARCEL);

        questionNumber=arrayListOfQuestions.size();




        buttonContinue = findViewById(R.id.btn_continue);
        buttonBack = findViewById(R.id.btn_back);
        con2 = findViewById(R.id.one_text_input);
        con1 = findViewById(R.id.multiple_choice);
        questionNameMultiple = findViewById(R.id.question);
        questionNameText = findViewById(R.id.question2);
        textViewQuesNo1 = findViewById(R.id.ques_name);
        textViewQuesNo2 = findViewById(R.id.ques_name2);
        buttonDone = findViewById(R.id.done);
        editTextAnswer = findViewById(R.id.edit);

        recyclerViewMultiple = findViewById(R.id.recycle_multiple);
        recyclerViewMultiple.setLayoutManager(new GridLayoutManager(this, 1));






        getCurrentQuestion();

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if((!editTextAnswer.getText().toString().equals("")&&!editTextAnswer.getText().toString().trim().isEmpty())||!currentSingleSelected.equals("")||!selectedOptionsIDsString.equals(""))
                {
                    addingAnswers();
                    editTextAnswer.setText("");
                    currentSingleSelected="";
                    selectedOptionsIDsString="";
                    if(currentQustion<questionNumber-1){

                        currentQustion++;
                        getCurrentQuestion();
                    }
                    else{


                        json = MyJsonBuilderFromArrayList(arrayListOfQuesAndAns);
                        LogPrint("Dekjajasse",json);
                        pressed=true;
                        SurveyAnswerPosting(json);
                        // post answers to zihan api

                    }

                }
                else{
                    Toast.makeText(Survey.this, "Give Opinion to proceed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(currentQustion>0){
                    editTextAnswer.setText("");
                    currentSingleSelected="";
                    selectedOptionsIDsString="";
                    currentQustion--;
                    removingAnswer();
                    getCurrentQuestion();
                }
                else{
                    Survey.super.onBackPressed();
                }
            }
        });


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });


    }

    private String MyJsonBuilderFromArrayList(List<QuesAndAns> arrayListOfQuesAndAns) {
        String finalString="";
        try {
            JSONObject jsonObject= new JSONObject();
            JSONArray jsonArray=new JSONArray();
            jsonObject.put("survey_id",surveyID);

            for(int i=0;i<arrayListOfQuesAndAns.size();i++){
                QuesAndAns quesAndAns = arrayListOfQuesAndAns.get(i);
                JSONObject item = new JSONObject();
                item.put("question_id", quesAndAns.getQuesId());
                item.put("answer", quesAndAns.getAnswer());

                jsonArray.put(item);
            }

            jsonObject.put("answers",jsonArray);
            finalString=jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalString;

    }

    private void removingAnswer() {
        arrayListOfQuesAndAns.remove(currentQustion);

        LogPrint("Dekjajasse","Removed\n");
        for(int i=0;i<arrayListOfQuesAndAns.size();i++){
            QuesAndAns quesAndAns2 = arrayListOfQuesAndAns.get(i);
            LogPrint("Dekjajasse","QuesId = "+quesAndAns2.getQuesId()+" Answer = "+ quesAndAns2.getAnswer());
        }
        LogPrint("Dekjajasse","\n \n \n");
    }

    private void addingAnswers() {
        QuesAndAns quesAndAns = new QuesAndAns();
        quesAndAns.setQuesId(currentQustionId);

        if(currentQuestionType.equals(TEXT)){
            quesAndAns.setAnswer(editTextAnswer.getText().toString());
        }
        else if(currentQuestionType.equals(SINGLE_CHOICE)){

            quesAndAns.setAnswer(currentSingleSelected);
        }
        else if(currentQuestionType.equals(MULTIPLE_CHOICE)){

            quesAndAns.setAnswer(selectedOptionsIDsString);
        }

        arrayListOfQuesAndAns.add(quesAndAns);

        LogPrint("Dekjajasse","Added\n");
        for(int i=0;i<arrayListOfQuesAndAns.size();i++){
            QuesAndAns quesAndAns2 = arrayListOfQuesAndAns.get(i);
            LogPrint("Dekjajasse","QuesId = "+quesAndAns2.getQuesId()+" Answer = "+ quesAndAns2.getAnswer());
        }
        LogPrint("Dekjajasse","\n \n \n");
    }



    private void getCurrentQuestion() {

        if(currentQustion==(questionNumber-1)){
            buttonContinue.setText("Complete");
        }
        Questions questions=arrayListOfQuestions.get(currentQustion);
        currentQuestionType=questions.getQuestionType();
        currentQustionId=questions.getQuestionId();

        surveyID=questions.getSurveyId();

        if(currentQuestionType.equals(TEXT)){
            String s="Question "+(currentQustion+1);
            textViewQuesNo2.setText(s);
            con2.setVisibility(View.VISIBLE);
            con1.setVisibility(View.GONE);

            questionNameText.setText(questions.getQuestion());


        }
        else if(currentQuestionType.equals(SINGLE_CHOICE)){
            String s="Question "+(currentQustion+1);
            textViewQuesNo1.setText(s);
            con1.setVisibility(View.VISIBLE);
            con2.setVisibility(View.GONE);


            List<Options> arrayListOfOptions = questions.getOptions();
            questionNameMultiple.setText(questions.getQuestion()+" ( Select Single Option )");

            MultipleListRecyclerAdapter customAdapter = new MultipleListRecyclerAdapter(this, arrayListOfOptions,SINGLE_CHOICE,this);
            recyclerViewMultiple.setAdapter(customAdapter);
        }
        else if(currentQuestionType.equals(MULTIPLE_CHOICE)){

            String s="Question "+(currentQustion+1);
            textViewQuesNo1.setText(s);
            con1.setVisibility(View.VISIBLE);
            con2.setVisibility(View.GONE);


            List<Options> arrayListOfOptions = questions.getOptions();
            questionNameMultiple.setText(questions.getQuestion()+" ( Select One/More Options )");

            MultipleListRecyclerAdapter customAdapter = new MultipleListRecyclerAdapter(this, arrayListOfOptions,MULTIPLE_CHOICE,this);
            recyclerViewMultiple.setAdapter(customAdapter);
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

    @Override
    public void onBackPressed() {
        if(currentQustion>0){
            editTextAnswer.setText("");
            currentSingleSelected="";
            selectedOptionsIDsString="";
            currentQustion--;
            removingAnswer();
            getCurrentQuestion();
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public void onClickOption(String value) {
        currentSingleSelected=value;
    }

    @Override
    public void onClickMultipleOption(List<Options> arrayListOfOptions) {
        selectedOptionsIDsString="";
        for(int i=0;i<arrayListOfOptions.size();i++){
            if(arrayListOfOptions.get(i).isSelected()){
                String s=arrayListOfOptions.get(i).getOptionId();
                if(selectedOptionsIDsString.equals("")){
                    selectedOptionsIDsString=s;
                }
                else{
                    selectedOptionsIDsString=selectedOptionsIDsString+","+s;
                }

            }
        }
    }

    private void SurveyAnswerPosting(String ans) {

        if(progressDialogAPi==null) {
            progressDialogAPi = new ProgressDialog(this);
            progressDialogAPi.setCancelable(false);
            progressDialogAPi.setCanceledOnTouchOutside(false);
            progressDialogAPi.setMessage("Completing Survey ..");
            progressDialogAPi.show();
        }
        RequestBody formBody = new FormBody.Builder()
                .add("answers", ans)
                .build();

        String token = getMyPreference(this,TOKEN_OF_SEESION);
        LogPrint("setol",token);


        getUrlInstance(SURVEY_ANSWER_POSTING,formBody,"Authorization",token, new CustomCallBack(progressDialogAPi,this) {
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
                        Boolean success=responseJson.getBoolean("success");






                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                    Survey.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogAPi.dismiss();

                            Toast.makeText(Survey.this, "Survey Completed!", Toast.LENGTH_SHORT).show();
                            Survey.super.onBackPressed();


                        }
                    });

                } else {
                    progressDialogAPi.dismiss();
                    backgroundThreadShortToast(Survey.this,"Error!");
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pressed){
            SurveyAnswerPosting(json);
        }
    }
}
