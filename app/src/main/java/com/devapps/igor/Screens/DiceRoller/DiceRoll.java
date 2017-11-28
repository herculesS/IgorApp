package com.devapps.igor.Screens.DiceRoller;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by tulio on 09/11/2017.
 */

public class DiceRoll {

    private String username;
    private int diceType;
    private int diceNumber;
    private int diceModifier;
    private int result;
    private String sendTime;

    private Random randomDice = new Random();

    public DiceRoll() {}

    public DiceRoll(String username, int diceType, int diceNumber, int diceModifier) {
        this.username = username;
        this.diceType = diceType;
        this.diceNumber = diceNumber;
        this.diceModifier = diceModifier;
        roll();
    }

    public void roll(){
        Calendar today = Calendar.getInstance();
        sendTime = today.get(Calendar.HOUR_OF_DAY) + ":" + today.get(Calendar.MINUTE);

        result = 0;
        for(int i = 0; i<diceNumber; i++) {
            result += randomDice.nextInt(diceType) + 1;
        }
        result += diceModifier;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
