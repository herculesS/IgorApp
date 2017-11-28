package com.devapps.igor.Util;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Notification;
import com.devapps.igor.DataObject.Profile;

/**
 * Created by hercules on 25/11/17.
 */

public class DataObjectUtils {
    static public boolean isPlayerInTheAdventure(Adventure a, String playerId) {
        boolean isPlayerInTheAdventure = false;

        if (a.getDMChar() != null && a.getDMChar().getPlayerId().equals(playerId)) {
            return true;
        }
        for (Character c : a.getCharacters()) {
            if (c.getPlayerId().equals(playerId)) {
                isPlayerInTheAdventure = true;
                break;
            }
        }
        return isPlayerInTheAdventure;
    }

    static public boolean isAddPlayerNotificationAlreadyAdded(Adventure a, Profile p) {
        if (p.getNotifications() != null) {
            for (Notification n : p.getNotifications()) {
                if (n.getAdventureId().equals(a.getId()) && n.getNotificationType()
                        == Notification.NotificationType.AddedAsPlayer) {
                    return true;
                }
            }
            return false;

        } else {
            return false;
        }
    }
}
