package com.chompchompfig.rockpaperscissors.domain.moves;

import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.Result;

/**
 * A Classic Rock Move in a Classic Rock, Paper, Scissors game
 */
public class Rock implements ClassicMove {

    public static final String MOVE_NAME = "Rock";

    @Override
    public Result playedAgainst(Move move) {
        return ((ClassicMove) move).playedAgainst(this).opposite();
    }

    /**
     * @see ClassicMove#playedAgainst(Rock)
     */
    @Override
    public Result playedAgainst(Rock move) {
        return Result.DRAWS;
    }

    /**
     * @see ClassicMove#playedAgainst(Paper)
     */
    @Override
    public Result playedAgainst(Paper move) {
        return Result.LOSES;
    }

    /**
     * @see ClassicMove#playedAgainst(Scissors)
     */
    @Override
    public Result playedAgainst(Scissors move) {
        return Result.WINS;
    }

    @Override
    public String toString() {
        return MOVE_NAME;
    }

    @Override
    public int hashCode() {
        return MOVE_NAME.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Rock) return true;
        return false;
    }
}
