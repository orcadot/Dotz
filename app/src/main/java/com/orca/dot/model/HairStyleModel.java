package com.orca.dot.model;

import org.json.JSONObject;

/**
 * Defines the data structure for style object.
 *
 * @author Amit on 08-06-2016.
 */
public class HairStyleModel {

    private String expertInfo;
    private String faceCut;
    private String fileUrl;
    private String gender;
    private String length;
    private int likes;
    private String name;
    private int type;
    private JSONObject price;

    /**
     * Required public constructor
     */
    public HairStyleModel() {
    }

    /**
     * Use this constructor to create new StyleObject.
     * Takes different params
     *
     * @param expertInfo
     * @param faceCut
     * @param likes
     * @param type
     * @param fileUrl
     * @param gender
     * @param length
     * @param likes
     * @param name
     * @param price
     */

    public HairStyleModel(String expertInfo, String faceCut, String fileUrl, String gender, String length,
                          int likes, String name, int type, JSONObject price) {
        this.expertInfo = expertInfo;
        this.faceCut = faceCut;
        this.fileUrl = fileUrl;
        this.gender = gender;
        this.length = length;
        this.likes = likes;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    /**
     * @return expertInfo
     */
    public String getExpertInfo() {
        return expertInfo;
    }

    /**
     * @return faceCut
     */
    public String getFaceCut() {
        return faceCut;
    }

    /**
     * @return fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return Length
     */
    public String getLength() {
        return length;
    }

    /**
     * @return likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * @return price
     */
    public JSONObject getPrice() {
        return price;
    }
}
