package com.danbar.luminis.data;

import org.apache.log4j.Logger;

/**
 * Created by danbar-yaakov on 5/3/14.
 */
public class Player {

    private static final Logger logger = Logger.getLogger(Player.class);
    public static final double ANGLE_UNIT = Math.PI / 25;
    public static final double DISTANCE_UNIT = 5.0;

    /* The car steering state at a given time */
    private SteeringWheel steering = SteeringWheel.STRAIGHT;

    /* The car speed at a given time */
    private Speed speed = Speed.STOPPED;

    /* The section of the track on which the car is located at a given time (grass, road or wall) */
    private TrackSection trackSection = TrackSection.ROAD;

    private Track track;

    private double heading = 0;

    private int quadrantsPassed = 0;
    private int currentQuadrant = 1;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;
    private CarColor carColor;
    private String name;
    private PlayerState state = new PlayerState();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarColor getCarColor() {
        return carColor;
    }

    public void setCarColor(CarColor carColor) {
        this.carColor = carColor;
    }

    public PlayerState getState() {
        return state;
    }

    public void processPlayerCommand(PlayerCommand command) {
        if (command == PlayerCommand.NONE) {
            return;
        }
        switch (command) {
            case FASTER: speed = speed.faster();
                break;
            case SLOWER: speed = speed.slower();
                break;
            case TURN_RIGHT: steering = steering.turnRight();
                break;
            case TURN_LEFT: steering = steering.turnLeft();
        }
        state.setDistance(speed.ordinal() * DISTANCE_UNIT);
        state.setSteeringAngle(steering.getAngle());

        logger.info("After processing player command, speed is " + speed + " and steering is " + steering);
    }

    public void updatePosition(long gameTime) {

        double x = state.getX();
        double y = state.getY();
        if (speed != Speed.STOPPED) {

            double distance = speed.ordinal() * DISTANCE_UNIT * (trackSection == TrackSection.GRASS ? .5 : 1);
            heading = heading + steering.getAngle();
            double dx = Math.cos(heading) * distance;
            double dy = Math.sin(heading) * distance;
            state.setPosition(x + dx, y + dy);
            state.setHeading(heading);
        }
        trackSection = track.getSection(x, y);
        if (trackSection == TrackSection.WALL) {
            state.setCrashed(true);
        }

        int quadrant = track.getQuadrantAt(x, y);
        if ((quadrant == 1 && currentQuadrant == 4) || quadrant > currentQuadrant) {
            quadrantsPassed++;
        } else if ((quadrant == 4 && currentQuadrant == 1) || quadrant < currentQuadrant) {
            quadrantsPassed--;
        }
        currentQuadrant = quadrant;

//        logger.info("Player " + id + " is in quadrant " + currentQuadrant + ". Number of quadrants passed = " + quadrantsPassed);
        if (quadrantsPassed == 4) {
            // finished track
            state.setFinished(true);
            state.setFinishTime(gameTime);
        }


//        logger.info("After updating position, speed is " + speed + " and steering is " + steering);
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", carColor='" + carColor + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    public boolean isCrashed() {
        return state.isCrashed();
    }

    public boolean isFinished() {
        return state.isFinished();
    }
}
