package com.chompchompfig.rockpaperscissors.domain;

import com.chompchompfig.rockpaperscissors.domain.Player.PlayerName;
import com.chompchompfig.rockpaperscissors.utils.FixtureFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerNameTests {

    @Test
    public void givenValidPlayerNamesWithEqualValuesWhenEqualsThenReturnTrue() {
        PlayerName aPlayerName = PlayerName.of(FixtureFactory.VALID_PLAYER_NAME_ONE);
        PlayerName anotherPlayerName = PlayerName.of(FixtureFactory.VALID_PLAYER_NAME_ONE);
        assertEquals(aPlayerName, anotherPlayerName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAnInvalidPlayerNameValueWhenOfThenThrowException() {
        PlayerName.of(FixtureFactory.INVALID_PLAYER_NAME_NULL_VALUE);
    }
}
