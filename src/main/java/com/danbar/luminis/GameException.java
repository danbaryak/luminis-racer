package com.danbar.luminis;

/**
 * Exception that can be thrown during the game.
 */
public class GameException extends RuntimeException {
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(String message) {
        super(message);
    }
}
