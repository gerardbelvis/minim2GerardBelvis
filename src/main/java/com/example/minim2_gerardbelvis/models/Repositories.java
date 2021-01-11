package com.example.minim2_gerardbelvis.models;

import java.io.Serializable;

public class Repositories implements Serializable {
    private static Repositories repoinstance;

    public String repoName;
    public int repoID;
    public String language;
    public  Repositories(){}

    public static synchronized Repositories getInstance(){
        if(repoinstance == null){
            repoinstance = new Repositories();
        }
        return repoinstance;
    }

    public Repositories(String repoName, int id, String language){
        this.repoName = repoName;
        this.repoID = id;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getRepoID() {
        return repoID;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepoID(int repoID) {
        this.repoID = repoID;
    }
}
