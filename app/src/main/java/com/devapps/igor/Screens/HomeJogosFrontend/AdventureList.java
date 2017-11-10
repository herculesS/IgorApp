package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 20/10/17.
 */

public class AdventureList {

    private String title;
    private String nextsession;
    private String progressbar;
    //private String imageUrl;
    private String createadventure;

    public AdventureList(){

    }
    public AdventureList(String title, String nextsession, String progressbar, String imageUrl, String createadventure) {
        this.title = title;
        this.nextsession = nextsession;
        this.progressbar = progressbar;
     //   this.imageUrl = imageUrl;
        this.createadventure = createadventure;
    }

    public String getTitle() {
        return title;
    }

    public String getNextsession() {
        return nextsession;
    }

    public String getProgressbar() {
        return progressbar;
    }

    public String getCreateadventure() {
        return createadventure;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNextsession(String nextsession) {
        this.nextsession = nextsession;
    }

    public void setProgressbar(String progressbar) {
        this.progressbar = progressbar;
    }

    public void setCreateadventure(String createadventure) {
        this.createadventure = createadventure;
    }

    // public String getImageUrl() {
   //     return imageUrl;
   // }
}
