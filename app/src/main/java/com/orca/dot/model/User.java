package com.orca.dot.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

/**
 * Model class for user details
 *
 * @author amit
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String gender;
    public int genderCode;
    public int facecut_type;
    public String facecut;
    public String dateOfBirth = "ddmmyyyy";
    public String phone;
    public Map<String, Map<String, Style>> user_likes_cart;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String gender, int genderCode) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.genderCode = genderCode;
    }


}
