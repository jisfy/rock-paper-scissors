package com.chompchompfig.rockpaperscissors.infrastructure.movestrategy;

import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;
import com.chompchompfig.rockpaperscissors.domain.moves.Paper;
import com.chompchompfig.rockpaperscissors.domain.moves.Rock;
import com.chompchompfig.rockpaperscissors.infrastructure.rest.RestContextConfiguration;
import com.chompchompfig.rockpaperscissors.utils.FixtureFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@EnableCircuitBreaker
@EnableAspectJAutoProxy
@ContextConfiguration(classes=RestContextConfiguration.class)
public class RemoteRESTfulMoveStrategyTests {

    private static final String VALID_FALLBACK_MOVE_NAME = Paper.MOVE_NAME;
    private static final String VALID_RESPONSE_MOVE_NAME = Rock.MOVE_NAME;
    private static final String VALID_RANDOM_MOVE_STRATEGY_NEXT_MOVE_API_URI_STRING =
            "http://localhost:8080/api/movestrategy/random/nextmove";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void givenValidRemoteRESTfulMoveStrategyAndServiceUpWhenGetNextThenReturnCorrectNextMove() {
        setUpRestTemplate(VALID_RANDOM_MOVE_STRATEGY_NEXT_MOVE_API_URI_STRING, VALID_RESPONSE_MOVE_NAME);
        RemoteRESTfulMoveStrategy remoteRESTfulMoveStrategy =
                new RemoteRESTfulMoveStrategy(restTemplate, VALID_RANDOM_MOVE_STRATEGY_NEXT_MOVE_API_URI_STRING,
                        VALID_FALLBACK_MOVE_NAME);
        Move nextMove = remoteRESTfulMoveStrategy.getNextMove();
        assertNotNull(nextMove);
        assertEquals(ClassicMoves.ROCK_MOVE, nextMove);
    }

    private void setUpRestTemplate(String randomMoveStragtegyNextMoveApiUriString,
                                           String nextMoveResourceName) {
        URI randomMoveStrategyNextMoveApiUri = URI.create(randomMoveStragtegyNextMoveApiUriString);
        RemoteNextMoveResource nextMoveResource = FixtureFactory.newRemoteNextMoveResource(nextMoveResourceName);
        doReturn(nextMoveResource).when(restTemplate).getForObject(
                randomMoveStrategyNextMoveApiUri, RemoteNextMoveResource.class);
    }

}
