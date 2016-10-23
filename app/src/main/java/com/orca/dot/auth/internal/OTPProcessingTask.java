package com.orca.dot.auth.internal;

import android.content.Context;
import android.os.AsyncTask;

import com.orca.dot.auth.InitializeAPI;
import com.orca.dot.auth.callbacks.OTPVerificationCallback;
import com.orca.dot.backend.dotApi.model.OtpVerificationResponse;
import com.orca.dot.utils.Constants;
import com.orca.dot.utils.NetworkUtil;

import java.io.IOException;

class OTPProcessingTask extends AsyncTask<Object, Void, OtpVerificationResponse> {
    private OTPVerificationCallback otpVerificationCallback;

    @Override
    protected OtpVerificationResponse doInBackground(Object... params) {
        Context context = (Context) params[0];
        String OTP = (String) params[1];
        String phoneNumber = (String) params[2];
        String sessionId = (String) params[3];
        otpVerificationCallback = (OTPVerificationCallback) params[4];

        try {
            if (new NetworkUtil(context).hasActiveInternetConnection())
                return InitializeAPI.dotApiService.verifyOTP(OTP, phoneNumber, sessionId).execute();
            else {
                OtpVerificationResponse otpVerificationResponse = new OtpVerificationResponse();
                otpVerificationResponse.setStatus(Constants.OTP_VERIFICATION_ERROR);
                otpVerificationResponse.setDetails("Error in connection");
                return otpVerificationResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            OtpVerificationResponse otpVerificationResponse = new OtpVerificationResponse();
            otpVerificationResponse.setStatus(Constants.OTP_VERIFICATION_ERROR);
            otpVerificationResponse.setDetails(e.getMessage());
            return otpVerificationResponse;
        }
    }

    @Override
    protected void onPostExecute(OtpVerificationResponse response) {
        if (response.getStatus() == Constants.OTP_MATCHED) {
            otpVerificationCallback.otpVerificationSuccess(response.getToken());
        } else {
            otpVerificationCallback.otpVerificationFailure(response.getDetails());
        }
    }
}
