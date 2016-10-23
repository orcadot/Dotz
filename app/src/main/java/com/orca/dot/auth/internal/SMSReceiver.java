package com.orca.dot.auth.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            assert pdusObj != null;
            for (Object aPdusObj : pdusObj) {
                SmsMessage currentMessage;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj, bundle.getString("format"));
                } else
                    currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                String senderAddress = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();

                Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                // if the SMS is not from our gateway, ignore the message
                if (!senderAddress.toUpperCase().contains("VERIFY")) {
                    return;
                }

                // verification code from sms
                String verificationCode = extractDigits(message);

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("DOT_OTP_RECEIVED");
                broadcastIntent.putExtra("otp", verificationCode);
                context.sendBroadcast(broadcastIntent);
                Log.e(TAG, "OTP received: " + verificationCode);

            }
        }
    }

    public String extractDigits(final String message) {
        final Pattern pattern = Pattern.compile("(\\d{6})");
        final Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

}
