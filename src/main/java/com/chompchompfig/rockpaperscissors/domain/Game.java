package com.chompchompfig.rockpaperscissors.domain;

import com.google.auto.value.AutoValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A Rock, Paper, Scissors Game
 */
public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    /**
     * Plays the Rock, Paper, Scissors game for the given number of iterations a players
     * @param iterations <p>the number of Iterations to play</p>
     * @param playerOne <p>the first player playing the game</p>
     * @param playerTwo <p>the second player playing the game</p>
     * @return <p>a collection of GamePlays performed by the given players, whose size should match the number of
     * iterations given</p>
     */
    public Collection<GamePlay> play(@NotNull Iterations iterations, @NotNull Player playerOne,
                                     @NotNull Player playerTwo) {
        List<GamePlay> gamePlays = new ArrayList<>();
        IntStream.range(0, iterations.getValue()).forEach(t -> gamePlays.add(play(playerOne, playerTwo)));
        return gamePlays;
    }

    /**
     * Plays the Rock, Paper, Scissors game with the given players. Just one iteration
     * @param playerOne <p>the first player playing the game</p>
     * @param playerTwo <p>the second player playing the game</p>
     * @return <p>a GamePlay performed by the given players</p>
     */
    public GamePlay play(@NotNull Player playerOne, @NotNull Player playerTwo) {
        GamePlay gamePlay = GamePlay.of(playerOne, playerTwo);
        logger.debug("Playing " + gamePlay);
        return gamePlay;
    }

    /**
     * Creates a GameSummary from a collection of GamePlays. The GameSummary typically contains the total number
     * of plays won by each player, and also the number of plays drawn.
     * @param gamePlays <p>the collection of GamePlays we would like to summarize</p>
     * @return <p>a summarized representation of the given GamePlays</p>
     */
    public GameSummary summarize(@NotNull Collection<GamePlay> gamePlays) {
        Map<Boolean, List<GamePlay>> gamePlayByIsDraw = partitionGamePlaysByDrawResult(gamePlays);
        long numberOfDrawGamePlays = gamePlayByIsDraw.get(true).stream().count();
        Map<Player.PlayerName, Long> gamePlaysWonByPlayerName = getGamePlaysWonByPlayerName(gamePlayByIsDraw.get(false));
        return new GameSummary(numberOfDrawGamePlays, gamePlaysWonByPlayerName);
    }

    /**
     * Partitions the given game plays according to their result, whether it is DRAW or not
     * @param gamePlays <p>the collection of game plays to partition</p>
     * @return <p>a Map containing the given game plays partitioned by the Result.isDraw predicate</p>
     */
    private Map<Boolean, List<GamePlay>> partitionGamePlaysByDrawResult(@NotNull Collection<GamePlay> gamePlays) {
        return gamePlays.stream().collect(Collectors.partitioningBy(GamePlay::isDraw));
    }

    /**
     * Gets the number of plays won by each player, given a collection of game plays. Notice that this collection
     * can not contain DRAW plays. This is, they should all have a winner
     * @param gamePlaysWithWinner <p>the collection of game plays with an effective winner, from which to calculate the
     *                            total games won</p>
     * @return <p>a Map containing the number of plays from the given list won by each player</p>
     */
    private Map<Player.PlayerName, Long> getGamePlaysWonByPlayerName(List<GamePlay> gamePlaysWithWinner) {
        return gamePlaysWithWinner.stream().collect(
                Collectors.groupingBy(g -> g.getWinner().get().getPlayerName(), Collectors.counting()));
    }

    /**
     * A Value Object representing the number of iterations a Game can be played
     */
    @AutoValue
    public static abstract class Iterations {

        public static final String ITERATIONS_VALIDATION_ERROR_MSG = "Iterations can't be less or equal than zero";

        public abstract  int getValue();

        /**
         * Creates a new valid Iteration with the given actual number of iterations. Notice that this factory method
         * will always provide valid Iteration instances. This is, Iterations that represent valid numbers used to
         * play Games
         * @param iterations <p>the actual number of iterations</p>
         * @return <p>a new Iterations instance matching the given actual iterations value</p>
         */
        public static Iterations of(int iterations) {
            validateIterations(iterations);
            return new AutoValue_Game_Iterations(iterations);
        }

        /**
         * Validates the given number of iterations
         * @param iterations <p>the number of actual iterations to validate</p>
         * @throws IllegalArgumentException <p>in case the number of iterations is equal or below zero</p>
         */
        private static void validateIterations(int iterations) {
            if (iterations <= 0) {
                throw new IllegalArgumentException(ITERATIONS_VALIDATION_ERROR_MSG);
            }
        }

    }

    /**
     * A Game report for multiple GamePlays, with total games won and drawn
     */
    public static class GameSummary {

        public static final String GAME_SUMMARY_TO_STRING_PATTERN = "GameSummary '{' {0} '}'";
        public static final String GAME_SUMMARY_ENTRY_DELIMITER = ", ";
        public static final String GAME_SUMMARY_ENTRY_VALUE_SEPARATOR = " : ";
        public static final String PLAYER_NAME_NUMBER_WON_SUBSTRING = " won : ";

        private long drawGamePlays;
        private Map<Player.PlayerName, Long> gamePlaysWonByPlayerName;

        /**
         * Creates a new instance with the given number of games drawn and won by the different players
         * @param drawGamePlays <p>the number of game plays drawn</p>
         * @param gamePlaysWonByPlayerName <p>a Map with the number of game plays won by each player</p>
         */
        public GameSummary(long drawGamePlays, Map<Player.PlayerName, Long> gamePlaysWonByPlayerName) {
            this.drawGamePlays = drawGamePlays;
            this.gamePlaysWonByPlayerName = gamePlaysWonByPlayerName;
        }

        public long getDrawGamePlays() {
            return drawGamePlays;
        }

        public Map<Player.PlayerName, Long> getGamePlaysWonByPlayerName() {
            return gamePlaysWonByPlayerName;
        }

        @Override
        public String toString() {
            String playsDrawString = Result.DRAWS + GAME_SUMMARY_ENTRY_VALUE_SEPARATOR + drawGamePlays;
            String playsWonByPlayerNamesString =
                    gamePlaysWonByPlayerName.entrySet().stream().map(
                        gp -> formatStringForWinnerGamePlays(gp.getKey(), gp.getValue())).
                            collect(Collectors.joining(GAME_SUMMARY_ENTRY_DELIMITER));
            String gameSummaryFormattedContent =
                    Stream.of(playsDrawString, playsWonByPlayerNamesString).filter(s -> !s.isEmpty()).
                            collect(Collectors.joining(GAME_SUMMARY_ENTRY_DELIMITER));
            return MessageFormat.format(GAME_SUMMARY_TO_STRING_PATTERN, gameSummaryFormattedContent);
        }

        /**
         * Creates a nicely formatted string reporting the number of game plays won by a given player
         * @param playerName <p>the effective winner of the given game plays</p>
         * @param numberOfGamePlaysWon <p>the number of game plays won by the player</p>
         * @return <p>a nicely formatted string with the total number of plays won by a player</p>
         */
        private String formatStringForWinnerGamePlays(Player.PlayerName playerName, long numberOfGamePlaysWon) {
            return playerName + PLAYER_NAME_NUMBER_WON_SUBSTRING + numberOfGamePlaysWon;
        }
    }

}
