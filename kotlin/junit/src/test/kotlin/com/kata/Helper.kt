package com.kata

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThatThrownBy

fun assert(rover: Rover): RoverMatcher = RoverMatcher(rover)

class RoverMatcher(private val rover: Rover) {
    fun atPosition(x: Int, y: Int): RoverMatcher {
        assertThat(rover.position()).isEqualTo(Position(x, y))
        return this
    }

    fun isOriented(direction: Direction): RoverMatcher {
        assertThat(rover.direction()).isEqualTo(direction)
        return this
    }
}

fun moveRover(marsMap: MarsMap, startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
    val rover = Rover(Position(startingX, startingY), direction)
    rover.move(marsMap, movingMode)
    assert(rover).atPosition(expectedX, expectedY).isOriented(direction)
}

fun detectObstacle(marsMap: MarsMap, roverX: Int, roverY: Int, direction: Direction, movingMode: MovingMode, obstacleX: Int, obstacleY: Int) {
    val rover = Rover(Position(roverX, roverY), direction)

    assertThatThrownBy { rover.move(marsMap, movingMode) }
            .isInstanceOf(ObstacleDetectedException::class.java)
            .hasFieldOrPropertyWithValue(ObstacleDetectedException::position.name, Position(obstacleX, obstacleY))

    assert(rover).atPosition(roverX, roverY).isOriented(direction)
}
