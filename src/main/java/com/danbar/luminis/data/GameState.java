package com.danbar.luminis.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent the game state at a given point.
 */
public class GameState {

    private GameStatus status;

    private double distanceUnit;
    private double angleUnit;

    // time since the beginning of the game
    private long gameTime;

    private Track track;
    Map<Integer, PlayerState> players = new HashMap<>();


    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Map<Integer, PlayerState> getPlayers() {
        return players;
    }

    public double getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(double distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public void setAngleUnit(double angleUnit) {
        this.angleUnit = angleUnit;
    }

    public double getAngleUnit() {
        return angleUnit;
    }
}
