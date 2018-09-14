package com.chompchompfig.rockpaperscissors.domain;

import com.google.auto.value.AutoValue;

/**
 * A Player of a Game, able to perform moves or play
 */
public class Player {

    public static final String INVALID_MOVE_STRATEGY_FOR_PLAYER_MSG = "Invalid move strategy for player";
    public static final String INVALID_PLAYER_NAME_FOR_PLAYER_MSG = "Invalid PlayerName for player";

    private PlayerName playerName;
    private MoveStrategy moveStrategy;

    /**
     * Creates a new instance of a Player
     * @param playerName <p>the player name</p>
     * @param moveStrategy <p>the strategy used by the player to perform moves in a game</p>
     */
    private Player(PlayerName playerName, MoveStrategy moveStrategy) {
        validatePlayerName(playerName);
        validateMoveStrategy(moveStrategy);
        this.playerName = playerName;
        this.moveStrategy = moveStrategy;
    }

    /**
     * Validates a PlayerName
     * @param playerName <p>the PlayerName instance to validate</p>
     * @throws IllegalArgumentException <p>in case the validation failed</p>
     */
    private void validatePlayerName(PlayerName playerName) {
        if (playerName == null) {
            throw new IllegalArgumentException(INVALID_PLAYER_NAME_FOR_PLAYER_MSG);
        }
    }

    /**
     * Validates a MoveStrategy
     * @param moveStrategy <p>the MoveStrategy to validate</p>
     * @throws IllegalArgumentException <p>in case the validation failed</p>
     */
    private void validateMoveStrategy(MoveStrategy moveStrategy) {
        if (moveStrategy == null) {
            throw new IllegalArgumentException(INVALID_MOVE_STRATEGY_FOR_PLAYER_MSG);
        }
    }

    public PlayerName getPlayerName() {
        return playerName;
    }

    public Move performMove() {
        return moveStrategy.getNextMove();
    }

    @Override
    public String toString() {
        return "Player {" + getPlayerName().getValue() + "}";
    }

    /**
     * Creates a new instance of a Player
     * @param playerName <p>the player name of the new Player</p>
     * @param moveStrategy <p>the strategy the player will use to play</p>
     * @return <p>a new instance of Player wirh the given name and strategy</p>
     */
    public static Player newPlayer(PlayerName playerName, MoveStrategy moveStrategy) {
        return new Player(playerName, moveStrategy);
    }

    /**
     * A Value Object representing a Player's name
     */
    @AutoValue
    public static abstract class PlayerName {

        public static final String INVALID_NAME_FOR_PLAYER_NAME_MSG = "Invalid playerName for PlayerName";

        /**
         * @return <p>the effective name of the player</p>
         */
        public abstract String getValue();

        /**
         * Creates a new instance of a PlayerName
         * @param name <p>the actual name of the player</p>
         * @return <p>a new instance of a PlayerName with the given effective name</p>
         */
        public static PlayerName of(String name) {
            validateName(name);
            return new AutoValue_Player_PlayerName(name);
        }

        /**
         * Validates the given name. Valid names can't be null
         * @param name <p>the name to validate</p>
         * @throws IllegalArgumentException <p>in case the validation failed</p>
         */
        private static void validateName(String name) {
            if (name == null) {
                throw new IllegalArgumentException(INVALID_NAME_FOR_PLAYER_NAME_MSG);
            }
        }
    }

}
