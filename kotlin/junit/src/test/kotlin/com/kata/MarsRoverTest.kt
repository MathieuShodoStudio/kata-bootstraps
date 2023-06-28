package com.kata

import com.kata.Direction.*
import com.kata.MovingMode.*
import com.kata.Rotation.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class RoverCreationTest {
    @Test
    fun `new rover created at position 0,0 and is oriented north`() {
        val rover = Rover(Position(0, 0), North)

        assert(rover).atPosition(0, 0).isOriented(North)
    }
}

internal class MovingForwardTest {
    @ParameterizedTest(name = "when a rover oriented {2} moves forward then it changes position by 1")
    @MethodSource("moveNominal")
    fun nominalMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        rover().at(startingX, startingY).facing(direction).on(marsMap)
                .moves(movingMode)
                .arrivedAt(expectedX, expectedY)
    }

    @ParameterizedTest(name = "when a rover at the edge and oriented {2} moves forward then it goes around to the other edge")
    @MethodSource("moveEdge")
    fun edgeMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        rover().at(startingX, startingY).facing(direction).on(marsMap)
                .moves(movingMode)
                .arrivedAt(expectedX, expectedY)
    }

    companion object {
        @JvmStatic
        fun moveNominal() = listOf(
                Arguments.of(0, 0, North, Forward, 0, 1),
                Arguments.of(0, 0, East, Forward, 1, 0),
                Arguments.of(0, 1, South, Forward, 0, 0),
                Arguments.of(1, 0, West, Forward, 0, 0),
        )

        @JvmStatic
        fun moveEdge() = listOf(
                Arguments.of(0, EDGE_INDEX, North, Forward, 0, 0),
                Arguments.of(EDGE_INDEX, 0, East, Forward, 0, 0),
                Arguments.of(0, 0, South, Forward, 0, EDGE_INDEX),
                Arguments.of(0, 0, West, Forward, EDGE_INDEX, 0),
        )
    }
}

internal class TurningTest {
    @ParameterizedTest(name = "when a rover oriented {2} turns {3} then it becomes oriented {4}")
    @MethodSource("turn")
    fun turn(startingDirection: Direction, rotation: Rotation, expectedDirection: Direction) {
        rover().at(0, 0).facing(startingDirection).on(marsMap)
                .turns(rotation)
                .facedTowards(expectedDirection)
    }

    companion object {
        @JvmStatic
        fun turn() = listOf(
                Arguments.of(North, Clockwise, East),
                Arguments.of(East, Clockwise, South),
                Arguments.of(South, Clockwise, West),
                Arguments.of(West, Clockwise, North),
                Arguments.of(North, CounterClockwise, West),
                Arguments.of(West, CounterClockwise, South),
                Arguments.of(South, CounterClockwise, East),
                Arguments.of(East, CounterClockwise, North),
        )
    }
}

internal class MovingBackwardsTest {
    @ParameterizedTest(name = "when a rover oriented {2} moves backwards then it changes position by 1")
    @MethodSource("moveNominal")
    fun nominalMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        rover().at(startingX, startingY).facing(direction).on(marsMap)
                .moves(movingMode)
                .arrivedAt(expectedX, expectedY)
    }

    @ParameterizedTest(name = "when a rover at the edge and oriented {2} moves backwards then it goes around to the other edge")
    @MethodSource("moveEdge")
    fun edgeMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        rover().at(startingX, startingY).facing(direction).on(marsMap)
                .moves(movingMode)
                .arrivedAt(expectedX, expectedY)
    }

    companion object {
        @JvmStatic
        fun moveNominal() = listOf(
                Arguments.of(0, 1, North, Backward, 0, 0),
                Arguments.of(1, 0, East, Backward, 0, 0),
                Arguments.of(0, 0, South, Backward, 0, 1),
                Arguments.of(0, 0, West, Backward, 1, 0),
        )

        @JvmStatic
        fun moveEdge() = listOf(
                Arguments.of(0, 0, North, Backward, 0, EDGE_INDEX),
                Arguments.of(0, 0, East, Backward, EDGE_INDEX, 0),
                Arguments.of(0, EDGE_INDEX, South, Backward, 0, 0),
                Arguments.of(EDGE_INDEX, 0, West, Backward, 0, 0),
        )
    }
}

internal class ObstacleDetectionTest {
    @Test
    fun `when a rover detects an obstacle it doesn't move and reports the location`() {
        val (obstacleX, obstacleY) = intArrayOf(0, 1)
        val mapWithObstacle = MarsMap(Position(obstacleX, obstacleY))

        rover().at(0, 0).facing(North).on(mapWithObstacle)
                .moves(Forward)
                .detectedObstacleAt(obstacleX, obstacleY)
    }
}
