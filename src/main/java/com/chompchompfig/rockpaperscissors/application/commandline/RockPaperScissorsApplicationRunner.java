package com.chompchompfig.rockpaperscissors.application.commandline;

import com.chompchompfig.rockpaperscissors.domain.Game;
import com.chompchompfig.rockpaperscissors.domain.GamePlay;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;
import com.chompchompfig.rockpaperscissors.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.System.exit;

/**
 * A SpringBoot based, command line Rock, Paper, Scissors Game implementation
 */
@Profile("tool")
@Component
public class RockPaperScissorsApplicationRunner implements ApplicationRunner {

    public static final GameMode DEFAULT_GAME_MODE = GameMode.FAIR;
    public static final String DEFAULT_GAME_MODE_NAME = DEFAULT_GAME_MODE.name();
    public static final int DEFAULT_GAME_ITERATIONS_VALUE = 10;
    public static final Game.Iterations DEFAULT_GAME_ITERATIONS = Game.Iterations.of(DEFAULT_GAME_ITERATIONS_VALUE);

    public static final String PLAYER_ONE_NAME = "ONE";
    public static final String PLAYER_TWO_NAME = "TWO";
    public static final Player.PlayerName VALID_PLAYER_ONE_PLAYERNAME = Player.PlayerName.of(PLAYER_ONE_NAME);
    public static final Player.PlayerName VALID_PLAYER_TWO_PLAYERNAME = Player.PlayerName.of(PLAYER_TWO_NAME);

    public static final String GAME_SUMMARY_HORIZONTAL_LINE = "----------------------------------------------------";
    public static final String GAME_MODE_OPTION_ARG_NAME = "mode";
    public static final String GAME_ITERATIONS_OPTION_ARG_NAME = "iterations";
    public static final String GAME_FILE_OUTPUT_OPTION_ARG_NAME = "file";

    public static final String ROCK_PAPER_SCISSORS_GAME_USAGE =
            "Usage : --mode=[fair, unfair, remote] --iterations=non_zero_positive_integer --file=output_filename";

    public static final String FAILED_WRITING_RESULTS_FILE_ERROR_MSG = "Error writing results to output file ";
    public static final String INVALID_ARGUMENTS_ERROR_MSG = "Invalid input arguments!!!!";

    public static final int SUCCESS_EXIT_CODE = 0;
    public static final int FAILURE_EXIT_CODE = -1;

    public enum GameMode { FAIR, UNFAIR, REMOTE};

    private static final Logger logger = LoggerFactory.getLogger(RockPaperScissorsApplicationRunner.class);

    @Qualifier("randomMoveStrategy")
    @Autowired
    private MoveStrategy randomMoveStrategy;
    @Qualifier("fixedRockMoveStrategy")
    @Autowired
    private MoveStrategy fixedRockMoveClassicStrategy;
    @Qualifier("remoteMoveStrategy")
    @Autowired
    private MoveStrategy remoteMoveStrategy;
    @Autowired
    private Game classicGame;

    @Override
    public void run(ApplicationArguments arguments) {
        try {
            GameMode selectedGameMode = getGameMode(arguments);
            Game.Iterations selectedGameIterations = getGameIterations(arguments);
            Optional<String> selectedFileNamePrintDestination = getFileNamePrintDestination(arguments);

            logger.info("Playing Rock Paper Scissors in mode " + selectedGameMode + " with iterations "
                    + selectedGameIterations + ", and printing to " + selectedFileNamePrintDestination.orElse("Console"));

            Collection<GamePlay> gamePlays = performSelectedGamePlays(selectedGameMode, selectedGameIterations);
            Game.GameSummary gameSummary = getGameSummary(gamePlays);

            printToSelected(selectedFileNamePrintDestination, gamePlays, gameSummary);
            exitApplicationWithSuccess();
        } catch(FileNotFoundException e) {
            exitApplicationWithFileWritingFailure(e);
        } catch (IllegalArgumentException e) {
            exitApplicationWithFailure();
        }
    }

    /**
     * Gets the GameSummary for the given game plays. Just added this one liner for easier testing
     */
    Game.GameSummary getGameSummary(Collection<GamePlay> gamePlays) {
        return classicGame.summarize(gamePlays);
    }

