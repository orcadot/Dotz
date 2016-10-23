package com.orca.dot.auth.internal;

import android.content.Context;
import android.os.AsyncTask;

import com.orca.dot.auth.InitializeAPI;
import com.orca.dot.auth.callbacks.ServerCallback;
import com.orca.dot.backend.dotApi.model.OtpSentResponse;
import com.orca.dot.utils.Constants;
import com.orca.dot.utils.NetworkUtil;

import java.io.IOException;

class AuthenticationAsyncTask extends AsyncTask<Object, Void, OtpSentResponse> {
    private Context context;
    private ServerCallback serverCallback;

    @Override
    protected OtpSentResponse doInBackground(Object... params) {
        context = (Context) params[0];
        String phoneNum = (String) params[1];
        serverCallback = (ServerCallback) params[2];

        try {
            if (new NetworkUtil(context).hasActiveInternetConnection())
                return InitializeAPI.dotApiService.sendOTP(phoneNum).execute();
            else {
                OtpSentResponse otpSentResponse = new OtpSentResponse();
                otpSentResponse.setStatus(Constants.OTP_SENT_ERROR);
                otpSentResponse.setDetails("Error in connectivity.");
                return otpSentResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            OtpSentResponse otpSentResponse = new OtpSentResponse();
            otpSentResponse.setStatus(Constants.OTP_SENT_ERROR);
            otpSentResponse.setDetails(e.getMessage());
            return otpSentResponse;
        }

    }

    @Override
    protected void onPostExecute(OtpSentResponse result) {
        if (result.getStatus() == Constants.OTP_SENT_SUCCESS)
            serverCallback.otpSendSuccessfully(result.getPhoneNumber(), result.getDetails());
        else
            serverCallback.otpSentError(result.getDetails());
    }

}
