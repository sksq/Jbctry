package com.shubham.rural2g;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

/**
 * Updated by Ashish on 23-01-2015.
 */
public class RuralApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "iNUUU9wcOd9ICQbsLuIbxEuymmB8YltZERMjjAQS", "28tSzrXYN27MpIPg0J9GaL9pA2SglrqROdOiU3lr");
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
