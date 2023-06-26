package com.kata

import org.assertj.core.api.Assertions

fun assert(rover: Rover): RoverMatcher = RoverMatcher(rover)

class RoverMatcher(private val rover: Rover) {
    fun atPosition(x: Int, y: Int): RoverMatcher {
        Assertions.assertThat(rover.position()).isEqualTo(Position(x, y))
        return this
    }

    fun isOriented(direction: Direction): RoverMatcher {
        Assertions.assertThat(rover.direction()).isEqualTo(direction)
        return this
    }
}

fun moveRover(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
    val rover = Rover(Position(startingX, startingY), direction)
    rover.move(MarsMap(), movingMode)
    assert(rover).atPosition(expectedX, expectedY).isOriented(direction)
}
