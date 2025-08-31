package com.uchith.utms.service.remote;

import jakarta.ejb.Remote;

@Remote
public enum Direction {
    SOUTH("South"),
    NORTH("North"),
    LEFT("Left"),
    RIGHT("Right"),
    SOUTH_TO_NORTH("Started from South and finished from North"),
    SOUTH_TO_LEFT("Started from South and finished from Left"),
    SOUTH_TO_RIGHT("Started from South and finished from Right"),
    NORTH_TO_SOUTH("Started from North and finished from South"),
    NORTH_TO_RIGHT("Started from North and finished from Right"),
    NORTH_TO_LEFT("Started from North and finished from Left"),
    RIGHT_TO_LEFT("Started from Right and finished from Left"),
    RIGHT_TO_NORTH("Started from Right and finished from North"),
    RIGHT_TO_SOUTH("Started from Right and finished from South"),
    LEFT_TO_RIGHT("Started from Left and finished from Right"),
    LEFT_TO_NORTH("Started from Left and finished from South"),
    LEFT_TO_SOUTH("Started from Left and finished from South");

    String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
