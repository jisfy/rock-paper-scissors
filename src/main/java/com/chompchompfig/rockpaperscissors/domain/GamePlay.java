package com.chompchompfig.rockpaperscissors.domain;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * A Rock, Paper, Scissors game play. This is, a 'turn' of that game, where two different players performed their
 * corresponding moves, producing a game Result
 */
public class GamePlay {

    public static final String GAME_PLAY_FORMATTING_STRING = "GamePlay'{' {0}, Move[{1}]:{2} - Move[{3}]:{4} '}'";

    private Player playerOne;
    private Player playerTwo;

    private Move playerOneMove;
    private Move playerTwoMove;

    /**
     * Creates a new instance of a GamePlay given two Players. A GamePlay creation requires players perform
     * their corresponding moves
     * @param playerOne <p>the first player part of this game play</p>
     * @param playerTwo <p>the second player part of this game play</p>
     */
    private GamePlay(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneMove = playerOne.performMove();
        this.playerTwoMove = playerTwo.performMove();
    }

    /**
     * Gets the first player in this GamePlay
     * @return <p>the player one</p>
     */
    public Move getPlayerOneMove() {
        return playerOneMove;
    }

    /**
     * Gets the second player in this GamePlay
     * @return <p>the player two</p>
     */
    public Move getPlayerTwoMove() {
        return playerTwoMove;
    }

    /**
     * Gets the Result of this GamePlay for the first player, obviously considering his move
     */
    public Result getPlayerOneResult() {
        return getResult();
    }

    /**
     * Gets the Result of this GamePlay for the second player, obviously considering his move
     */
    public Result getPlayerTwoResult() {
        return getResult().opposite();
    }

    /**
     * Gets whether the GamePlay ended in a draw result
     * @return <ul><li>True if the GamePlay is draw</li><li>False otherwise</li></ul>
     */
    public boolean isDraw() {
        return getResult().equals(Result.DRAWS);
    }

    /**
     * Gets the result of the GamePlay considering the different players' moves
     */
    private Result getResult() {
        return playerOneMove.playedAgainst(playerTwoMove);
    }

    /**
     * Gets the winner of the GamePlay if there is one. Notice that if the GamePlay is draw, then there will be no
     * actual winner
     * @return <p>the winner of the GamePlay if not draw. Empty otherwise</p>
     */
    Optional<Player> getWinner() {
        Player winner = null;
        if (!getResult().equals(Result.DRAWS)) {
            winner = playerTwo;
            if (getResult().equals(Result.WINS)) {
                winner = playerOne;
            }
        }
        return Optional.ofNullable(winner);
    }

    @Override
    public String toString() {
        return MessageFormat.format(GAME_PLAY_FORMATTING_STRING, getResultString(),
                playerOne.getPlayerName().getValue(), getPlayerOneMove(),
                playerTwo.getPlayerName().getValue(), getPlayerTwoMove());
    }

    /**
     * Creates a nicely formatted String identifying the winner of the GamePlay
     * @param winner <p>the effective winner of the GamePlay</p>
     * @return <p>a String identifying the winner of the GamePlay</p>
     */
    private String getPlayerWinsFormattedString(Player winner) {
        return winner.toString() + " " + Result.WINS;
    }

    /**
     * Creates a nicely formatted String to report the result of the GamePlay. The String will contain either the
     * effective winner, or indicate a draw result otherwise
     * @return <p>a nicely formatted String with the Result of the GamePlay</p>
     */
    private String getResultString() {
        return getWinner().map(w -> getPlayerWinsFormattedString(w)).orElse(Result.DRAWS.name());
    }

    /**
     * Creates a new instance of a GamePlay given two players
     * @param playerOne <p>the first player to create the GamePlay with</p>
     * @param playerTwo <p>the second player to create the GamePlay with</p>
     * @return <p>a new instance of GamePlay involving the given players</p>
     */
    public static GamePlay of(@NotNull Player playerOne, @NotNull Player playerTwo) {
        return new GamePlay(playerOne, playerTwo);
    }
}
