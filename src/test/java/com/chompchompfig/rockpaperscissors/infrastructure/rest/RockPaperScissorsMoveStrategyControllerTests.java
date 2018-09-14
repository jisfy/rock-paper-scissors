package com.chompchompfig.rockpaperscissors.infrastructure.rest;

import com.chompchompfig.rockpaperscissors.domain.moves.ClassicMoves;
import com.chompchompfig.rockpaperscissors.infrastructure.movestrategy.RemoteNextMoveResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriTemplate;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RockPaperScissorsMoveStrategyController.class)
@ContextConfiguration(classes=RestContextConfiguration.class)
public class RockPaperScissorsMoveStrategyControllerTests {

    public static final String RANDOM_STRATEGY_RESOURCE_NAME = "random";
    public static final String JSONPATH_RESPONSE_MOVE_PROPERTY_PATTERN = "$.move";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidConfigurationWhenApiMoveStrategyRandomNextMoveThenReturn200kAndRandomMove() throws Exception{
        UriTemplate template = new UriTemplate(RockPaperScissorsMoveStrategyController.API_MOVE_STRATEGY_NEXT_MOVE_URI);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(template.expand(RANDOM_STRATEGY_RESOURCE_NAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath(JSONPATH_RESPONSE_MOVE_PROPERTY_PATTERN).hasJsonPath())
                .andExpect(jsonPath(JSONPATH_RESPONSE_MOVE_PROPERTY_PATTERN).isString()).andReturn();
        assertResponseIsClassicMove(result.getResponse().getContentAsString());
    }

    private void assertResponseIsClassicMove(String jsonResponse) throws java.io.IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RemoteNextMoveResource response = objectMapper.readValue(jsonResponse, RemoteNextMoveResource.class);
        assertNotNull(ClassicMoves.from(response.getMove()));
    }
}
