package com.lecture.nitika.grub.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tchet on 4/22/2017.
 */

public class RecipeBriefContent {


    public static List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();
    public static Map<String, RecipeItem> ITEM_MAP = new HashMap<String, RecipeItem>();

    public static void addItem(RecipeItem item){
        ITEMS.add(item);
    }

    public static void deleteAllItems(){
        ITEMS.clear();
    }

}
