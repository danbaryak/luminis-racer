package com.danbar.luminis;

/**
 * Configurable constants used throughout the game. In a real game I would probably have these configurable
 * by a properties file / spring context bean.
 */
public class GameConstants {
    /* Maximum game time (even if nobody wins) */
    public static final int MAX_GAME_TIME_MINUTES = 15;
    /* Maximum time in minutes that a game can continue after there is a winner */
    public static final int MAX_GAME_TIME_AFTER_WIN_MINUTES = 5;
    /* interval between keystroke updates (in milliseconds) */
    public static final int KEY_UPDATE_INTERVAL = 500;
    /* Interval between each update of car positions */
    public static final int POSITION_UPDATE_INTERVAL = 200;
    /* Game results will be displayed for this amount of seconds before continuing to the next game */
    public static final int DISPLAY_RESULTS_TIME_SEC = 30;
    /* The number of laps a user has to complete */
    public static final int NUMBER_OF_LAPS = 3;
}
