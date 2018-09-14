package com.chompchompfig.rockpaperscissors.domain.movestrategies;

import org.junit.Test;
import org.assertj.core.util.Lists;
import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;

import static org.junit.Assert.assertTrue;

public class RandomMoveStrategyTests {

    @Test(expected = IllegalArgumentException.class)
    public void givenAnEmptyMovesListWhenNewRandomMoveStrategyThenThrowsException() {
        new RandomMoveStrategy(Lists.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenANullMovesListWhenNewRandomMoveStrategyThenThrowsException() {
        new RandomMoveStrategy(null);
    }

    @Test
    public void givenARandomMoveStrategyWithAllMovesWhenGetNextMoveThenReturnsAValidMove() {
        RandomMoveStrategy randomMoveStrategy = new RandomMoveStrategy(ClassicMoves.ALL);
        assertTrue(ClassicMoves.ALL.contains(randomMoveStrategy.getNextMove()));
    }

}
