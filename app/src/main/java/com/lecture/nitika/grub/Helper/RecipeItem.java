package com.lecture.nitika.grub.Helper;

import com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;

import java.io.Serializable;

/**
 * Created by tchet on 4/22/2017.
 */

public class RecipeItem implements Serializable{

    private FindByIngredientsModel content = null;

    public RecipeItem(FindByIngredientsModel model) {
        this.content = model;
    }

    public FindByIngredientsModel getContent() {
        return content;
    }


}
