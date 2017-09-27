package com.example.arpansharma.bloodbankremastered;

import android.content.Context;

/**
 * Created by Arpan Sharma on 23-09-2017.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return  mInstance;
    }

}
