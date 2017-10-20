package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 07/10/17.
 */

public class HomeJogosFrontendListItem {

    private String head;
    private String desc;
    private String team;
    private String firstappearance;
    private String createdby;
    private String publisher;
    private String imageUrl;
    private String bio;

    public HomeJogosFrontendListItem(String head, String desc, String team, String firstappearance, String createdby, String publisher, String imageUrl, String bio) {
        this.head = head;
        this.desc = desc;
        this.team = team;
        this.firstappearance = firstappearance;
        this.createdby = createdby;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getTeam() {
        return team;
    }

    public String getFirstappearance() {
        return firstappearance;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBio() {
        return bio;
    }
}
