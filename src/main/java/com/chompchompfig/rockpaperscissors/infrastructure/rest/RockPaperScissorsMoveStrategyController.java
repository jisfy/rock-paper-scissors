package com.chompchompfig.rockpaperscissors.infrastructure.rest;

import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * A RESTful Rock Paper Scissors MoveStrategy implementation
 */
@RestController
public class RockPaperScissorsMoveStrategyController {

    public static final String API_URI = "/api";
    public static final String API_MOVE_STRATEGY_URI = API_URI + "/movestrategy/{strategyName}";
    public static final String API_MOVE_STRATEGY_NEXT_MOVE_URI = API_MOVE_STRATEGY_URI + "/nextmove";

    private static final Logger logger = LoggerFactory.getLogger(RockPaperScissorsMoveStrategyController.class);

    @Autowired
    private MoveStrategy randomMoveStrategy;
    @Value("${rockpapersicssors.api.induced.latency: 0}")
    private long inducedLatencyMilliseconds;

    /**
     * Gets the next move in the specified MoveStrategy
     * @param strategyName <p>the name of the MoveStrategy we would like to get its next move from</p>
     * @return <p>a resource representing the next move in the selected MoveStrategy</p>
     */
    @GetMapping(value = API_MOVE_STRATEGY_NEXT_MOVE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public NextMoveResource getMoveStrategyNextMove(@PathVariable String strategyName) {
        simulateInducedLatency();
        logger.debug("Selecting next move for MoveStrategy " + strategyName);
        return getMoveStrategyRandomNextMove(); // for now always defaulting to the same random strategy
    }

    /**
     * Selects the next move from a RandomMoveStrategy
     * @return <p>a NextMoveResource representing the next move in the RandomMoveStrategy</p>
     */
    private NextMoveResource getMoveStrategyRandomNextMove() {
        return new NextMoveResource(randomMoveStrategy.getNextMove());
    }

    /**
     * Simulates a slow response service, forcing it to wait for the induced latency milliseconds when above zero
     */
    private void simulateInducedLatency() {
        if (inducedLatencyMilliseconds > 0) {
            try {
                Thread.sleep(inducedLatencyMilliseconds);
            } catch (InterruptedException e) {
                logger.error("Something went wrong when simulating service latency ", e);
            }
        }
    }
}
