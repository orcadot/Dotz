package com.orca.dot.auth.callbacks;

/**
 * Interface of listening OTP verifications callback
 *
 * @author amit
 */

public interface OTPVerificationCallback {

    void otpVerificationFailure(String error);

    void otpVerificationSuccess(String token);
}
