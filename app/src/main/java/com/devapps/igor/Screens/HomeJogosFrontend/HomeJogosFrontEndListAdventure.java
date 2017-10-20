package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 20/10/17.
 */

public class HomeJogosFrontEndListAdventure {

    private String title;
    private String nextsession;
    private String progressbar;
    private String imageUrl;
    private String createadventure;

    public HomeJogosFrontEndListAdventure(){

    }


    public HomeJogosFrontEndListAdventure(String title, String nextsession, String progressbar, String imageUrl, String createadventure) {
        this.title = title;
        this.nextsession = nextsession;
        this.progressbar = progressbar;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
