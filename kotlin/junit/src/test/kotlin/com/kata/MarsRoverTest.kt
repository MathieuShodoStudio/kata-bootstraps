package com.kata

import com.kata.Direction.*
import com.kata.MovingMode.*
import com.kata.Rotation.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class LandingTest {
    @Test
    fun `new rover lands at position 0,0 and is oriented north`() {
        val rover = Rover(Position(0, 0), North)

        assert(rover).atPosition(0, 0).isOriented(North)
    }
}

private const val EDGE_INDEX = 9

internal class MovingForwardTest {
    @MethodSource("moveNominal")
    @ParameterizedTest(name = "when a rover oriented {2} moves forward then it changes position by 1")
    fun nominalMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        moveRover(startingX, startingY, direction, movingMode, expectedX, expectedY)
    }

    @MethodSource("moveEdge")
    @ParameterizedTest(name = "when a rover at the edge and oriented {2} moves forward then it goes around to the other edge")
    fun edgeMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        moveRover(startingX, startingY, direction, movingMode, expectedX, expectedY)
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
    @MethodSource("turn")
    @ParameterizedTest(name = "when a rover oriented {2} turns {3} then it becomes oriented {4}")
    fun turn(x: Int, y: Int, startingDirection: Direction, rotation: Rotation, expectedDirection: Direction) {
        val rover = Rover(Position(x, y), startingDirection)
        rover.turn(rotation)
        assert(rover).atPosition(x, y).isOriented(expectedDirection)
    }

    companion object {
        @JvmStatic
        fun turn() = listOf(
                Arguments.of(0, 0, North, Clockwise, East),
                Arguments.of(0, 0, East, Clockwise, South),
                Arguments.of(0, 0, South, Clockwise, West),
                Arguments.of(0, 0, West, Clockwise, North),
                Arguments.of(0, 0, North, CounterClockwise, West),
                Arguments.of(0, 0, West, CounterClockwise, South),
                Arguments.of(0, 0, South, CounterClockwise, East),
                Arguments.of(0, 0, East, CounterClockwise, North),
        )
    }
}

internal class MovingBackwardsTest {
    @MethodSource("moveNominal")
    @ParameterizedTest(name = "when a rover oriented {2} moves backwards then it changes position by 1")
    fun nominalMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        moveRover(startingX, startingY, direction, movingMode, expectedX, expectedY)
    }

    @MethodSource("moveEdge")
    @ParameterizedTest(name = "when a rover at the edge and oriented {2} moves backwards then it goes around to the other edge")
    fun edgeMove(startingX: Int, startingY: Int, direction: Direction, movingMode: MovingMode, expectedX: Int, expectedY: Int) {
        moveRover(startingX, startingY, direction, movingMode, expectedX, expectedY)
    }

    companion object {
        @JvmStatic
        fun moveNominal() = listOf(
                Arguments.of(0, 1, North, Backwards, 0, 0),
                Arguments.of(1, 0, East, Backwards, 0, 0),
                Arguments.of(0, 0, South, Backwards, 0, 1),
                Arguments.of(0, 0, West, Backwards, 1, 0),
        )

        @JvmStatic
        fun moveEdge() = listOf(
                Arguments.of(0, 0, North, Backwards, 0, EDGE_INDEX),
                Arguments.of(0, 0, East, Backwards, EDGE_INDEX, 0),
                Arguments.of(0, EDGE_INDEX, South, Backwards, 0, 0),
                Arguments.of(EDGE_INDEX, 0, West, Backwards, 0, 0),
        )
    }
}

internal class ObstacleDetectionTest {
    @Test
    fun `when a rover detects an obstacle it doesn't move and reports the location`() {
        TODO("Not yet implemented")
    }
}
