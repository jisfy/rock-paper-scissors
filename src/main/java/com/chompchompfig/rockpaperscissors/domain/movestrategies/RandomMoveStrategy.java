package com.chompchompfig.rockpaperscissors.domain.movestrategies;

import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;

import java.util.List;
import java.util.Random;

/**
 * A Rock, Paper, Scissors MoveStrategy where the next move is selected randomly from a collection of possible Moves
 */
public class RandomMoveStrategy implements MoveStrategy {

    public static final String MOVES_VALIDATION_ERROR_MSG = "Invalid Moves for random selection!";

    private Random random = new Random();
    private List<? extends Move> allMoves;

    /**
     * Creates a new MoveStrategy which will pick next Move randomly from a list
     * @param allMoves <p>the list of possible next Moves</p>
     */
    public RandomMoveStrategy(List<? extends Move> allMoves) {
        validateMoves(allMoves);
        this.allMoves = allMoves;
    }

    /**
     * Validates a list of possible next Moves to not be null
     * @param allMoves <p>the list of moves to validate</p>
     * @throws IllegalArgumentException <p>in case the validation failed</p>
     */
    void validateMoves(List<? extends Move> allMoves) {
        if ((allMoves == null) || allMoves.isEmpty()) {
            throw new IllegalArgumentException(MOVES_VALIDATION_ERROR_MSG);
        }
    }

    /**
     * @see MoveStrategy#getNextMove()
     * @return <p>the randomly selected Move to be used next</p>
     */
    @Override
    public Move getNextMove() {
        int nextMoveIndex = random.nextInt(allMoves.size());
        return allMoves.get(nextMoveIndex);
    }
}
