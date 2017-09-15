package com.devapps.igor.DataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 08/06/2017.
 */

public class Profile implements Serializable {

    private String id;
    private String name;
    private String email;
    private String birthday;
    private String adress;
    private String phoneNumber;
    private String description;

    private List<String> myProjects = new ArrayList<>();
    private List<String> favoritesProjects = new ArrayList<>();
    private List<String> projectsParticipating = new ArrayList<>();

    /**********************
     * Profile Type
     * * 0 - no profile
     * * 1 - hacker
     * * 2 - hipster
     * * 3 - hustler
     **********************/
    private int profileType;

    public void addMyProject(String projectId){
        myProjects.add(projectId);
    }

    public void addFavoriteProject(String projectId){
        favoritesProjects.add(projectId);
    }

    public void addProjectParticipating(String projectId){
        projectsParticipating.add(projectId);
    }

    public boolean removeMyProject(String projectId){
        return myProjects.remove(projectId);
    }

    public boolean removeFavoriteProject(String projectId){
        return favoritesProjects.remove(projectId);
    }

    public boolean removeProjectParticipating(String projectId){
        return  projectsParticipating.remove(projectId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMyProjects() {
        return myProjects;
    }

    public List<String> getFavoritesProjects() {
        return favoritesProjects;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }
}
