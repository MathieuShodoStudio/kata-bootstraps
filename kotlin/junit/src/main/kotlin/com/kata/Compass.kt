package com.kata

import com.kata.Direction.*
import com.kata.Rotation.*

class Compass {
    fun next(direction: Direction, rotation: Rotation): Direction = when(rotation) {
        Clockwise -> nextClockwise(direction)
        CounterClockwise -> nextCounterClockwise(direction)
    }

    fun opposite(direction: Direction): Direction = when (direction) {
        North -> South
        South -> North
        East -> West
        West -> East
    }

    private fun nextClockwise(direction: Direction) = when (direction) {
        North -> East
        East -> South
        South -> West
        West -> North
    }

    private fun nextCounterClockwise(direction: Direction) = when (direction) {
        North -> West
        West -> South
        South -> East
        East -> North
    }
}
