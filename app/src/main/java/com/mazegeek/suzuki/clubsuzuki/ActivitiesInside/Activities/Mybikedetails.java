package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.ProgressDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.BikeDetailsPartsRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MySpecificationAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableChild;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableParent;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.PartsClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Variant;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE_DETAILS_WITH_ID;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_JSON;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;

public class Mybikedetails extends AppCompatActivity {

    RecyclerView recyclerView,recyclerViewEngine,recyclerViewBody,recyclerViewAccess;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    String myResponse;
    List<ExpandableParent> arrayListofExpandableParent;
    List<Variant> arrayListofVaiants;
    List<PartsClass> arrayListofEngine,arrayListofBody,arrayListofAccessories;
    ImageView variantImage,back;
    TextView nameTxt,italicTxt,priceTxt,overviewtxt;
    String name,price,overview,italic,image;
    boolean engineVisible=false,bodyVisible=false,acessVisible=false;
    ConstraintLayout constraintLayoutEngin,constraintLayoutBody,constraintLayoutAccessories;
    TextView bottomTxtEngin,bottomTxtBody,bottomTxtAccess;
    View bottomViewEngin,bottomViewBody,bottomViewAccess;
    String extraJson;

    ConstraintLayout constraintLayoutPartsCons;
    CardView cardViewrecycles_of_parts;



    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MySpecificationAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybikedetails);
        arrayListofExpandableParent = new ArrayList<>();
        arrayListofVaiants = new ArrayList<>();
        arrayListofEngine = new ArrayList<>();
        arrayListofBody = new ArrayList<>();
        arrayListofAccessories = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bundle extras = getIntent().getExtras();
        extraJson = extras.getString(EXTRA_JSON);




        progressBar = findViewById(R.id.progressbar_bike_details);
        progressBar.setVisibility(View.GONE);
        variantImage = findViewById(R.id.image_variant);
        back = findViewById(R.id.back);
        nameTxt = findViewById(R.id.name);
        italicTxt = findViewById(R.id.italic_text);
        priceTxt = findViewById(R.id.price);
        overviewtxt = findViewById(R.id.over_view);
        constraintLayoutPartsCons = findViewById(R.id.parts_con);
        cardViewrecycles_of_parts = findViewById(R.id.recycles_of_parts);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mybikedetails.super.onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.specifications_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        LinearLayoutManager layoutManagerHorizontal2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewEngine = findViewById(R.id.engin_recycle);
        recyclerViewEngine.setLayoutManager(layoutManagerHorizontal2);

        LinearLayoutManager layoutManagerHorizontal3 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBody = findViewById(R.id.body_recycle);
        recyclerViewBody.setLayoutManager(layoutManagerHorizontal3);

        LinearLayoutManager layoutManagerHorizontal4 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewAccess = findViewById(R.id.access_recycle);
        recyclerViewAccess.setLayoutManager(layoutManagerHorizontal4);



        constraintLayoutEngin = findViewById(R.id.engine);
        bottomTxtEngin = findViewById(R.id.engin_bottom_text);
        bottomViewEngin = findViewById(R.id.engin_bottom_view);

        constraintLayoutBody = findViewById(R.id.body);
        bottomTxtBody = findViewById(R.id.body_bottom_text);
        bottomViewBody = findViewById(R.id.body_bottom_view);

        constraintLayoutAccessories = findViewById(R.id.accessories);
        bottomTxtAccess = findViewById(R.id.access_bottom_text);
        bottomViewAccess = findViewById(R.id.access_bottom_view);

        constraintLayoutEngin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleTxt(bottomTxtEngin,bottomViewEngin,recyclerViewEngine);
                setInvisibleTxt(bottomTxtAccess,bottomViewAccess,recyclerViewAccess);
                setInvisibleTxt(bottomTxtBody,bottomViewBody,recyclerViewBody);

            }
        });

        constraintLayoutBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInvisibleTxt(bottomTxtEngin,bottomViewEngin,recyclerViewEngine);
                setInvisibleTxt(bottomTxtAccess,bottomViewAccess,recyclerViewAccess);
                setVisibleTxt(bottomTxtBody,bottomViewBody,recyclerViewBody);

            }
        });

        constraintLayoutAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInvisibleTxt(bottomTxtEngin,bottomViewEngin,recyclerViewEngine);
                setVisibleTxt(bottomTxtAccess,bottomViewAccess,recyclerViewAccess);
                setInvisibleTxt(bottomTxtBody,bottomViewBody,recyclerViewBody);

            }
        });

        //String s="{\"success\":true,\"message\":\"Bike Details Data\",\"data\":{\"title\":\"Gixxer SF\",\"sub_title\":\"SUZUKI'S LEGENDARY RACING HERITAGE\",\"description\":\"The premium looking Suzuki Gixxer SF is a fully faired 155cc motorcycle which has been developed in the same wind-tunnel where the legendary Hayabusa, GSX-R and MotoGP machines are developed.\",\"category\":\"Sports\",\"bike_image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_models\\/variant_color_images\\/variant_color_image__154331923929253.png\",\"price\":\"249,950\",\"specifications\":{\"BRAKE\":[{\"label\":\"Rear\",\"value\":\"Single Disc\"},{\"label\":\"Front\",\"value\":\"Drum\"}],\"ENGINE\":[{\"label\":\"Bore x Stroke(mm)\",\"value\":\"56.0 x 62.9\"},{\"label\":\"Fuel System\",\"value\":\"Carburetor\"},{\"label\":\"Valve System\",\"value\":\"SOHC, 2 Valve\"},{\"label\":\"Engine Output\",\"value\":\"14.8ps@8,000rpm\"},{\"label\":\"Torque\",\"value\":\"14nm@6,000rpm\"},{\"label\":\"Starter System\",\"value\":\"Electric\"},{\"label\":\"Type\",\"value\":\"4-Stroke,1-Cylinder, Air cooled\"},{\"label\":\"Displacement(cm3)\",\"value\":\"154.9\"}],\"DIMENSION\":[{\"label\":\"Seat Height (mm)\",\"value\":\"780\"},{\"label\":\"Wheels\",\"value\":\"Cast\"},{\"label\":\"Ground Clearance (mm)\",\"value\":\"160\"},{\"label\":\"Curb Mass(kg)\",\"value\":\"139\"},{\"label\":\"Fuel Tank Capacity (l)\",\"value\":\"12\"},{\"label\":\"Wheelbase (mm)\",\"value\":\"1,330\"},{\"label\":\"Overall Width (mm)\",\"value\":\"785\"},{\"label\":\"Overall Height (mm)\",\"value\":\"1,085\"},{\"label\":\"Overall Length (mm)\",\"value\":\"2,050\"}],\"TYRE SIZE\":[{\"label\":\"Rear\",\"value\":\"140\\/60R-17-Tubeless\"},{\"label\":\"Front\",\"value\":\"100\\/80-17-Tubeless\"}],\"ELECTRICAL\":[{\"label\":\"Battery\",\"value\":\"Maintenance free 12V, 3Ah\"},{\"label\":\"Headlight\",\"value\":\"12V 35\\/35W (Halogen)\"},{\"label\":\"Tail Light\",\"value\":\"LED\"},{\"label\":\"Speedometer\",\"value\":\"Digital\"}],\"SUSPENTION\":[{\"label\":\"Rear\",\"value\":\"Swing Arm, Mono Suspension\"},{\"label\":\"Front\",\"value\":\"Telescopic\"}],\"TRANSMISSION\":[{\"label\":\"Transmission\",\"value\":\"5 speed, MT\"}]},\"parts\":{\"Engine\":[{\"title\":\"Engine Parts 01\",\"description\":\"155 GSX Engine\",\"price\":80000,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097937415623.jpg\"},{\"title\":\"Carburator\",\"description\":\"Heavy duty Carburator\",\"price\":25000,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097939420732.jpg\"},{\"title\":\"Engine Parts 01\",\"description\":\"155 GSX Engine\",\"price\":40000,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097948533548.jpg\"},{\"title\":\"Engine Parts 02\",\"description\":\"155 GSX Engine, heavy duty.\",\"price\":90000,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097953387883.jpg\"}],\"Body Parts\":[{\"title\":\"Body parts 01\",\"description\":\"155 GSX Engine, Heavy duty Carburator\",\"price\":7000,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097964924199.jpg\"},{\"title\":\"Body parts 02\",\"description\":\"Heavy duty breakpad\",\"price\":800,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097968587535.png\"},{\"title\":\"Body parts 03\",\"description\":\"Heavy duty Seat.\",\"price\":300,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097972429419.jpg\"},{\"title\":\"Body parts 04\",\"description\":\"Heavy duty Rim\",\"price\":800,\"image\":\"https:\\/\\/suzuki.mazegeek.com\\/images\\/bike_parts\\/bike_part_image__154097976535101.jpg\"}]}}}\n";
        setValues(extraJson);

    }

    private void setInvisibleTxt(TextView bottomTxt, View bottomView,RecyclerView recyclerView) {
        bottomView.setVisibility(View.GONE);
        bottomTxt.setTextColor(ContextCompat.getColor(this, R.color.colorAccent2));
        recyclerView.setVisibility(View.GONE);
    }

    private void setVisibleTxt(TextView bottomTxt, View bottomView,RecyclerView recyclerView) {
        bottomView.setVisibility(View.VISIBLE);
        bottomTxt.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        recyclerView.setVisibility(View.VISIBLE);
    }


    public void loadSpecifications(JSONObject specifications){

        Iterator<String> iter = specifications.keys();





        while (iter.hasNext()) {
            ExpandableParent expandableParent= new ExpandableParent();
            String key = iter.next();
            String Formattedkey = formatUpper(key);
            expandableParent.setTitle(Formattedkey);
            List<ExpandableChild> arrayListofExpandableChild = new ArrayList<>();
            //LogPrint("lloo",key);
            try {
                JSONArray valuesArray = specifications.getJSONArray(key);


                for (int i = 0; i < valuesArray.length(); i++) {



                    JSONObject specArrayObject = null;
                    try {

                        specArrayObject = valuesArray.getJSONObject(i);


                        ExpandableChild singleChild = new ExpandableChild();
                        singleChild.setName(specArrayObject.getString("label"));
                        singleChild.setValue(specArrayObject.getString("value"));



                        arrayListofExpandableChild.add(singleChild);

                    } catch (JSONException e) {

                        backgroundThreadShortToast(this, "Server Error!");
                        e.printStackTrace();
                    }


                }

                expandableParent.setChildObjectList(arrayListofExpandableChild);
                arrayListofExpandableParent.add(expandableParent);



            } catch (JSONException e) {
                // Something went wrong!
            }
        }


        //parentObject.addAll(arrayListofExpandableParent);



    }

    private String formatUpper(String key) {
        String  s =key.toUpperCase().replace('_', ' ');
        return s;
    }


    public void setValues(String bikeModel){
        JSONObject fileObject = null;
        JSONObject dataObject = null;
        JSONObject specificationsObject = null;
        JSONObject partsObject = null;





        try {
            fileObject = new JSONObject(bikeModel);

            dataObject = new JSONObject(fileObject.getString("data"));
            specificationsObject = new JSONObject(dataObject.getString("specifications"));
            loadSpecifications(specificationsObject);
            partsObject = new JSONObject(dataObject.getString("parts"));
            loadParts(partsObject);


            italic =formatUpper(dataObject.getString("sub_title"));
            name=dataObject.getString("title");
            price ="\u09F3 "+dataObject.getString("price");
            overview = dataObject.getString("description");
            image = dataObject.getString("bike_image");




        } catch (JSONException e) {
            progressDialog.dismiss();
            backgroundThreadShortToast(Mybikedetails.this, ""+e);
            e.printStackTrace();
        }


/*
        Mybikedetails.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {*/

                progressDialog.dismiss();

                MySpecificationAdapter mySpecificationAdapter = new MySpecificationAdapter(Mybikedetails.this,arrayListofExpandableParent);
                //mySpecificationAdapter.setParentList(arrayListofExpandableParent,true);
                //mySpecificationAdapter.setHasStableIds(true);

                recyclerView.setAdapter(mySpecificationAdapter);



                nameTxt.setText(name);
                italicTxt.setText(italic);
                priceTxt.setText(price);
                overviewtxt.setText(overview);
                gluideLoader(Mybikedetails.this,image,variantImage);



                if(engineVisible){
                    BikeDetailsPartsRecyclerAdapter bikeDetailsPartsRecyclerAdapter = new BikeDetailsPartsRecyclerAdapter(Mybikedetails.this,arrayListofEngine);
                    recyclerViewEngine.setAdapter(bikeDetailsPartsRecyclerAdapter);
                }
                else{
                    constraintLayoutEngin.setVisibility(View.GONE);
                }

                if(bodyVisible){
                    BikeDetailsPartsRecyclerAdapter bikeDetailsPartsRecyclerAdapter2 = new BikeDetailsPartsRecyclerAdapter(Mybikedetails.this,arrayListofBody);
                    recyclerViewBody.setAdapter(bikeDetailsPartsRecyclerAdapter2);
                }
                else{
                    constraintLayoutBody.setVisibility(View.GONE);
                }

                if(acessVisible){
                    BikeDetailsPartsRecyclerAdapter bikeDetailsPartsRecyclerAdapter3 = new BikeDetailsPartsRecyclerAdapter(Mybikedetails.this,arrayListofAccessories);
                    recyclerViewAccess.setAdapter(bikeDetailsPartsRecyclerAdapter3);
                }
                else{
                    constraintLayoutAccessories.setVisibility(View.GONE);
                }

        if(!engineVisible && !acessVisible && !bodyVisible){
            constraintLayoutPartsCons.setVisibility(View.GONE);
            cardViewrecycles_of_parts.setVisibility(View.GONE);
        }


       /*     }
        });*/

    }


    private void loadParts(JSONObject partsObject) {


        JSONArray bodyArray =null;
        JSONArray accessArray =null;
        JSONArray engineArray =null;




        if (partsObject.has("Body Parts")) {
            try {
                bodyVisible=true;
                bodyArray=partsObject.getJSONArray("Body Parts");
                for (int i = 0; i < bodyArray.length(); i++) {

                    JSONObject bodyArrayObject = null;

                    bodyArrayObject = bodyArray.getJSONObject(i);

                    PartsClass singleBody = new PartsClass();
                    singleBody.setName(bodyArrayObject.getString("title"));
                    singleBody.setDescription(bodyArrayObject.getString("description"));
                    singleBody.setImage(bodyArrayObject.getString("image"));
                    singleBody.setPrice(bodyArrayObject.getString("price"));

                    arrayListofBody.add(singleBody);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (partsObject.has("Accessories")) {
            try {
                acessVisible=true;
                accessArray=partsObject.getJSONArray("Accessories");
                for (int i = 0; i < accessArray.length(); i++) {

                    JSONObject accessArrayObject = null;

                    accessArrayObject = accessArray.getJSONObject(i);

                    PartsClass singleAccess = new PartsClass();
                    singleAccess.setName(accessArrayObject.getString("title"));
                    singleAccess.setDescription(accessArrayObject.getString("description"));
                    singleAccess.setImage(accessArrayObject.getString("image"));
                    singleAccess.setPrice(accessArrayObject.getString("price"));

                    arrayListofAccessories.add(singleAccess);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (partsObject.has("Engine")) {
            try {
                engineVisible=true;
                engineArray=partsObject.getJSONArray("Engine");
                for (int i = 0; i < engineArray.length(); i++) {

                    JSONObject enginArrayObject = null;

                    enginArrayObject = engineArray.getJSONObject(i);

                    PartsClass singleEngin = new PartsClass();
                    singleEngin.setName(enginArrayObject.getString("title"));
                    singleEngin.setDescription(enginArrayObject.getString("description"));
                    singleEngin.setImage(enginArrayObject.getString("image"));
                    singleEngin.setPrice(enginArrayObject.getString("price"));

                    arrayListofEngine.add(singleEngin);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    private void loadVariants(JSONArray variantArray) {

        for(int i=0;i<variantArray.length();i++){

            JSONObject variantArrayObject = null;
            try {

                variantArrayObject = variantArray.getJSONObject(i);


                Variant singleVariant = new Variant();
                singleVariant.setColorName(variantArrayObject.getString("color_name"));
                singleVariant.setColorCode(variantArrayObject.getString("color_code"));
                singleVariant.setImageUrl(variantArrayObject.getString("image"));



                arrayListofVaiants.add(singleVariant);

            } catch (JSONException e) {
                progressBar.setVisibility(View.GONE);
                backgroundThreadShortToast(this, "Server Error!");
                e.printStackTrace();
            }

        }
    }

}
