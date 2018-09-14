package com.chompchompfig.rockpaperscissors.infrastructure.rest;

import com.chompchompfig.rockpaperscissors.domain.Move;

/**
 * A Resource representing the Next Move in a MoveStrategy
 */
public class NextMoveResource {

    private Move nextMove;

    /**
     * Creates a new instance with the given underlying Rock, Paper, Scissors Move
     * @param nextMove <p>a Rock, Paper, Scissors Move to be represented by this resource</p>
     */
    public NextMoveResource(Move nextMove) {
        this.nextMove = nextMove;
    }

    public String getMove() {
        return nextMove.toString();
    }
}
