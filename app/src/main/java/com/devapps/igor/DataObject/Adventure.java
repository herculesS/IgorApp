package com.devapps.igor.DataObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hercules on 09/09/17.
 */

public class Adventure implements Serializable {
    private static final String DefaultAdventureName = "Aventura sem título";
    private String mName;
    private ArrayList<Session> mSessions;

    public Adventure(String name) {
        mName = name;
        mSessions = null;
    }

    public Adventure() {
        mName = DefaultAdventureName;
        mSessions = null;
    }

    public ArrayList<Session> getSessions() {
        return mSessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        mSessions = sessions;
    }

    public void addSession(Session s){
        if(mSessions == null) mSessions = new ArrayList<Session>();
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
