package com.chompchompfig.rockpaperscissors.infrastructure.rest;

import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;
import com.chompchompfig.rockpaperscissors.domain.movestrategies.RandomMoveStrategy;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ComponentScan("com.chompchompfig.rockpaperscissors.infrastructure.rest")
@Configuration
public class RestContextConfiguration {

    @Bean
    public RandomMoveStrategy randomMoveClassicStrategy() {
        return new RandomMoveStrategy(ClassicMoves.ALL);
    }

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }

}