    /**
     * Performs a number of Rock, Paper, Scissors game plays in the given mode and for the given number of iterations
     * @param selectedGameMode <p>the GameMode in which to perform the game plays</p>
     * @param selectedGameIterations <p>the number of game plays to perform</p>
     * @return <p>a collection of the game plays performed</p>
     */
    Collection<GamePlay> performSelectedGamePlays(GameMode selectedGameMode, Game.Iterations selectedGameIterations) {
        Collection<GamePlay> gamePlays = new ArrayList<>();
        switch (selectedGameMode) {
            case FAIR : gamePlays = performFairPlays(selectedGameIterations); break;
            case UNFAIR : gamePlays = performUnfairPlays(selectedGameIterations); break;
            case REMOTE : gamePlays = performRemotePlays(selectedGameIterations); break;
        }
        return gamePlays;
    }

    /**
     * Prints the given game plays and summary to the destination file. In case the destination file is empty,
     * the system console will be the output destination
     * @param fileNameDestination <p>the file name destination to print the game plays and summary to</p>
     * @param gamePlays <p>the game plays to print</p>
     * @param gameSummary <p>the game summary to print</p>
     */
    void printToSelected(Optional<String> fileNameDestination, Collection<GamePlay> gamePlays, Game.GameSummary gameSummary)
            throws FileNotFoundException {
        if (fileNameDestination.isPresent()) {
            printToFile(fileNameDestination.get(), gamePlays, gameSummary);
        } else {
            printToConsole(gamePlays, gameSummary);
        }
    }

