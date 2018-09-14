package com.chompchompfig.rockpaperscissors.domain.movestrategies;

import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;

/**
 * A Rock, Paper, Scissors MoveStrategy where the next move is fixed
 */
public class FixedMoveStrategy implements MoveStrategy {

    private Move move;

    /**
     * Creates a new MoveStrategy which will always provide the same, predetermined, next move
     * @param move <p>the Move to always provide as the next one</p>
     */
    public FixedMoveStrategy(Move move) {
        this.move = move;
    }

    /**
     * @see MoveStrategy#getNextMove()
     * @return <p>the fixed Move to be used next</p>
     */
    @Override
    public Move getNextMove() {
        return move;
    }

}
