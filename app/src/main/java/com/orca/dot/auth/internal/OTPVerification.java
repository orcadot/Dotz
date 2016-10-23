package com.orca.dot.auth.internal;

import android.content.Context;

import com.orca.dot.auth.callbacks.OTPVerificationCallback;


public class OTPVerification {

    public static void verify(Context context, String otp, String phoneNum, String sessionID, OTPVerificationCallback otpVerificationCallback) {
        new OTPProcessingTask().execute(context, otp, phoneNum, sessionID, otpVerificationCallback);
    }
}
