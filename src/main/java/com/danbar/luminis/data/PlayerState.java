package com.danbar.luminis.data;

/**
 * Player state, passed to the client upon request.
 */
public class PlayerState {
    private double x;
    private double y;
    private String name;
    private int id;
    private double heading = 0;
    private double distance = 0;

    private double steeringAngle = 0;
    private boolean crashed;
    private boolean finished;
    private long finishTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSteeringAngle() {
        return steeringAngle;
    }

    public void setSteeringAngle(double steeringAngle) {
        this.steeringAngle = steeringAngle;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getFinishTime() {
        return finishTime;
    }
}
