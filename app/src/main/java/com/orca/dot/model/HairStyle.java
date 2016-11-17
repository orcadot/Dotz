package com.orca.dot.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by master on 15/6/16.
 */

@IgnoreExtraProperties
public class HairStyle extends BaseModel{
    public String Name;
    public String Image;
    public String uniqueKey;
    public int likesCount;
    public Map<String, Boolean> likes = new HashMap<>();
    public Map<String, Boolean> carts = new HashMap<>();
    public Map<String, Boolean> filters = new HashMap<>();

    public HairStyle() {
        super();
    }

    public HairStyle(String name, String image, int likesCount, Map likes, Map carts, Map filters) {
        this.Name = name;
        this.Image = image;
        this.likesCount = likesCount;
        this.likes = likes;
        this.carts = carts;
        this.filters = filters;
    }
}
