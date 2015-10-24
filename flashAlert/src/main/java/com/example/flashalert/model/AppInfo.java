package com.example.flashalert.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by hanh on 11/10/2015.
 */
public class AppInfo implements Serializable{
    private String name;
    private String packageName;
    private Drawable icon;
    public AppInfo(String name, String packageName, Drawable icon){
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return name;
    }

    public String getPackageName(){return packageName;}

    public Drawable getIcon(){
        return icon;
    }
}
