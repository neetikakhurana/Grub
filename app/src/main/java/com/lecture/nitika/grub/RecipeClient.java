package com.lecture.nitika.grub;

import android.content.Context;
import android.util.Log;

import com.mashape.p.spoonacularrecipefoodnutritionv1.Configuration;
import com.mashape.p.spoonacularrecipefoodnutritionv1.SpoonacularAPIClient;
import com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.APIController;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import com.mashape.p.spoonacularrecipefoodnutritionv1.models.DynamicResponse;


import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by tchet on 4/18/2017.
 */

public class RecipeClient {

    private SpoonacularAPIClient client = null;
    private APIController controller = null;
    private static Context mcontext = null;
    private static RecipeClient rClient = null;
    private static String TAG = "RecipeClient";

    public static RecipeClient getInstance(Context context){
        mcontext = context;
        if(rClient == null){
            rClient = new RecipeClient();
        }
        return rClient;
    }

    public RecipeClient(){

        Configuration.initialize(mcontext);
        Configuration.setXMashapeKey(Constants.xMashapeKey);
        client = new SpoonacularAPIClient();
        controller = client.getClient();
    }

    public void SearchRecipe(){
        Map<String,Object> queryParameters = new LinkedHashMap<>();
        controller.searchRecipesAsync("", null, null, null, null, null, 60, null, null, queryParameters, new APICallBack<DynamicResponse>() {
            @Override
            public void onSuccess(HttpContext context, DynamicResponse response) {
                try {
                    Log.d(TAG,"inSuccess "+response.parseAsString());
                } catch (ParseException e) {
                    Log.d(TAG,"inParse Exception "+e.getMessage());
                }
            }

            @Override
            public void onFailure(HttpContext context, Throwable error) {
                Log.d(TAG,"inError");
            }
        });
    }
}
