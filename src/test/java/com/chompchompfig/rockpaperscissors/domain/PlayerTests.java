package com.chompchompfig.rockpaperscissors.domain;

import com.chompchompfig.rockpaperscissors.utils.FixtureFactory;
import org.junit.Test;

public class PlayerTests {

    public static MoveStrategy INVALID_NULL_MOVE_STRATEGY = null;

    @Test(expected = IllegalArgumentException.class)
    public void givenANullStrategyAndValidPlayerNameWhenNewPlayerThenThrowException() {
        Player.newPlayer(FixtureFactory.validPlayerName(), INVALID_NULL_MOVE_STRATEGY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAnInvalidNullPlayerNameAndAValidStrategyWhenNewPlayerThenThrowException() {
        Player.newPlayer(FixtureFactory.INVALID_NULL_PLAYER_NAME, FixtureFactory.FIXED_ROCK_MOVE_STRATEGY);
    }
}
