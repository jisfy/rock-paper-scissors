package com.chompchompfig.rockpaperscissors.infrastructure.movestrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to represent the NextMoveResource received from the API endpoint. Notice that this class may seem like
 * duplicated code, but it is created on purpose. Having a client representation of the resource, allows us to
 * effectively decouple the application from the actual endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteNextMoveResource {

    private String move;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
