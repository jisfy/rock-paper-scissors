package com.chompchompfig.rockpaperscissors.domain.moves;

import com.chompchompfig.rockpaperscissors.domain.Result;
import org.junit.Test;

import static com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves.PAPER_MOVE;
import static com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves.ROCK_MOVE;
import static com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves.SCISSORS_MOVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ClassicMovesTests {

    public static final String SOME_DUMB_RANDOM_OBJECT = "Something dumb";

    @Test
    public void givenAClassicRockMoveWhenPlayedAgainstRockThenReturnsDraws() {
        assertEquals(ROCK_MOVE.playedAgainst(ROCK_MOVE), Result.DRAWS);
    }

    @Test
    public void givenAClassicRockMoveWhenPlayedAgainstPaperThenReturnsLoses() {
        assertEquals(ROCK_MOVE.playedAgainst(PAPER_MOVE), Result.LOSES);
    }

    @Test
    public void givenAClassicRockMoveWhenPlayedAgainstScissorsThenReturnsWins() {
        assertEquals(ROCK_MOVE.playedAgainst(SCISSORS_MOVE), Result.WINS);
    }

    @Test
    public void givenAClassicPaperMoveWhenPlayedAgainstPaperThenReturnsDraws() {
        assertEquals(PAPER_MOVE.playedAgainst(PAPER_MOVE), Result.DRAWS);
    }

    @Test
    public void givenAClassicPaperMoveWhenPlayedAgainstScissorsThenReturnsLoses() {
        assertEquals(PAPER_MOVE.playedAgainst(SCISSORS_MOVE), Result.LOSES);
    }

    // the playedAgainst method should be reflexive, so adding these tests to cover those scenarios
    @Test
    public void givenAClassicPaperMoveWhenPlayedAgainstRockThenReturnsWins() {
        assertEquals(PAPER_MOVE.playedAgainst(ROCK_MOVE), Result.WINS);
    }

    @Test
    public void givenAClassicScissorsMoveWhenPlayedAgainstScissorsThenReturnsDraws() {
        assertEquals(SCISSORS_MOVE.playedAgainst(SCISSORS_MOVE), Result.DRAWS);
    }

    @Test
    public void givenAClassicScissorsMoveWhenPlayedAgainstRockThenReturnsLoses() {
        assertEquals(SCISSORS_MOVE.playedAgainst(ROCK_MOVE), Result.LOSES);
    }

    @Test
    public void givenAClassicScissorsMoveWhenPlayedAgainstPaperThenReturnsWins() {
        assertEquals(SCISSORS_MOVE.playedAgainst(PAPER_MOVE), Result.WINS);
    }

    @Test
    public void givenAClassicScissorsMoveWhenEqualsAnotherScissorsThenReturnsTrue() {
        assertEquals(SCISSORS_MOVE, new Scissors());
    }

    @Test
    public void givenAClassicScissorsMoveWhenEqualsAnythingOtherThanScissorsThenReturnsFalse() {
        givenAClassicMoveWhenEqualsAnythingOtherThanOriginalThenReturnsFalse(SCISSORS_MOVE, new Rock(), new Paper());
    }

    @Test
    public void givenAClassicRockMoveWhenEqualsAnotherRockThenReturnsTrue() {
        assertEquals(ROCK_MOVE, new Rock());
    }

    @Test
    public void givenAClassicRockMoveWhenEqualsAnythingOtherThanRockThenReturnsFalse() {
        givenAClassicMoveWhenEqualsAnythingOtherThanOriginalThenReturnsFalse(ROCK_MOVE, new Scissors(), new Paper());
    }

    @Test
    public void givenAClassicPaperMoveWhenEqualsAnotherPaperThenReturnsTrue() {
        assertEquals(PAPER_MOVE, new Paper());
    }

    @Test
    public void givenAClassicPaperMoveWhenEqualsAnythingOtherThanPaperThenReturnsFalse() {
        givenAClassicMoveWhenEqualsAnythingOtherThanOriginalThenReturnsFalse(PAPER_MOVE, new Scissors(), new Rock());
    }

    private void givenAClassicMoveWhenEqualsAnythingOtherThanOriginalThenReturnsFalse(
            ClassicMove move, ClassicMove otherMove, ClassicMove yetAnotherMove) {
        assertNotEquals(move, otherMove);
        assertNotEquals(move, yetAnotherMove);
        assertNotEquals(move, SOME_DUMB_RANDOM_OBJECT);
    }

}
