package com.chompchompfig.rockpaperscissors.domain;

/**
 * A strategy or criterion used to select the next move in a Rock, Paper, Scissors game.
 */
public interface MoveStrategy {

    /**
     * Gets the next Move to perform in a Rock, Paper, Scissors game
     * @return <p>the next selected Move</p>
     */
    Move getNextMove();

}
