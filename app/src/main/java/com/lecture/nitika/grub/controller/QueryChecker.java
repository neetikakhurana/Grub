package com.lecture.nitika.grub.controller;

/**
 * Created by tchet on 4/22/2017.
 */

public class QueryChecker {

    public static boolean isQueryProper(String str){

        if(str== null || str.isEmpty()){
            return false;
        }

        String[] splitarr = str.trim().split(",");
        if(splitarr.length <= 0){
            return false;
        }
        return true;
    }
}
