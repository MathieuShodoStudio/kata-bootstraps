package com.kata

import com.kata.Command.*
import com.kata.Direction.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class LandingTest {
    @Test
    fun `when receiving a starting point and a direction the rover lands successfully`() {
        terminal()
                .startingPoint(0, 0).direction(North).on(marsMap)
                .lands()
                .landed()
    }

    @Test
    fun `when receiving a starting point corresponding to an obstacle the landing is aborted`() {
        val (obstacleX, obstacleY) = intArrayOf(0, 0)
        val mapWithObstacle = MarsMap(Position(obstacleX, obstacleY))

        terminal()
                .startingPoint(obstacleX, obstacleY).direction(North).on(mapWithObstacle)
                .lands()
                .abortedLanding()
    }
}

internal class SingleCommandTest {
    @ParameterizedTest(name = "when receiving command {2} the rover moves by 1")
    @MethodSource("moveCommands")
    fun moveCommand(startingX: Int, startingY: Int, commandMove: Command, expectedX: Int, expectedY: Int) {
        terminal()
                .startingPoint(startingX, startingY).direction(North).on(marsMap)
                .lands().landed()
                .commands(commandMove)
                .reportedSuccess()
                .atPosition(expectedX, expectedY).isOriented(North)
    }

    @ParameterizedTest(name = "when receiving command {1} the rover turns")
    @MethodSource("turnCommands")
    fun turnCommand(startingDirection: Direction, commandTurn: Command, expectedDirection: Direction) {
        terminal()
                .startingPoint(0, 0).direction(startingDirection).on(marsMap)
                .lands().landed()
                .commands(commandTurn)
                .reportedSuccess()
                .atPosition(0, 0).isOriented(expectedDirection)
    }

    companion object {
        @JvmStatic
        fun moveCommands() = listOf(
                Arguments.of(0, 0, Forward, 0, 1),
                Arguments.of(0, 1, Backward, 0, 0),
        )

        @JvmStatic
        fun turnCommands() = listOf(
                Arguments.of(North, Left, West),
                Arguments.of(North, Right, East),
        )
    }
}

internal class CommandSequenceTest {
    @Test
    fun `when receiving sequence of commands the rover executes them all`() {
        terminal()
                .startingPoint(0, 0).direction(North).on(marsMap)
                .lands().landed()
                .commands(Forward, Right, Forward)
                .reportedSuccess()
                .atPosition(1, 1).isOriented(East)
    }

    @Test
    fun `when commands encounter an obstacle the rover stops just before and reports it`() {
        val (obstacleX, obstacleY) = intArrayOf(1, 1)
        val mapWithObstacle = MarsMap(Position(obstacleX, obstacleY))

        terminal()
                .startingPoint(0, 0).direction(North).on(mapWithObstacle)
                .lands().landed()
                .commands(Forward, Right, Forward, Backward)
                .reportedObstacleAt(obstacleX, obstacleY)
                .atPosition(0, 1).isOriented(East)
    }
}
