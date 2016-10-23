package com.orca.dot.utils;

/**
 * Constants store most important strings and paths of the app.
 *
 * @author Amit Kumar
 */
public class Constants {

    public static final String PACKAGE_NAME = "com.orca.dotz";

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where user lists are stored (ie "userLists")
     */
    public static final String FIREBASE_LOCATION_HAIR_STYLE_DATA = "data";
    public static final String FIREBASE_SALON_DATA = "salonData";
    public static final String FIREBASE_LOCATION_USER_LISTS = "userLists";

    /**
     * Constants for bundles, extras and shared preferences keys
     */
    private static final int TYPE_UNISEX = 0;
    private static final int TYPE_MALE = 1;
    private static final int TYPE_FEMALE = 2;

    private static final String TYPE_UNISEX_STRING = "Unisex";
    private static final String TYPE_MALE_STRING = "Male";
    private static final String TYPE_FEMALE_STRING = "Female";


    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    public static String getStyleType(int type) {
        switch (type) {
            case TYPE_UNISEX:
                return TYPE_UNISEX_STRING;
            case TYPE_MALE:
                return TYPE_MALE_STRING;
            case TYPE_FEMALE:
                return TYPE_FEMALE_STRING;
            default:
                return TYPE_UNISEX_STRING;
        }
    }

    /**
     * Authentication related errors
     */
    // OTP sent successfully;
    public static final int OTP_SENT_SUCCESS = 1000;

    // error in OTP flow;
    public static final int OTP_SENT_ERROR = 2000;

    // OTP matched;
    public static final int OTP_MATCHED = 1001;

    // OTP mismathced;
    public static final int OTP_VERIFICATION_ERROR = 3001;

}
