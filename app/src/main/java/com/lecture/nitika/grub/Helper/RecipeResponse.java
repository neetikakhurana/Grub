package com.lecture.nitika.grub.Helper;

import com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;

import java.util.List;

/**
 * Created by tchet on 4/22/2017.
 */

public class RecipeResponse {

    private boolean isSuccess = false;
    private List<FindByIngredientsModel> resultList = null;
    private String errorStr = null;
    private static RecipeResponse rResponse = null;

    public static RecipeResponse getInstance(){
        if(rResponse == null){
            rResponse = new RecipeResponse();
        }
        return rResponse;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<FindByIngredientsModel> getResultList() {
        return resultList;
    }

    public void setResultList(List<FindByIngredientsModel> resultList) {
        this.resultList = resultList;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public void setDefaults(){
        setErrorStr(null);
        setSuccess(false);
        setResultList(null);
    }
}
