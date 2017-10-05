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

    private String mName;
    private String mSummary;
    private ArrayList<Session> mSessions;

    public Adventure(String name) {
        mName = name;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
    }

    public Adventure() {
        mName = DefaultAdventureName;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
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

    public void addSession(Session s){
        mSessions.add(s);
    }

    public void removeSession(Session s) {
        if(mSessions != null)
            mSessions.remove(s);
    }


    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
}
