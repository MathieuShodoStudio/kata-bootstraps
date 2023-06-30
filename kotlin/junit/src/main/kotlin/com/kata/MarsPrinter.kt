package com.kata

import com.kata.Direction.East
import com.kata.Direction.North
import com.kata.Direction.South
import com.kata.Direction.West

class MarsPrinter {
    fun print(map: MarsMap, rover: Rover): String {
        return map.indexRange().reversed()
                .joinToString("\n") {
                    y -> printRow(y, map, rover)
                }
                .trimIndent()
    }

    private fun printRow(y: Int, map: MarsMap, rover: Rover): String {
        return map.indexRange()
                .joinToString("") {
                    x -> printPosition(Position(x, y), map, rover)
                }
    }

    private fun printPosition(position: Position, map: MarsMap, rover: Rover): String {
        return if (map.isObstacle(position)) {
            obstacle()
        } else if (rover.position() == position) {
            rover(rover.direction())
        } else {
            emptyPosition()
        }
    }

    private fun emptyPosition() = "-"
    private fun obstacle() = "X"
    private fun rover(direction: Direction) = when (direction) {
        North -> "^"
        East -> ">"
        South -> "v"
        West -> "<"
    }
}

private fun MarsMap.indexRange(): IntRange = 0 until this.size