    /**
     * Prints the given game plays and summary to a file with the given filename
     * @param fileNameDestination <p>the name of the file to write the contents to</p>
     * @param gamePlays <p>the game plays to print</p>
     * @param gameSummary <p>the game summary to print</p>
     * @throws FileNotFoundException <p>if the destination file is invalid</p>
     */
    void printToFile(String fileNameDestination, Collection<GamePlay> gamePlays, Game.GameSummary gameSummary)
            throws FileNotFoundException {
        try (PrintWriter filePrintWriter = new PrintWriter(new File(fileNameDestination))) {
            printGamePlaysToFile(filePrintWriter, gamePlays);
            printGameSummaryToFile(filePrintWriter, gameSummary);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    void exitApplicationWithFileWritingFailure(FileNotFoundException e) {
        logger.error(FAILED_WRITING_RESULTS_FILE_ERROR_MSG, e);
        exit(FAILURE_EXIT_CODE);
    }

    /**
     * Prints the given game plays to the given PrintWriter
     * @param fileWriter <p>the PrintWriter where to print the contents</p>
     * @param gamePlays <p>the game plays to print</p>
     */
    void printGamePlaysToFile(PrintWriter fileWriter, Collection<GamePlay> gamePlays) {
        gamePlays.stream().forEach(gp -> fileWriter.println(gp));
    }

    /**
     * Prints the given game plays to the given PrintWriter
     * @param fileWriter <p>the PrintWriter where to print the contents</p>
     * @param gameSummary <p>the game summary to print</p>
     */
    void printGameSummaryToFile(PrintWriter fileWriter, Game.GameSummary gameSummary) {
        fileWriter.println(GAME_SUMMARY_HORIZONTAL_LINE);
        fileWriter.println(gameSummary);
    }

    /**
     * Prints the given game plays and summary to the system console
     * @param gamePlays <p>the game plays to print</p>
     * @param gameSummary <p>the game summary to print</p>
     */
    void printToConsole(Collection<GamePlay> gamePlays, Game.GameSummary gameSummary) {
        printGamePlays(gamePlays);
        printSummary(gameSummary);
    }

    /**
     * Prints the given game plays to the system console
     * @param gamePlays <p>the game plays to print</p>
     */
    void printGamePlays(Collection<GamePlay> gamePlays) {
        gamePlays.stream().forEach(gp -> logger.info(gp.toString()));
    }

    /**
     * prints a summary of the given game plays to the system console
     * @param gameSummary <p>the game summary we would like to print</p>
     */
    void printSummary(Game.GameSummary gameSummary) {
        logger.info(GAME_SUMMARY_HORIZONTAL_LINE);
        logger.info(gameSummary.toString());
    }

    /**
     * Performs the given number of game plays in FAIR mode
     * @param iterations <p>the number of game plays to perform</p>
     * @return <p>a collection of game plays performed in FAIR mode</p>
     */
    Collection<GamePlay> performFairPlays(Game.Iterations iterations) {
        Player playerOne = Player.newPlayer(VALID_PLAYER_ONE_PLAYERNAME, randomMoveStrategy);
        Player playerTwo = Player.newPlayer(VALID_PLAYER_TWO_PLAYERNAME, randomMoveStrategy);
        return classicGame.play(iterations, playerOne, playerTwo);
    }

    /**
     * Performs the given number of game plays in UNFAIR mode
     * @param iterations <p>the number of game plays to perform</p>
     * @return <p>a collection of game plays performed in UNFAIR mode</p>
     */
    Collection<GamePlay> performUnfairPlays(Game.Iterations iterations) {
        Player playerOne = Player.newPlayer(VALID_PLAYER_ONE_PLAYERNAME, randomMoveStrategy);
        Player playerTwo = Player.newPlayer(VALID_PLAYER_TWO_PLAYERNAME, fixedRockMoveClassicStrategy);
        return classicGame.play(iterations, playerOne, playerTwo);
    }

    /**
     * Performs the given number of game plays in REMOTE mode
     * @param iterations <p>the number of game plays to perform</p>
     * @return <p>a collection of game plays performed in REMOTE mode</p>
     */
    Collection<GamePlay> performRemotePlays(Game.Iterations iterations) {
        Player playerOne = Player.newPlayer(VALID_PLAYER_ONE_PLAYERNAME, randomMoveStrategy);
        Player playerTwo = Player.newPlayer(VALID_PLAYER_TWO_PLAYERNAME, remoteMoveStrategy);
        return classicGame.play(iterations, playerOne, playerTwo);
    }

    /**
     * Gets the file name print destination from the command line. In case there is no file argument in the command
     * line, sensible defaults will be used.
     * @param arguments <p>the command line arguments to get the file name print destination from</p>
     * @return <p>the file name destination or empty if the file option was missing</p>
     */
    Optional<String> getFileNamePrintDestination(ApplicationArguments arguments) {
        Optional<List<String>> fileNamePrintDestinationOptions =
                Optional.ofNullable(arguments.getOptionValues(GAME_FILE_OUTPUT_OPTION_ARG_NAME));
        Optional<String> fileNamePrintDestination = fileNamePrintDestinationOptions.flatMap(g -> g.stream().findFirst());
        return fileNamePrintDestination;
    }

    /**
     * Gets the GameMode from the given command line arguments. Argument validation is also performed. In case there
     * is no matching argument in the command line, a sensible default is provided
     * @param arguments <p>the command line arguments to get the GameMode from</p>
     * @return <p>the GameMode parsed from the command line arguments</p>
     */
    GameMode getGameMode(ApplicationArguments arguments) {
        Optional<List<String>> gameModeOptions =
                Optional.ofNullable(arguments.getOptionValues(GAME_MODE_OPTION_ARG_NAME));
        Optional<String> gameModeName = gameModeOptions.flatMap(g -> g.stream().findFirst());
        return gameModeName.map(gmn -> GameMode.valueOf(gmn.toUpperCase())).orElse(DEFAULT_GAME_MODE);
    }

    /**
     * Gets the Iterations from the given command line arguments. Argument validation is also performed. In case there
     * is no matching argument in the command line, a sensible default is provided
     * @param arguments <p>the command line arguments to get the Iterations from</p>
     * @return <p>the Iterations parsed from the command line arguments</p>
     */
    Game.Iterations getGameIterations(ApplicationArguments arguments) {
        Optional<List<String>> gameIterationsOptions =
                Optional.ofNullable(arguments.getOptionValues(GAME_ITERATIONS_OPTION_ARG_NAME));
        Optional<String> gameIterations = gameIterationsOptions.flatMap(g -> g.stream().findFirst());
        int correctedGameIterations = gameIterations.map(gi -> Integer.parseInt(gi)).orElse(DEFAULT_GAME_ITERATIONS_VALUE);
        return Game.Iterations.of(correctedGameIterations);
    }

    void exitApplicationWithSuccess() {
        exit(SUCCESS_EXIT_CODE);
    }

    void exitApplicationWithFailure() {
        logger.error(INVALID_ARGUMENTS_ERROR_MSG);
        logger.error(ROCK_PAPER_SCISSORS_GAME_USAGE);
        exit(FAILURE_EXIT_CODE);
    }
}
