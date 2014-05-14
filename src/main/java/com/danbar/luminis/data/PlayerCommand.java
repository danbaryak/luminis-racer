package com.danbar.luminis.data;

/**
 * Represents a single command sent from client to server instructing the player's car to either turn or change
 * its speed.
 */
public enum PlayerCommand {
    NONE,
    TURN_LEFT,
    TURN_RIGHT,
    FASTER,
    SLOWER
}
