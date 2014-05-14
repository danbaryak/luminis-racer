package com.danbar.luminis.data;

/**
 * Created by danbar-yaakov on 5/9/14.
 */
public class PlayerCommandResult {
    private boolean accepted;

    public PlayerCommandResult() {

    }

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
