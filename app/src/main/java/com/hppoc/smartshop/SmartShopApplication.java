package com.hppoc.smartshop;

import android.app.Application;

import com.arubanetworks.meridian.Meridian;

/**
 * Created by Chirag Sidhiwala on 5/30/2019.
 * chirag.sidhiwala@hotmail.com
 */
public class SmartShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Meridian.configure(this);
    }
}
