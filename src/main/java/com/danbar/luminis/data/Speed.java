package com.danbar.luminis.data;

/**
 * Represents the car speed.
 */
public enum Speed {
    STOPPED,
    SLOW,
    FAST;

    public Speed faster() {
        return this.ordinal() < Speed.values().length - 1
                ? Speed.values()[this.ordinal() + 1]
                : this;
    }

    public Speed slower() {
        return this.ordinal() > 0
                ? Speed.values()[this.ordinal() - 1]
                : this;
    }

}
