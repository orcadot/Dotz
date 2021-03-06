package com.orca.dot.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model class for user details
 *
 * @author amit
 */
@IgnoreExtraProperties
public class UserDetails {

    public String username;
    public String email;
    public String gender;
    public int genderCode;
    public boolean isProfileFilled;
    public boolean isFCTFilled;
    public int facecut_type;
    public String facecut;
    public String dateOfBirth = "ddmmyyyy";

    public UserDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserDetails(String username, String email, String gender, int genderCode, boolean isProfileFilled, boolean isFCTFilled) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.genderCode = genderCode;
        this.isFCTFilled = isFCTFilled;
        this.isProfileFilled = isProfileFilled;
    }


}
