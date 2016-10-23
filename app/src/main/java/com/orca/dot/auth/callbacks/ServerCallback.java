package com.orca.dot.auth.callbacks;

/**
 * Interface of listening Server callbacks
 *
 * @author amit
 */

public interface ServerCallback {

    void otpSendSuccessfully(String phoneNumber, String details);

    void otpSentError(String status);

}
