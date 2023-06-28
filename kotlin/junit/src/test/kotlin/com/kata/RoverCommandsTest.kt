package com.kata

import com.kata.Command.*
import com.kata.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

private val commandPanel = CommandPanel()
private val marsMap = MarsMap()

internal class LandingTest {
    @Test
    fun `when receiving a starting point and a direction the rover lands successfully`() {
        val rover = commandPanel.land(marsMap, Position(0, 0), North)

        assert(rover).atPosition(0, 0).isOriented(North)
    }

    @Test
    fun `when receiving a starting point corresponding to an obstacle the landing is aborted`() {
        val obstacle = Position(0, 0)
        val mapWithObstacle = MarsMap(obstacle)

        assertThatThrownBy { commandPanel.land(mapWithObstacle, obstacle, North) }
                .isInstanceOf(ObstacleDetectedException::class.java)
    }
}

internal class SingleCommandTest {
    @ParameterizedTest(name = "when receiving command {2} the rover moves by 1")
    @MethodSource("moveCommands")
    fun moveCommand(startingX: Int, startingY: Int, commandMove: Command, expectedX: Int, expectedY: Int) {
        val rover = Rover(Position(startingX, startingY), North)

        commandPanel.input(rover, marsMap, commandMove)

        assert(rover).atPosition(expectedX, expectedY).isOriented(North)
    }

    @ParameterizedTest(name = "when receiving command {1} the rover turns")
    @MethodSource("turnCommands")
    fun turnCommand(startingDirection: Direction, commandTurn: Command, expectedDirection: Direction) {
        val rover = Rover(Position(0, 0), startingDirection)

        commandPanel.input(rover, marsMap, commandTurn)

        assert(rover).atPosition(0, 0).isOriented(expectedDirection)
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
        val rover = Rover(Position(0, 0), North)

        val obstacleReport = commandPanel.input(rover, marsMap, Forward, Right, Forward)

        assert(rover).atPosition(1, 1).isOriented(East)
        assertThat(obstacleReport).isNull()
    }

    @Test
    fun `when commands encounter an obstacle the rover stops just before and reports it`() {
        val rover = Rover(Position(0, 0), North)
        val obstacle = Position(1, 1)
        val mapWithObstacle = MarsMap(obstacle)

        val obstacleReport = commandPanel.input(rover, mapWithObstacle, Forward, Right, Forward, Backward)

        assert(rover).atPosition(0, 1).isOriented(East)
        assertThat(obstacleReport)
                .isNotNull()
                .hasFieldOrPropertyWithValue(ObstacleReport::obstacle.name, obstacle)
    }
}
