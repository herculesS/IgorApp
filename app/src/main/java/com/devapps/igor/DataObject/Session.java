package com.devapps.igor.DataObject;

import java.io.Serializable;

/**
 * Created by hercules on 29/09/17.
 */

public class Session implements Serializable {

    public static final String DefaultSessionTitle = "Sessão sem título";
    public static final String DefaultSessionSummary = "Sessão sem resumo";
    public static final String DefaultSessionDate = "00/00/0000";

    private String mTitle;
    private String mDate;
    private String mSummary;

    public Session(String title,String date) {
        mTitle = title;
        mDate = date;
        mSummary = DefaultSessionSummary;


    }

    public Session() {
        mTitle = DefaultSessionTitle;
        mDate = DefaultSessionDate;
        mSummary = DefaultSessionSummary;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }


}
