package com.chompchompfig.rockpaperscissors.utils;

import com.chompchompfig.rockpaperscissors.domain.Game.GameSummary;
import com.chompchompfig.rockpaperscissors.domain.Game.Iterations;
import com.chompchompfig.rockpaperscissors.domain.GamePlay;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;
import com.chompchompfig.rockpaperscissors.domain.Player;
import com.chompchompfig.rockpaperscissors.domain.Player.PlayerName;
import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;
import com.chompchompfig.rockpaperscissors.domain.movestrategies.FixedMoveStrategy;
import com.chompchompfig.rockpaperscissors.infrastructure.movestrategy.RemoteNextMoveResource;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixtureFactory {

    public static final MoveStrategy FIXED_ROCK_MOVE_STRATEGY = new FixedMoveStrategy(ClassicMoves.ROCK_MOVE);
    public static final MoveStrategy FIXED_PAPER_MOVE_STRATEGY = new FixedMoveStrategy(ClassicMoves.PAPER_MOVE);
    public static final MoveStrategy FIXED_SCISSORS_MOVE_STRATEGY = new FixedMoveStrategy(ClassicMoves.SCISSORS_MOVE);

    public static final String VALID_PLAYER_NAME_ONE = "ONE";
    public static final String VALID_PLAYER_NAME_TWO = "TWO";
    public static final String INVALID_PLAYER_NAME_NULL_VALUE = null;
    public static final PlayerName INVALID_NULL_PLAYER_NAME = null;

    public static final Player VALID_PLAYER_ONE = newRockMovingPlayer(VALID_PLAYER_NAME_ONE);
    public static final Player VALID_PLAYER_TWO = newRockMovingPlayer(VALID_PLAYER_NAME_TWO);
    public static final Player INVALID_PLAYER_TWO = null;

    public static final int INVALID_NEGATIVE_GAME_ITERATIONS_VALUE = -2;
    public static final int INVALID_ZERO_GAME_ITERATIONS_VALUE = 0;
    public static final int GREATER_ZERO_GAME_ITERATIONS_VALUE = 2;
    public static final Iterations GREATER_ZERO_GAME_ITERATIONS = Iterations.of(GREATER_ZERO_GAME_ITERATIONS_VALUE);
    public static final Iterations INVALID_GAME_ITERATIONS = null;


    public static PlayerName validPlayerName() {
        return validPlayerOneName();
    }

    public static PlayerName validPlayerOneName() {
        return PlayerName.of(VALID_PLAYER_NAME_ONE);
    }

    public static PlayerName validPlayerTwoName() {
        return PlayerName.of(VALID_PLAYER_NAME_TWO);
    }

    public static Player newRockMovingPlayer(String name) {
        PlayerName playerName = PlayerName.of(name);
        return Player.newPlayer(playerName, FIXED_ROCK_MOVE_STRATEGY);
    }

    public static Player newFixedMovingPlayer(String name, MoveStrategy strategy) {
        PlayerName playerName = PlayerName.of(name);
        return Player.newPlayer(playerName, strategy);
    }

    public static List<GamePlay> newAllDrawGamePlays() {
        return Lists.newArrayList(GamePlay.of(VALID_PLAYER_ONE, VALID_PLAYER_TWO),
                GamePlay.of(VALID_PLAYER_ONE, VALID_PLAYER_TWO));
    }

    public static List<GamePlay> newPlayerOneAlwaysWinsGamePlays() {
        Player alwaysMovingPaperPlayerOne = newFixedMovingPlayer(VALID_PLAYER_NAME_ONE, FIXED_PAPER_MOVE_STRATEGY);
        return Lists.newArrayList(GamePlay.of(alwaysMovingPaperPlayerOne, VALID_PLAYER_TWO),
                GamePlay.of(alwaysMovingPaperPlayerOne, VALID_PLAYER_TWO));
    }

    public static GameSummary newPlayerOneAlwaysWinsGameSummary() {
        Map<PlayerName, Long> gamesWonByPlayerName = new HashMap<>();
        gamesWonByPlayerName.put(PlayerName.of(VALID_PLAYER_NAME_ONE), Long.valueOf(2));
        GameSummary playerOneAlwaysWinsGameSummary = new GameSummary(0, gamesWonByPlayerName);
        return playerOneAlwaysWinsGameSummary;
    }

    public static List<GamePlay> newAssortedGamePlays() {
        Player alwaysMovingPaperPlayerOne = newFixedMovingPlayer(VALID_PLAYER_NAME_ONE, FIXED_PAPER_MOVE_STRATEGY);
        Player alwaysMovingScissorsPlayerOne = newFixedMovingPlayer(VALID_PLAYER_NAME_ONE, FIXED_SCISSORS_MOVE_STRATEGY);

        GamePlay playerOneWinning = GamePlay.of(alwaysMovingPaperPlayerOne, VALID_PLAYER_TWO);
        GamePlay drawGamePlay = GamePlay.of(VALID_PLAYER_ONE, VALID_PLAYER_TWO);
        GamePlay playerTwoWinning = GamePlay.of(alwaysMovingScissorsPlayerOne, VALID_PLAYER_TWO);

        return Lists.newArrayList(playerOneWinning, drawGamePlay, playerTwoWinning);
    }

    public static RemoteNextMoveResource newRemoteNextMoveResource(String moveName) {
        RemoteNextMoveResource nextMoveResource = new RemoteNextMoveResource();
        nextMoveResource.setMove(moveName);
        return nextMoveResource;
    }

}
