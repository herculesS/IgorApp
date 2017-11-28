package com.devapps.igor.DataObject;

import java.io.Serializable;

/**
 * Created by hercules on 25/11/17.
 */

public class Notification implements Serializable {
    private String mAdventureId;
    private NotificationType mNotificationType;
    private String mAdventureName;

    public enum NotificationType {AddedAsMaster, AddedAsPlayer}


    public String getAdventureName() {
        return mAdventureName;
    }

    public void setAdventureName(String adventureName) {
        mAdventureName = adventureName;
    }


    public String getAdventureId() {
        return mAdventureId;
    }

    public void setAdventureId(String adventureId) {
        mAdventureId = adventureId;
    }

    public NotificationType getNotificationType() {
        return mNotificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        mNotificationType = notificationType;
    }


}
