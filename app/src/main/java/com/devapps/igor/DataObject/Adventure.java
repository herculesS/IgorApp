package com.devapps.igor.DataObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hercules on 09/09/17.
 */

public class Adventure implements Serializable {
    public static final String DefaultAdventureName = "Aventura sem título";
    public static final String DefaultAdventureSummary = "Aventura sem resumo";
    public static final String ADVENTURE_TAG = "Adventure";

    private String mName;
    private String mSummary;
    private ArrayList<Session> mSessions;


    private ArrayList<Character> mCharacters;
    private String mDMid;

    public Adventure(String name) {
        mName = name;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
        mDMid = null;
        mCharacters = new ArrayList<Character>();
    }

    public Adventure() {
        mName = DefaultAdventureName;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
        mDMid = null;
        mCharacters = new ArrayList<Character>();
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public ArrayList<Session> getSessions() {
        return mSessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        mSessions = sessions;
    }

    public void addSession(Session s) {
        mSessions.add(s);
    }

    public void removeSession(Session s) {
        if (mSessions != null)
            mSessions.remove(s);
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Character> getCharacters() {
        return mCharacters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        mCharacters = characters;
    }

    public String getDMid() {
        return mDMid;
    }

    public void setDMid(String DMid) {
        mDMid = DMid;
    }
}
