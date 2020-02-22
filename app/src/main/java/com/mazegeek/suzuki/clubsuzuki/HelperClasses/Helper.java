/*
 * Created by Alvi Khalil on 10/22/18 1:04 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/5/18 4:57 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.util.Log;

public class Helper {
    public static final boolean LogPrintCheck = true;
    public static final boolean LogExceptionCheck = true;
    public static final boolean LogVerboseCheck = true;
    public static final boolean LogWeirdCheck = true;

    public static void LogPrint(String TAG, String message) {
        if(LogPrintCheck){
            Log.d(TAG,message);
        }
    }

    public static void LogPrintException(String exception, String exceptionMessage) {
        if(LogExceptionCheck){
            Log.e(exception,exceptionMessage);

        }
    }

    public static void LogPrintExcepWithThrow(String exception, String exceptionMessage,Throwable tr) {
        if(LogExceptionCheck){
            Log.e(exception, exceptionMessage, tr);
        }
    }

    public static void LogPrintVerbose(String exception, String exceptionMessage) {
        if(LogVerboseCheck){
            Log.v(exception,exceptionMessage);

        }
    }

    public static void LogPrintVerboseWithThrow(String exception, String exceptionMessage,Throwable tr) {
        if(LogVerboseCheck){
            Log.v(exception, exceptionMessage, tr);
        }
    }

    public static void LogPrintWeird(String exception, String exceptionMessage) {
        if(LogWeirdCheck){
            Log.w(exception,exceptionMessage);

        }
    }

    public static void LogPrintWeirdWithThrow(String exception, String exceptionMessage,Throwable tr) {
        if(LogWeirdCheck){
            Log.w(exception, exceptionMessage, tr);
        }
    }


}
