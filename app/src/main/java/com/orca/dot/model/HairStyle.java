package com.orca.dot.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by master on 15/6/16.
 */

@IgnoreExtraProperties
public class HairStyle {
    public String Name;
    public String Image;
    public String Length;
    public String uniqueKey;
    public int likesCount;
    public Map<String, Boolean> likes = new HashMap<>();
    public Map<String, Boolean> carts = new HashMap<>();

    public HairStyle() {

    }

    public HairStyle(String name, String image, String length, int likesCount) {
        this.Name = name;
        this.Image = image;
        this.Length = length;
        this.likesCount = likesCount;
    }
}
