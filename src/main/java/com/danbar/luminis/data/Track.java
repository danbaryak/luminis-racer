package com.danbar.luminis.data;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The racing track, loaded from JSON file.
 */
public class Track {

    List<Point> outerGrass;
    List<Point> track;
    List<Point> innerGrass;
    List<Point> pavement;

    Path outerGrassPath;
    Path trackPath;
    Path innerGrassPath;
    Path pavementPath;

    public static double WIDTH = 800;
    public static double HEIGHT = 500;

    public Track() {

    }

    private Path buildPath(List<Point> points) {
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (i == 0) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
            } else {
                path.getElements().add(new LineTo(p.getX(), p.getY()));
            }
        }
        path.getElements().add(new ClosePath());
        path.setFillRule(FillRule.EVEN_ODD);
        path.setFill(Color.WHITE);

        return path;
    }

    public TrackSection getSection(double x, double y) {
        if (! outerGrassPath.contains(x, y)) {
            // boom you're dead
            return TrackSection.WALL;
        } else if (! trackPath.contains(x, y)) {
            // you're in the outer grass
            return TrackSection.GRASS;
        } else if (! innerGrassPath.contains(x, y)) {
            // you're on the road
            return TrackSection.ROAD;
        } else if (! pavementPath.contains(x, y)) {
            // in the inner grass
            return TrackSection.GRASS;
        } else {
            // bam
            return TrackSection.WALL;
        }
    }

    /**
     * Returns the quadrant at a given point.
     * @return
     */
    public int getQuadrantAt(double x, double y) {
        return x < WIDTH / 2
                ? y < HEIGHT / 2 ? 4 : 3
                : y < HEIGHT / 2 ? 1 : 2;
    }

    public List<Point> getOuterGrass() {
        return outerGrass;
    }

    public void setOuterGrass(List<Point> outerGrass) {
        this.outerGrass = outerGrass;
        this.outerGrassPath = buildPath(outerGrass);
    }

    public List<Point> getTrack() {
        return track;
    }

    public void setTrack(List<Point> track) {
        this.track = track;
        this.trackPath = buildPath(track);
    }

    public List<Point> getInnerGrass() {
        return innerGrass;
    }

    public void setInnerGrass(List<Point> innerGrass) {
        this.innerGrass = innerGrass;
        this.innerGrassPath = buildPath(innerGrass);
    }

    public List<Point> getPavement() {
        return pavement;
    }

    public void setPavement(List<Point> pavement) {
        this.pavement = pavement;
        this.pavementPath = buildPath(pavement);
    }
}
