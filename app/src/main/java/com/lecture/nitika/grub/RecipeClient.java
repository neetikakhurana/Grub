package com.lecture.nitika.grub;

import android.content.Context;
import android.util.Log;

import com.mashape.p.spoonacularrecipefoodnutritionv1.Configuration;
import com.mashape.p.spoonacularrecipefoodnutritionv1.SpoonacularAPIClient;
import com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.APIController;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import com.mashape.p.spoonacularrecipefoodnutritionv1.models.DynamicResponse;
import com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;


import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tchet on 4/18/2017.
 */

public class RecipeClient {

    private SpoonacularAPIClient client = null;
    public APIController controller = null;
    private static Context mcontext = null;
    private static RecipeClient rClient = null;
    private static String TAG = "RecipeClient";

    public static List<FindByIngredientsModel> result = null;

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

    public List<FindByIngredientsModel> getResult() {
        return result;
    }

    /**
     * Find recipes that use as many of the given ingredients as possible and have as little as possible missing ingredients. This is a whats in your fridge API endpoint.
     * @param    ingredientsStr    Required parameter: A comma-separated list of ingredients that the recipes should contain
     */

    public void searchRecipeByIngredients(String ingredientsStr){

        result = null;
        controller.findByIngredientsAsync(
                ingredientsStr,
                null,
                Constants.numberRecipeResults,
                Constants.ranking,
                null,
                new APICallBack<List<FindByIngredientsModel>>() {
            @Override
            public void onSuccess(HttpContext context, List<FindByIngredientsModel> response) {
                Log.d(TAG,"inSuccess "+response.size());
                synchronized (result){
                    result = response;
                }

            }

            @Override
            public void onFailure(HttpContext context, Throwable error) {

            }
        });
    }

    /**
     * Search recipes in natural language.
     * @param    query    Required parameter: The (natural language) recipe search query.
     * @param    cuisine    Optional parameter: The cuisine(s) of the recipes. One or more (comma separated) of the following: african, chinese, japanese, korean, vietnamese, thai, indian, british, irish, french, italian, mexican, spanish, middle eastern, jewish, american, cajun, southern, greek, german, nordic, eastern european, caribbean, or latin american.
     * @param    diet    Optional parameter: The diet to which the recipes must be compliant. Possible values are: pescetarian, lacto vegetarian, ovo vegetarian, vegan, and vegetarian.
     * @param    excludeIngredients    Optional parameter: An comma-separated list of ingredients or ingredient types that must not be contained in the recipes.
     * @param    intolerances    Optional parameter: A comma-separated list of intolerances. All found recipes must not have ingredients that could cause problems for people with one of the given tolerances. Possible values are: dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, and wheat.
     * @param    limitLicense    Optional parameter: Whether the recipes should have an open license that allows for displaying with proper attribution.
     * @param    number    Optional parameter: The number of results to return (between 0 and 100).
     * @param    offset    Optional parameter: The number of results to skip (between 0 and 900).
     * @param    type    Optional parameter: The type of the recipes. One of the following: main course, side dish, dessert, appetizer, salad, bread, breakfast, soup, beverage, sauce, or drink.
     * @param    queryParameters    Additional optional query parameters are supported by this endpoint
     * @return    Returns the void response from the API call
     */
    public void SearchRecipeByQuery(
            final String query,
            final String cuisine,
            final String diet,
            final String excludeIngredients,
            final String intolerances,
            final Boolean limitLicense,
            final Integer number,
            final Integer offset,
            final String type,
            Map<String, Object> queryParameters
    ){

        controller.searchRecipesAsync(
                query,
                cuisine,
                diet,
                excludeIngredients,
                intolerances,
                limitLicense,
                number,
                offset,
                type,
                queryParameters,
                new APICallBack<DynamicResponse>() {
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
