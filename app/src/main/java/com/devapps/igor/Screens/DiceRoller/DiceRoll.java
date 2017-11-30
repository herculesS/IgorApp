package com.devapps.igor.Screens.DiceRoller;

/**
 * Created by tulio on 09/11/2017.
 */

public class DiceRoll {
    private String username;
    private String dices;
    private int result;
    private String sendTime;

    public DiceRoll() {}

    public DiceRoll(String username, String dices, int result, String sendTime) {
        this.username = username;
        this.dices = dices;
        this.result = result;
        this.sendTime = sendTime;
    }

    public String getDices() {
        return dices;
    }

    public void setDices(String dices) {
        this.dices = dices;
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
