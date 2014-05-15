package com.danbar.luminis;

import com.danbar.luminis.data.*;
import com.danbar.luminis.results.GameResult;
import com.danbar.luminis.results.ResultStorage;
import org.apache.log4j.Logger;

import static com.danbar.luminis.GameConstants.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main game loop handler.
 */
public class GameWorld implements Runnable {

    private static final Logger logger = Logger.getLogger(GameWorld.class);

    /* The racing track, loaded on initialization from JSON file */
    private Track track;

    /* The game status at a given time */
    GameStatus status;

    /* player commands are stored here until they are consumed by the game loop each configured
       interval (KEY_UPDATE_INTERVAL) */
    private Map<Integer, PlayerCommand> commands;

    private long gameStartTime = 0;
    private long gameTime = 0;
    long lastKeyUpdate = 0;
    long lastPosUpdate = 0;
    // number of current players. used as an ID provider
    private int playerCount = 0;
    private ScheduledExecutorService executor;

    private Map<Integer, Player> players;

    // game result storage
    private ResultStorage resultStorage;

    public GameWorld() {
        executor = Executors.newSingleThreadScheduledExecutor();
        status = GameStatus.WAITING;
        players = new HashMap<>();
        track = GameUtils.readTrackFromFile();
        commands = new HashMap<>();
    }

    public void init() {
        executor.scheduleWithFixedDelay(this, 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Processes a join request. Join requests are only valid when the game status
     * is WAITING.
     *
     * @param playerName THe name of the newly joined player.
     */
    public synchronized Player join(String playerName) {
        if (status != GameStatus.WAITING) {
            throw new IllegalStateException("Game stat is not WAITING, cannot join");
        }


        playerCount++;
        Player player = new Player(playerCount, playerName);
        player.setTrack(this.track);

        // set initial player position
        PlayerState state = player.getState();

        state.setPosition(400 - ((player.getId() - 1)/ 3) * 28, 60 + ((player.getId() - 1) % 3) * 28);

        state.setName(player.getName());
        state.setId(player.getId());

        players.put(player.getId(), player);
        logger.info("New player added: " + player);

        if (playerCount >= 2) {
            // start or reset 10 second counter loop
            gameStartTime = System.currentTimeMillis();
        }

        return player;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * Fills the game state to be sent to the client.
     *
     * @param state The state to fill.
     */
    public synchronized void fillGameState(GameState state) {
        state.setStatus(status);
        state.setGameTime(gameTime);
        state.setTrack(track);
        state.getPlayers().clear();
        state.setDistanceUnit(Player.DISTANCE_UNIT);
        state.setAngleUnit(Player.ANGLE_UNIT);
        for (int playerId : players.keySet()) {
            PlayerState playerState = players.get(playerId).getState();
            state.getPlayers().put(playerId, playerState);
        }
    }

    public ResultStorage getResultStorage() {
        return resultStorage;
    }

    public void setResultStorage(ResultStorage resultStorage) {
        this.resultStorage = resultStorage;
    }

    /**
     * Processes a command sent by a player.
     *
     * @param playerId The Player's ID
     * @param command The command
     * @return true if the command has been accepted (if no previous command is waiting to be processed)
     */
    public synchronized boolean handlePlayerCommand(int playerId, PlayerCommand command) {
        if (! commands.containsKey(playerId)) {
            commands.put(playerId, command);
            return true;
        }
        return false;
    }

    /**
     * The game loop.
     */
    public synchronized void run() {
        try {
            if (gameStartTime == 0) {
                // no game is active
                return;
            }

            long timeInMs = System.currentTimeMillis() - gameStartTime;

            if (status == GameStatus.WAITING) {
                handleWaitingState(timeInMs);
            } else if (status == GameStatus.IN_PROGRESS) {
                handleGameInProgress(timeInMs);
            } else if (status == GameStatus.DONE) {
                handleDoneState(timeInMs);
            }
        } catch (Exception exc) {
            logger.error("Error caught during game loop " + exc);
        }
    }

    private void handleDoneState(long timeInMs) {
        long timeInSeconds = timeInMs / 1000;
        if (timeInSeconds >= DISPLAY_RESULTS_TIME_SEC) {
            logger.info("Starting a new game");
            setStatus(GameStatus.WAITING);
            initState();
        }
    }

    private void handleGameInProgress(long timeInMs) {
        gameTime = timeInMs;
        long timeInSeconds = timeInMs / 1000;
        long timeInMinutes = timeInSeconds / 60;
        // process key events every configured interval
        if (gameTime - lastKeyUpdate > KEY_UPDATE_INTERVAL) {

            lastKeyUpdate = gameTime;
            for (Player player : players.values()) {
                if (player.isFinished()) {
                    continue;
                }
                PlayerCommand command = commands.get(player.getId());
                if (command != null) {
                    player.processPlayerCommand(command);
                }
            }
            commands.clear();
        }


        // update car positions
        if (gameTime - lastPosUpdate > POSITION_UPDATE_INTERVAL) {
            lastPosUpdate = gameTime;
            int stillAlive = 0;
            boolean someoneHasFinished = false;
            for (Player player : players.values()) {
                if (player.isFinished()) {
                    someoneHasFinished = true;
                    continue;
                }
                player.updatePosition(gameTime);
                if (! player.isCrashed()) {
                    stillAlive++;
                }
            }
            if (stillAlive == 0 || timeInMinutes >= MAX_GAME_TIME_MINUTES
                    || (someoneHasFinished && timeInMinutes >= MAX_GAME_TIME_AFTER_WIN_MINUTES)) {

                // end the game and store results
                for (Player p : players.values()) {
                    long finishTime = p.getState().getFinishTime();
                    if (finishTime > 0) {
                        resultStorage.storeResult(new GameResult(p.getName(), finishTime));
                    }
                }

                setStatus(GameStatus.DONE);
                gameStartTime = System.currentTimeMillis();

            }
        }
    }

    private void handleWaitingState(long timeInMs) {
        gameTime = -10000 + timeInMs;
        if (gameTime >= 0) {
            status = GameStatus.IN_PROGRESS;
            gameStartTime = System.currentTimeMillis();
            gameTime = 0;
        }
    }

    /**
     * Initializes the game world state, preparing it for a new game. 
     */
    private void initState() {
        this.players.clear();
        this.gameStartTime = 0;
        this.commands.clear();
        this.playerCount = 0;
        this.lastKeyUpdate = 0;
        this.lastPosUpdate = 0;
        this.gameTime = 0;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return "GameWorld{" +
                "status=" + status +
                ", playerCount=" + playerCount +
                ", players=" + players +
                '}';
    }
}
