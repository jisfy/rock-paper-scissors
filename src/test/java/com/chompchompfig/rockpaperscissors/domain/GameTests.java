package com.chompchompfig.rockpaperscissors.domain;

import com.chompchompfig.rockpaperscissors.domain.Game.GameSummary;
import com.chompchompfig.rockpaperscissors.domain.Game.Iterations;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static com.chompchompfig.rockpaperscissors.utils.FixtureFactory.*;
import static org.junit.Assert.*;

public class GameTests {

    private Game game = new Game();

    @Test(expected = NullPointerException.class)
    public void givenValidGameWhenPlayWithNullIterationsAndValidPlayersThenThrowException() {
        game.play(INVALID_GAME_ITERATIONS, VALID_PLAYER_ONE, VALID_PLAYER_TWO);
    }

    @Test
    public void givenValidGameWhenPlayWithValidIterationsAndPlayersThenPlayCalledRightNumberOfTimes() {
        Collection<GamePlay> gamePlays = game.play(GREATER_ZERO_GAME_ITERATIONS, VALID_PLAYER_ONE, VALID_PLAYER_TWO);
        assertNotNull(gamePlays);
        assertEquals(gamePlays.size(), GREATER_ZERO_GAME_ITERATIONS_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void givenValidGameWhenPlayWithGreaterZeroIterationsAndNullPlayerThenThrowException() {
        game.play(GREATER_ZERO_GAME_ITERATIONS, VALID_PLAYER_ONE, INVALID_PLAYER_TWO);
    }

    @Test(expected = NullPointerException.class)
    public void givenValidGameWhenPlayWithValidAndNullPlayerThenThrowException() {
        game.play(VALID_PLAYER_ONE, INVALID_PLAYER_TWO);
    }

    @Test
    public void givenValidGameWhenPlayWithValidPlayersThenReturnValidGamePlay() {
        GamePlay gamePlay = game.play(VALID_PLAYER_ONE, VALID_PLAYER_TWO);
        assertNotNull(gamePlay);
        assertEquals(gamePlay.getPlayerOneResult(), Result.DRAWS);
    }

    @Test(expected = NullPointerException.class)
    public void givenAnInvalidNullGamePlaysWhenSummarizeThenThrowException() {
        game.summarize(null);
    }

    @Test
    public void givenAValidEmptyGamePlaysWhenSummarizeThenReturnEmptyGameSummary() {
        givenValidGamePlaysWhenSummarizeThenReturnExpectedGameSummary(Lists.emptyList(), 0L, 0);
    }

    @Test
    public void givenAlwaysDrawGamePlaysWhenSummarizeThenReturnCorrectGameSummary() {
        List<GamePlay> allDrawGamePlays = newAllDrawGamePlays();
        givenValidGamePlaysWhenSummarizeThenReturnExpectedGameSummary(allDrawGamePlays, allDrawGamePlays.size(), 0);
    }

    @Test
    public void givenAlwaysWinsPlayerOneGamePlaysWhenSummarizeThenReturnCorrectGameSummary() {
        List<GamePlay> playerOneAlwaysWinsGamePlays = newPlayerOneAlwaysWinsGamePlays();
        GameSummary gameSummary =
                givenValidGamePlaysWhenSummarizeThenReturnExpectedGameSummary(playerOneAlwaysWinsGamePlays, 0L, 1);
        assertEquals(gameSummary.getGamePlaysWonByPlayerName().get(validPlayerOneName()),
                Long.valueOf(playerOneAlwaysWinsGamePlays.size()));
    }

    @Test
    public void givenAssortedGamePlaysWhenSummarizeªThenReturnCorrectGameSummary() {
        List<GamePlay> assortedGamePlays = newAssortedGamePlays();
        GameSummary gameSummary =
                givenValidGamePlaysWhenSummarizeThenReturnExpectedGameSummary(assortedGamePlays, 1L, 2);
        assertEquals(gameSummary.getGamePlaysWonByPlayerName().get(validPlayerOneName()), Long.valueOf(1L));
        assertEquals(gameSummary.getGamePlaysWonByPlayerName().get(validPlayerTwoName()), Long.valueOf(1L));
    }

    private GameSummary givenValidGamePlaysWhenSummarizeThenReturnExpectedGameSummary(
            List<GamePlay> validGamePlays, long expectedDrawPlays, int expectedWinnersSize) {
        GameSummary gameSummary = game.summarize(validGamePlays);
        assertNotNull(gameSummary);
        assertEquals(gameSummary.getDrawGamePlays(), expectedDrawPlays);
        assertEquals(gameSummary.getGamePlaysWonByPlayerName().size(), expectedWinnersSize);
        return gameSummary;
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeIterationsValueWhenIterationsOfThenThrowException() {
        Iterations.of(INVALID_NEGATIVE_GAME_ITERATIONS_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenZeroIterationsValueWhenIterationsOfThenThrowException() {
        Iterations.of(INVALID_ZERO_GAME_ITERATIONS_VALUE);
    }

    @Test
    public void givenPositiveIterationsValueWhenIterationsOfThenValidIterations() {
        Iterations iterations = Iterations.of(GREATER_ZERO_GAME_ITERATIONS_VALUE);
        assertNotNull(iterations);
        assertEquals(iterations.getValue(), GREATER_ZERO_GAME_ITERATIONS_VALUE);
    }

    @Test
    public void givenTwoValidIterationsWithTheSameValueWhenIterationsEqualsThenTrue() {
        Iterations anIterations = Iterations.of(GREATER_ZERO_GAME_ITERATIONS_VALUE);
        Iterations anotherIterations = Iterations.of(GREATER_ZERO_GAME_ITERATIONS_VALUE);
        assertEquals(anIterations, anotherIterations);
    }

}
