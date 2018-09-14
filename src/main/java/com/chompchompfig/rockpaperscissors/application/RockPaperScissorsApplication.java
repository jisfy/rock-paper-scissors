package com.chompchompfig.rockpaperscissors.application;

import com.chompchompfig.rockpaperscissors.domain.Game;
import com.chompchompfig.rockpaperscissors.domain.Move;
import com.chompchompfig.rockpaperscissors.domain.MoveStrategy;
import com.chompchompfig.rockpaperscissors.domain.moves.Paper;
import com.chompchompfig.rockpaperscissors.domain.moves.Rock;
import com.chompchompfig.rockpaperscissors.domain.moves.Scissors;
import com.chompchompfig.rockpaperscissors.domain.movestrategies.FixedMoveStrategy;
import com.chompchompfig.rockpaperscissors.domain.movestrategies.RandomMoveStrategy;
import com.google.common.collect.Lists;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * The Spring Boot entry point for the Rock Paper Scissors Game. This class also holds the Spring Configuration
 * that will be used for Bean creation and auto wiring
 */
@EnableCircuitBreaker
@ComponentScan({"com.chompchompfig.rockpaperscissors.application.commandline",
		"com.chompchompfig.rockpaperscissors.domain",
		"com.chompchompfig.rockpaperscissors.infrastructure"})
@SpringBootApplication
public class RockPaperScissorsApplication {

	public static final ArrayList<Move> CLASSIC_MOVES = Lists.newArrayList(new Paper(), new Rock(), new Scissors());

    public static void main(String[] args) {
		SpringApplication.run(RockPaperScissorsApplication.class, args);
	}

	@Bean("randomMoveStrategy")
	public MoveStrategy randomMoveStrategy() {
    	return new RandomMoveStrategy(CLASSIC_MOVES);
	}

	@Bean("fixedRockMoveStrategy")
	public MoveStrategy fixedRockMoveClassicStrategy() {
		return new FixedMoveStrategy(new Rock());
	}

    @Bean
	public Game classicRockPaperScissorsGame() {
        return new Game();
    }

    @Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
    	return new RestTemplate(httpRequestFactory);
	}

	@Bean
	public ClientHttpRequestFactory getClientHttpRequestFactory(
			@Value("${rockpapersicssors.api.timeout.millis: 2000}") int serviceApiResponseTimeoutMillis) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(serviceApiResponseTimeoutMillis)
				.setConnectionRequestTimeout(serviceApiResponseTimeoutMillis)
				.setSocketTimeout(serviceApiResponseTimeoutMillis).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}

}
