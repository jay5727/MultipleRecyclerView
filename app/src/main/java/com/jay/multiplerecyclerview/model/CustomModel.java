package com.jay.multiplerecyclerview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jay on 13-09-2018.
 */
public class CustomModel {

    @SerializedName("Name")
    @Expose
    private String name;

    public CustomModel(String name, String marks) {
        this.name = name;
        this.marks = marks;
    }


    @SerializedName("Marks")
    @Expose
    private String marks;

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
