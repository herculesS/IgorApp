package com.devapps.igor.DataObject;

import java.io.Serializable;

/**
 * Created by hercules on 25/10/17.
 */

public class Character implements Serializable {
    private String mPlayerId;
    private String mName;
    private String mSummary;

    public Character(String playerId, String name, String summary) {
        mPlayerId = playerId;
        mName = name;
        mSummary = summary;
    }

    public Character() {

    }


    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String playerId) {
        mPlayerId = playerId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }


}
