package com.kata

import com.kata.Command.Backward
import com.kata.Command.Forward
import com.kata.Command.Left
import com.kata.Command.Right
import com.kata.Rotation.Clockwise
import com.kata.Rotation.CounterClockwise

class CommandTerminal {
    fun land(marsMap: MarsMap, position: Position, direction: Direction): Rover {
        if (marsMap.isObstacle(position)) throw ObstacleDetectedException(position)
        return Rover(position, direction)
    }

    fun input(rover: Rover, map: MarsMap, vararg commands: Command): ObstacleReport? {
        return try {
            commands.forEach { command -> rover.execute(command, map) }
            null
        } catch (obstacleDetectedException: ObstacleDetectedException) {
            ObstacleReport(obstacleDetectedException.position)
        }
    }

    private fun Rover.execute(command: Command, map: MarsMap) {
        when (command) {
            Forward -> move(map, MovingMode.Forward)
            Backward -> move(map, MovingMode.Backward)
            Left -> turn(CounterClockwise)
            Right -> turn(Clockwise)
        }
    }
}
