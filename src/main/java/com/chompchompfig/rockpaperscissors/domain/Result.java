package com.chompchompfig.rockpaperscissors.domain;

/**
 * The Result of playing a Rock, Paper, Scissors game, or one of its variations
 */
public enum Result {
    DRAWS, WINS, LOSES;

    /**
     * @return <p>the opposite result of this one, unless it is DRAW</p>
     */
    public Result opposite() {
        switch (this) {
            case LOSES : return WINS;
            case WINS : return LOSES;
            default: return this;
        }
    }
}
