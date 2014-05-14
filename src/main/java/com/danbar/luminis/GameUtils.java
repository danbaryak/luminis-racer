package com.danbar.luminis;

import com.danbar.luminis.data.Track;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Various utility functions.
 */
public class GameUtils {

    private static Logger logger = Logger.getLogger(GameUtils.class);

    /**
     * Reads the racing trak information from the JSON file.
     *
     * @return Track
     */
    public static Track readTrackFromFile() {
        Track track = null;
        JsonFactory jfactory = new JsonFactory();
        try {
            ObjectMapper mapper = new ObjectMapper();

            // read JSON format file, deserialize JSON, bind values to User class object
            track = mapper.readValue(GameUtils.class.getResource("/track.json"), Track.class);


        } catch (Exception exc) {
            logger.error("Failed to load track from JSON file", exc);
            throw new GameException("Failed to load track from JSON file", exc);
        }

        return track;


    }
}
