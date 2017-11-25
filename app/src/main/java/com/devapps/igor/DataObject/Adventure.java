package com.devapps.igor.DataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hercules on 09/09/17.
 */

public class Adventure implements Serializable {
    public static final String DefaultAdventureName = "Aventura sem título";
    public static final String DefaultAdventureSummary = "Aventura sem resumo";
    public static final String ADVENTURE_TAG = "Adventure";

    private String mName;
    private String mSummary;
    private int mBackground;
    private ArrayList<Session> mSessions;
    private String mId;


    private ArrayList<Character> mCharacters;
    private Character mDMChar;

    public Adventure(String name, String id) {
        mName = name;
        mId = id;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
        Random rand = new Random();
        mBackground = rand.nextInt(5)+1;
        mDMChar = null;
        mCharacters = new ArrayList<Character>();
    }
    public Adventure() {
        mSessions = new ArrayList<Session>();
        mCharacters = new ArrayList<Character>();
    }
    public Adventure(String id) {
        mId = id;
        mName = DefaultAdventureName;
        mSummary = DefaultAdventureSummary;
        mSessions = new ArrayList<Session>();
       // mBackground = DefaultBackground;
        Random rand = new Random();
        mBackground = rand.nextInt(5)+1;
        mDMChar = null;
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

    public int getBackground() {
        return mBackground;
    }

    public void setBackground(int mBackground) {
        this.mBackground = mBackground;
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

    public void addCharacter(Character character) {
        if (mCharacters != null) {
            mCharacters.add(character);
        }
    }

    public void removeCharacter(Character character) {
        if (mCharacters != null)
            mCharacters.remove(character);
    }


    public Character getDMChar() {
        return mDMChar;
    }

    public void setDMChar(Character DMChar) {
        mDMChar = DMChar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
