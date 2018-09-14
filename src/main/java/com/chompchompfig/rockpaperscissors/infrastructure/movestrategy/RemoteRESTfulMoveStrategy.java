package com.chompchompfig.rockpaperscissors.infrastructure.movestrategy;

import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;
import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * A Rock, Paper, Scissors MoveStrategy where the next move is fetched from a remote RESTful endpoint.
 * This implementation tries to conform to the MoveStrategy interface as much as possible and always provide a next
 * Move, no matter the status of the remote endpoint. Therefore, there is a configurable fallback Move that it will
 * use in case of failure. Notice that this implementation relies on Spring RestTemplate, whose timeout values should
 * be properly configured beforehand. The class will use whatever RestTemplate is provided and will make no changes
 * to the timeout values whatsoever
 */
@Profile("tool")
@Component("remoteMoveStrategy")
public class RemoteRESTfulMoveStrategy implements MoveStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RemoteRESTfulMoveStrategy.class);

    private RestTemplate restTemplate;
    private URI remoteApiMoveStrategyServiceUrl;
    private Move fallbackMove;

    /**
     * Creates a new instance of a remote restful MoveStrategy, with the given fallback ClassicMove and accessing
     * the given remote restful endpoint
     * @param restTemplate <p>the RestTemplate to use to access the remote endpoint</p>
     * @param remoteApiMoveStrategyServiceUrlString <p>the URI string where the remote service providing the next
     *                                              move is located</p>
     * @param fallbackMoveName <p>the name of the ClassicMove to use as a fallback in case the remote service fails
     *                         or is experiencing problems (slow response)</p>
     */
    public RemoteRESTfulMoveStrategy(@Autowired RestTemplate restTemplate,
            @Value("${rockpaperscissors.api.moves.url}") String remoteApiMoveStrategyServiceUrlString,
            @Value("${rockpaperscissors.api.moves.fallback.name:Rock}") String fallbackMoveName) {
        this.restTemplate = restTemplate;
        this.remoteApiMoveStrategyServiceUrl = URI.create(remoteApiMoveStrategyServiceUrlString);
        this.fallbackMove = ClassicMoves.from(fallbackMoveName);
    }

    /**
     * @see MoveStrategy#getNextMove()
     * @return <p>the selected next Move, fetched from a remote RESTful endpoint, or a fallback in case the service
     * is unavailable</p>
     */
    @HystrixCommand(fallbackMethod = "getFallbackNextMove")
    @Override
    public Move getNextMove() {
        logger.debug("Accessing the remote api at " + remoteApiMoveStrategyServiceUrl);

        RemoteNextMoveResource remoteNextMoveResource =
                this.restTemplate.getForObject(remoteApiMoveStrategyServiceUrl, RemoteNextMoveResource.class);

        logger.debug("Received from the API " + remoteNextMoveResource.getMove());
        return ClassicMoves.from(remoteNextMoveResource.getMove());
    }

    /**
     * @return <p>the next Move to use as a fallback mechanism</p>
     */
    private Move getFallbackNextMove() {
        logger.debug("Falling back to " + fallbackMove);
        return fallbackMove;
    }


}
