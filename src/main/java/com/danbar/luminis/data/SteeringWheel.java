package com.danbar.luminis.data;

/**
 * Created by danbar-yaakov on 5/8/14.
 */
public enum SteeringWheel {


    LEFT(- Player.ANGLE_UNIT),
    STRAIGHT(0),
    RIGHT(Player.ANGLE_UNIT);


    private double angle;

    SteeringWheel(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public SteeringWheel turnRight() {
        return ordinal() < values().length - 1
                ? SteeringWheel.values()[ordinal() + 1]
                : this;
    }


    public SteeringWheel turnLeft() {
        return ordinal() > 0
                ? SteeringWheel.values()[ordinal() - 1]
                : this;
    }
}
