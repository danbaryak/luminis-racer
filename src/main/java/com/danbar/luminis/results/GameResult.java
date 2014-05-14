package com.danbar.luminis.results;

import java.util.Date;

/**
 * Created by danbar-yaakov on 5/13/14.
 */
public class GameResult {

    private String playerName;
    private Date when;
    private long finishTimeInMillis;

    public GameResult(String playerName, long finishTimeInMillis) {
        this.playerName = playerName;
        this.finishTimeInMillis = finishTimeInMillis;
        this.when = new Date();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public long getFinishTimeInMillis() {
        return finishTimeInMillis;
    }

    public void setFinishTimeInMillis(long finishTimeInMillis) {
        this.finishTimeInMillis = finishTimeInMillis;
    }
}
