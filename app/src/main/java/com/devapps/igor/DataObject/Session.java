package com.devapps.igor.DataObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hercules on 29/09/17.
 */

public class Session implements Serializable {
    private  String mName;
    private String mDate;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }


}
