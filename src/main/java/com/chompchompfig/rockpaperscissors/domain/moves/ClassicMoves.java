package com.chompchompfig.rockpaperscissors.domain.moves;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A ClassicMove Factory
 */
public class ClassicMoves {

    public static final ClassicMove ROCK_MOVE = new Rock();
    public static final ClassicMove PAPER_MOVE = new Paper();
    public static final ClassicMove SCISSORS_MOVE = new Scissors();
    public static final List<ClassicMove> ALL = Lists.newArrayList(ROCK_MOVE, PAPER_MOVE, SCISSORS_MOVE);

    /**
     * Gets a ClassMove given its name
     * @param moveName <p>the Move name</p>
     * @return <p>a ClassicMove instance for the given Move name</p>
     */
    public static ClassicMove from(String moveName) {
        // this is a rather quick and dirty implementation, but it is just three if statements, and shouldn't grow
        // beyond that, so I am leaving this for now
        if (Rock.MOVE_NAME.equalsIgnoreCase(moveName)) {
            return ROCK_MOVE;
        }
        if (Paper.MOVE_NAME.equalsIgnoreCase(moveName)) {
            return PAPER_MOVE;
        }
        if (Scissors.MOVE_NAME.equalsIgnoreCase(moveName)) {
            return SCISSORS_MOVE;
        }
        throw new IllegalArgumentException("Can't create a ClassicMove from " + moveName);
    }

}
