package com.orca.dot.utils.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amit on 30/11/16.
 */

public class OTPValidator {
    private static final String OTP_PATTERN = "^[0-9]{6}$";
    private Pattern pattern;
    private Matcher matcher;

    public OTPValidator() {
        pattern = Pattern.compile(OTP_PATTERN);
    }

    /**
     * Validate otp with regular expression
     *
     * @param otp for validation
     * @return true valid otp, false invalid otp
     */
    public boolean validate(final String otp) {

        matcher = pattern.matcher(otp);
        return matcher.matches();

    }
}
