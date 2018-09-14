package com.chompchompfig.rockpaperscissors.domain;

/**
 * A Rock, Paper, Scissors game Move. This is the base interface of all Moves, and could be further extended by exotic
 * variations of the classic Rock, Paper, Scissors game
 */
public interface Move {

    /**
     * Gets the Result of a game play, should this Move be played against a different one
     * @param move <p>a different Move this one would be played against</p>
     * @return <p>the Result of a game, should this Move be played against the given one</p>
     */
    Result playedAgainst(Move move);

}
