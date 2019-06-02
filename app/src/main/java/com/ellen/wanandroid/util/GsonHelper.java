package com.ellen.wanandroid.util;

import com.google.gson.Gson;

public class GsonHelper {

    private static Gson gson;

    public static Gson getInstance(){
        if(gson == null){
            synchronized (GsonHelper.class){
                if(gson == null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

}
