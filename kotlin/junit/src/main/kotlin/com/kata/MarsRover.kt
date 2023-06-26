package com.kata

import com.kata.MovingMode.*

class Rover(private var position: Position, private var direction: Direction) {
    private val compass = Compass()
    fun position(): Position = position
    fun direction(): Direction = direction

    fun move(map: MarsMap, movingMode: MovingMode) {
        val movingDirection = if (movingMode == Backwards) compass.opposite(direction) else direction
        val neighborPosition = map.neighbor(position, movingDirection)
        moveTo(neighborPosition)
    }

    private fun moveTo(neighborPosition: Position) {
        position = neighborPosition
    }

    fun turn(rotation: Rotation) {
        direction = compass.next(direction, rotation)
    }
}

enum class MovingMode { Forward, Backwards }
