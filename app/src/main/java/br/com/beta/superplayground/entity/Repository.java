package br.com.beta.superplayground.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renan on 09/05/16.
 */
public class Repository {

    @SerializedName("name")
    private String name;

    @SerializedName("full-name")
    private String fullName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
