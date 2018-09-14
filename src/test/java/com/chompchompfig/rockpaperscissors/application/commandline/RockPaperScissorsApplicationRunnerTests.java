package com.chompchompfig.rockpaperscissors.application.commandline;

import com.chompchompfig.rockpaperscissors.domain.Game.GameSummary;
import com.chompchompfig.rockpaperscissors.domain.GamePlay;
import com.chompchompfig.rockpaperscissors.utils.FixtureFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.chompchompfig.rockpaperscissors.application.commandline.RockPaperScissorsApplicationRunner.*;
import static com.chompchompfig.rockpaperscissors.application.commandline.RockPaperScissorsApplicationRunner.GameMode.FAIR;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class RockPaperScissorsApplicationRunnerTests {

    private static final String INVALID_MODE_NAME = "Something";
    private static final String INVALID_ITERATIONS_VALUE = "121D";
    private static final String INVALID_NEGATIVE_ITERATIONS_VALUE = "-10";
    private static final int VALID_ITERATIONS_VALUE = 12;
    private static final String VALID_ITERATIONS = String.valueOf(VALID_ITERATIONS_VALUE);
    private static final String VALID_DESTINATION_FILE_NAME = "./somefilename.out";
    private static final String VALID_DESTINATION_FILE_NAME_OTHER = "./somefilename_other.out";
    private static final String INVALID_DESTINATION_FILE_NAME = "./E./" + VALID_DESTINATION_FILE_NAME;
    private static final String COMMAND_LINE_OPTION_PREFIX = "--";
    private static final String SOME_RANDOM_COMMAND_LINE_OPTION = COMMAND_LINE_OPTION_PREFIX + "somethingElse";

    private RockPaperScissorsApplicationRunner rockPaperScissorsApplicationRunner =
            new RockPaperScissorsApplicationRunner();

    @Test
    public void givenCommandLineArgumentsWithValidModeWhenGetGameModeThenReturnCorrectMode() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithMode(FAIR.name()));
        assertEquals(rockPaperScissorsApplicationRunner.getGameMode(applicationArguments), FAIR);
    }

    @Test
    public void givenCommandLineArgumentsWithValidModeButLowercaseWhenGetGameModeThenReturnCorrectMode() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithMode(FAIR.name().toLowerCase()));
        assertEquals(rockPaperScissorsApplicationRunner.getGameMode(applicationArguments), FAIR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenCommandLineArgumentsWithInvalidModeWhenGetGameModeThenThrowException() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithMode(INVALID_MODE_NAME));
        rockPaperScissorsApplicationRunner.getGameMode(applicationArguments);
    }

    @Test
    public void givenCommandLineArgumentsWithNoModeWhenGetGameModeThenReturnDefaultGameMode() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithNoMode());
        assertEquals(rockPaperScissorsApplicationRunner.getGameMode(applicationArguments), DEFAULT_GAME_MODE);
    }

    @Test
    public void givenCommandLineArgumentsWithEmptyModeWhenGetGameModeThenReturnDefaultGameMode() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithEmptyMode());
        assertEquals(rockPaperScissorsApplicationRunner.getGameMode(applicationArguments), DEFAULT_GAME_MODE);
    }

    @Test
    public void givenCommandLineArgumentsWithEmptyIterationsWhenGetGameIterationsThenReturnDefault() {
        ApplicationArguments applicationArgumentsWithEmptyIterations =
                new DefaultApplicationArguments(newCommandLineArgumentsWithEmptyIterations());
        assertEquals(
                rockPaperScissorsApplicationRunner.getGameIterations(applicationArgumentsWithEmptyIterations),
                DEFAULT_GAME_ITERATIONS);
    }

    @Test
    public void givenCommandLineArgumentsWithNoIterationsWhenGetGameIterationsThenReturnDefault() {
        ApplicationArguments applicationArgumentsWithNoIterations =
                new DefaultApplicationArguments(newCommandLineArgumentsWithNoIterations());
        assertEquals(
                rockPaperScissorsApplicationRunner.getGameIterations(applicationArgumentsWithNoIterations),
                DEFAULT_GAME_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenCommandLineArgumentsWithInvalidIterationsWhenGetGameIterationsThenThrowException() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithIterations(INVALID_ITERATIONS_VALUE));
        rockPaperScissorsApplicationRunner.getGameIterations(applicationArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenCommandLineArgumentsWithNoGreaterZeroIterationsWhenGetGameIterationsThenThrowException() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithIterations(INVALID_NEGATIVE_ITERATIONS_VALUE));
        rockPaperScissorsApplicationRunner.getGameIterations(applicationArguments);
    }

    @Test
    public void givenCommandLineArgumentsWithValidIterationsWhenGetGameIterationsThenReturnCorrectValue() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithIterations(VALID_ITERATIONS));
        assertEquals(rockPaperScissorsApplicationRunner.getGameIterations(applicationArguments).getValue(),
                VALID_ITERATIONS_VALUE);
    }

    @Test
    public void givenCommandLineArgumentsWithNoFileOptionWhenGetFileNamePrintDestinationThenReturnEmpty() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithRandomOptions());
        assertFalse(rockPaperScissorsApplicationRunner.getFileNamePrintDestination(applicationArguments).isPresent());
    }

    @Test
    public void givenCommandLineArgumentsWithEmptyFileOptionWhenGetFileNamePrintDestinationThenReturnEmpty() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithEmptyFile());
        assertFalse(rockPaperScissorsApplicationRunner.getFileNamePrintDestination(applicationArguments).isPresent());
    }

    @Test
    public void givenCommandLineArgumentsWithVariousFileOptionsWhenGetFileNamePrintDestinationThenReturnFirstFileName() {
        ApplicationArguments applicationArguments =
                new DefaultApplicationArguments(
                        newCommandLineArgumentsWithFiles(VALID_DESTINATION_FILE_NAME, VALID_DESTINATION_FILE_NAME_OTHER));
        Optional<String> fileNamePrintDestination =
                rockPaperScissorsApplicationRunner.getFileNamePrintDestination(applicationArguments);
        assertTrue(fileNamePrintDestination.isPresent());
        assertEquals(fileNamePrintDestination.get(), VALID_DESTINATION_FILE_NAME);
    }

    @Test
    public void givenValidDestinationFilenameGamePlaysAndSummaryWhenPrintToSelectedThenPrintToFile()
            throws FileNotFoundException {
        Optional<String> validDestinationFileName = Optional.of(VALID_DESTINATION_FILE_NAME);
        List<GamePlay> performedGamePlays = FixtureFactory.newPlayerOneAlwaysWinsGamePlays();
        GameSummary gameSummary = FixtureFactory.newPlayerOneAlwaysWinsGameSummary();

        RockPaperScissorsApplicationRunner spyRockPaperScissorsApplicationRunner =
                Mockito.spy(rockPaperScissorsApplicationRunner);
        doNothing().when(spyRockPaperScissorsApplicationRunner).printToFile(
                VALID_DESTINATION_FILE_NAME, performedGamePlays, gameSummary);

        spyRockPaperScissorsApplicationRunner.printToSelected(validDestinationFileName, performedGamePlays, gameSummary);

        Mockito.verify(spyRockPaperScissorsApplicationRunner).printToFile(
                VALID_DESTINATION_FILE_NAME, performedGamePlays, gameSummary);
    }

    @Test(expected = FileNotFoundException.class)
    public void givenInvalidDestinationFilenameValidGamePlaysAndSummaryWhenPrintToFileThenThrowException()
            throws FileNotFoundException {
        List<GamePlay> performedGamePlays = FixtureFactory.newPlayerOneAlwaysWinsGamePlays();
        GameSummary gameSummary = FixtureFactory.newPlayerOneAlwaysWinsGameSummary();
        rockPaperScissorsApplicationRunner.printToFile(INVALID_DESTINATION_FILE_NAME, performedGamePlays, gameSummary);
    }

    @Test
    public void givenCommandLineArgumentsWithInvalidModeWhenRunThenExitWithFailure() {
        givenInvalidCommandLineArgumentsWhenRunThenExitWithFailure(
                new DefaultApplicationArguments(newCommandLineArgumentsWithMode(INVALID_MODE_NAME)));
    }

    @Test
    public void givenCommandLineArgumentsWithInvalidIterationsWhenRunThenExitWithFailure() {
        givenInvalidCommandLineArgumentsWhenRunThenExitWithFailure(
                new DefaultApplicationArguments(newCommandLineArgumentsWithIterations(INVALID_NEGATIVE_ITERATIONS_VALUE)));
    }

    private void givenInvalidCommandLineArgumentsWhenRunThenExitWithFailure(
            ApplicationArguments applicationArgumentsWithInvalidOption) {
        RockPaperScissorsApplicationRunner spyRockPaperScissorsApplicationRunner =
                Mockito.spy(rockPaperScissorsApplicationRunner);
        doNothing().when(spyRockPaperScissorsApplicationRunner).exitApplicationWithFailure();
        spyRockPaperScissorsApplicationRunner.run(applicationArgumentsWithInvalidOption);
        Mockito.verify(spyRockPaperScissorsApplicationRunner).exitApplicationWithFailure();
    }

    @Test
    public void givenValidCommandLineArgumentsWhenRunThenPerformPlaysPrintToConsoleAndExitWithSuccess() {
        ApplicationArguments validApplicationArguments =
                new DefaultApplicationArguments(newCommandLineArgumentsWithRandomOptions());
        List<GamePlay> performedGamePlays = FixtureFactory.newPlayerOneAlwaysWinsGamePlays();
        GameSummary gameSummary = FixtureFactory.newPlayerOneAlwaysWinsGameSummary();

        RockPaperScissorsApplicationRunner spyRockPaperScissorsApplicationRunner =
                Mockito.spy(rockPaperScissorsApplicationRunner);
        doReturn(performedGamePlays).when(spyRockPaperScissorsApplicationRunner)
                .performFairPlays(DEFAULT_GAME_ITERATIONS);
        doReturn(gameSummary).when(spyRockPaperScissorsApplicationRunner).getGameSummary(performedGamePlays);
        doNothing().when(spyRockPaperScissorsApplicationRunner).printToConsole(performedGamePlays, gameSummary);
        doNothing().when(spyRockPaperScissorsApplicationRunner).exitApplicationWithSuccess();

        spyRockPaperScissorsApplicationRunner.run(validApplicationArguments);

        Mockito.verify(spyRockPaperScissorsApplicationRunner).performFairPlays(DEFAULT_GAME_ITERATIONS);
        Mockito.verify(spyRockPaperScissorsApplicationRunner).printToConsole(performedGamePlays, gameSummary);
        Mockito.verify(spyRockPaperScissorsApplicationRunner).exitApplicationWithSuccess();
    }

    @Test // some code duplication with previous method
    public void givenCommandLineArgumentsWithInvalidFileWhenRunThenExitWithFileWritingFailure() {
        ApplicationArguments applicationArgumentsWithInvalidOption =
                new DefaultApplicationArguments(newCommandLineArgumentsWithFiles(INVALID_DESTINATION_FILE_NAME));
        List<GamePlay> performedGamePlays = FixtureFactory.newPlayerOneAlwaysWinsGamePlays();
        GameSummary gameSummary = FixtureFactory.newPlayerOneAlwaysWinsGameSummary();

        RockPaperScissorsApplicationRunner spyRockPaperScissorsApplicationRunner =
                Mockito.spy(rockPaperScissorsApplicationRunner);
        doReturn(performedGamePlays).when(spyRockPaperScissorsApplicationRunner)
                .performFairPlays(DEFAULT_GAME_ITERATIONS);
        doReturn(gameSummary).when(spyRockPaperScissorsApplicationRunner).getGameSummary(performedGamePlays);
        doNothing().when(spyRockPaperScissorsApplicationRunner).exitApplicationWithFileWritingFailure(any());

        spyRockPaperScissorsApplicationRunner.run(applicationArgumentsWithInvalidOption);

        Mockito.verify(spyRockPaperScissorsApplicationRunner).performFairPlays(DEFAULT_GAME_ITERATIONS);
        Mockito.verify(spyRockPaperScissorsApplicationRunner).exitApplicationWithFileWritingFailure(any());
    }

    private String[] newCommandLineArgumentsWithMode(String modeName) {
        String gameModeOption = COMMAND_LINE_OPTION_PREFIX + GAME_MODE_OPTION_ARG_NAME + "=" + modeName;
        return new String[] { gameModeOption };
    }

    private String[] newCommandLineArgumentsWithIterations(String iterations) {
        String gameIterationsOption = COMMAND_LINE_OPTION_PREFIX + GAME_ITERATIONS_OPTION_ARG_NAME + "=" + iterations;
        return new String[] { gameIterationsOption };
    }

    private String[] newCommandLineArgumentsWithFiles(String... destinationFilenames) {
         return Arrays.stream(destinationFilenames).map(
                 d -> newCommandLineArgumentsFilesOption(d)).collect(Collectors.toList()).toArray(
                         new String[destinationFilenames.length]);
    }

    private String newCommandLineArgumentsFilesOption(String destinationFileName) {
        return COMMAND_LINE_OPTION_PREFIX + GAME_FILE_OUTPUT_OPTION_ARG_NAME + "=" + destinationFileName;
    }

    private String[] newCommandLineArgumentsWithNoIterations() {
        return newCommandLineArgumentsWithRandomOptions();
    }

    private String[] newCommandLineArgumentsWithNoMode() {
        return newCommandLineArgumentsWithRandomOptions();
    }

    private String[] newCommandLineArgumentsWithRandomOptions() {
        return new String[] {SOME_RANDOM_COMMAND_LINE_OPTION};
    }

    private String[] newCommandLineArgumentsWithEmptyMode() {
        String emptyGameModeOption = COMMAND_LINE_OPTION_PREFIX + GAME_MODE_OPTION_ARG_NAME;
        return new String[] { emptyGameModeOption };
    }

    private String[] newCommandLineArgumentsWithEmptyIterations() {
        String emptyGameIterationsOption = COMMAND_LINE_OPTION_PREFIX + GAME_ITERATIONS_OPTION_ARG_NAME;
        return new String[] { emptyGameIterationsOption };
    }

    private String[] newCommandLineArgumentsWithEmptyFile() {
        String emptyGameFileOption = COMMAND_LINE_OPTION_PREFIX + GAME_FILE_OUTPUT_OPTION_ARG_NAME;
        return new String[] { emptyGameFileOption };
    }

}
