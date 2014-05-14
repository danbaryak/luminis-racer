package com.danbar.luminis;

import com.danbar.luminis.data.GameState;
import com.danbar.luminis.data.Player;
import com.danbar.luminis.data.PlayerCommand;
import com.danbar.luminis.data.PlayerCommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring MVC simple REST controller used for client communication with the game server.
 */
@Controller("/")
public class GameController  {

    @Autowired
    private GameWorld game;

    // thread local game state to prevent creating a new one for each request
    ThreadLocal<GameState> gameState = new ThreadLocal<>();
    ThreadLocal<PlayerCommandResult> commandResult = new ThreadLocal<>();

    public GameController() {

    }

    @RequestMapping("command")
    @ResponseBody
    public PlayerCommandResult handlePlayerCommand(@RequestParam("name") String command, @RequestParam("id") int playerId) {
        boolean accepted =  game.handlePlayerCommand(playerId, PlayerCommand.valueOf(command));
        if (commandResult.get() == null) {
            commandResult.set(new PlayerCommandResult());
        }
        commandResult.get().setAccepted(accepted);
        return commandResult.get();
    }

    /**
     * Called by the client to retrieve the updated game state.
     *
     * @return The current game state.
     */
    @RequestMapping("state")
    @ResponseBody
    public GameState getGameState() {
        if (gameState.get() == null) {
            gameState.set(new GameState());
        }
        game.fillGameState(gameState.get());
        return gameState.get();
    }

    @RequestMapping("join")
    @ResponseBody
    public Player joinGame(@RequestParam("name") String playerName) {
        return game.join(playerName);
    }

    public GameWorld getGame() {
        return game;
    }

    public void setGame(GameWorld game) {
        this.game = game;
    }
}