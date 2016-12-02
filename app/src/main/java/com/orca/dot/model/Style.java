package com.orca.dot.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by master on 15/6/16.
 */

@IgnoreExtraProperties
public class Style extends BaseModel{
    public String style_name;
    public String style_image;
    public long style_price;
    public long style_date_added;
    public String style_id;
    public String category_id;
    public boolean isLiked;
    public boolean isAdded;

    public Style() {
    }


}
