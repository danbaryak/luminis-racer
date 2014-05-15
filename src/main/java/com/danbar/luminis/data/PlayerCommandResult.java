package com.danbar.luminis.data;

/**
 * Result returned to the client as a result of a key press that is sent to the server.
 */
public class PlayerCommandResult {
    private boolean accepted;

    public PlayerCommandResult() {

    }

    /**
     * Creates the result.
     *
     * @param accepted Indicates that the server has accepted the key press (no previous command is waiting
     *                 to be processed)
     */
    public PlayerCommandResult(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
