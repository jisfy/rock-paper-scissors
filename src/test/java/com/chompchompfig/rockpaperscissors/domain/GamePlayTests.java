package com.chompchompfig.rockpaperscissors.domain;

import org.junit.Test;

import java.util.Optional;

import static com.chompchompfig.rockpaperscissors.utils.FixtureFactory.*;
import static org.junit.Assert.*;

public class GamePlayTests {

    @Test(expected = NullPointerException.class)
    public void givenAValidAndANullPlayerWhenOfThenThrowException() {
        GamePlay.of(VALID_PLAYER_ONE, INVALID_PLAYER_TWO);
    }

    @Test
    public void givenAGamePlayWithTwoValidRockMovingPlayersWhenGetWinnerThenReturnEmpty() {
        GamePlay doubleRockMoveGamePlay = GamePlay.of(VALID_PLAYER_ONE, VALID_PLAYER_TWO);
        Optional<Player> winner = doubleRockMoveGamePlay.getWinner();
        assertNotNull(winner);
        assertFalse(winner.isPresent());
    }

    @Test
    public void givenAGamePlayWithRockAndPaperMovingPlayersWhenGetWinnerThenReturnPlayerTwo() {
        Player paperMovingPlayer = newFixedMovingPlayer(VALID_PLAYER_NAME_TWO, FIXED_PAPER_MOVE_STRATEGY);
        givenAGamePlayWithFixedMovingPlayersWhenGetWinnerThenReturnExpectedPlayer(
                VALID_PLAYER_ONE, paperMovingPlayer, paperMovingPlayer);
    }

    @Test
    public void givenAGamePlayWithRockAndScissorsMovingPlayersWhenGetWinnerThenReturnPlayerOne() {
        Player paperMovingPlayer = newFixedMovingPlayer(VALID_PLAYER_NAME_TWO, FIXED_SCISSORS_MOVE_STRATEGY);
        givenAGamePlayWithFixedMovingPlayersWhenGetWinnerThenReturnExpectedPlayer(
                VALID_PLAYER_ONE, paperMovingPlayer, VALID_PLAYER_ONE);
    }

    private void givenAGamePlayWithFixedMovingPlayersWhenGetWinnerThenReturnExpectedPlayer(
            Player playerOne, Player playerTwo, Player expectedWinner) {
        GamePlay fixedMoveGamePlay = GamePlay.of(playerOne, playerTwo);
        Optional<Player> winner = fixedMoveGamePlay.getWinner();
        assertNotNull(winner);
        assertTrue(winner.isPresent());
        assertEquals(winner.get(), expectedWinner);
    }
}
