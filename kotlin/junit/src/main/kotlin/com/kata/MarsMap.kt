package com.kata

import com.kata.Direction.*

const val DEFAULT_MAP_SIZE = 10

class MarsMap(vararg obstacles: Position) {
    var size = DEFAULT_MAP_SIZE
    private var strategies : Map<Direction, NeighboringStrategy> = listOf(
            NorthNeighboringStrategy(size),
            EastNeighboringStrategy(size),
            SouthNeighboringStrategy(size),
            WestNeighboringStrategy(size),
    ).associateBy(NeighboringStrategy::supportedDirection)

    constructor(size: Int, vararg obstacles: Position) : this(*obstacles) {
        this.size = size
    }

    private var obstacles : List<Position> = obstacles.toList()

    fun neighbor(position: Position, direction: Direction): Position {
        return neighboringStrategy(direction).neighbor(position)
    }

    fun isObstacle(position: Position): Boolean = obstacles.any { it == position }

    private fun neighboringStrategy(direction: Direction) = strategies[direction]!!
}

abstract class NeighboringStrategy(mapSize: Int, private val supportedDirection: Direction) {
    protected val edgeIndex = mapSize - 1

    fun supportedDirection(): Direction = supportedDirection
    fun neighbor(position: Position): Position = if (isEdge(position)) opposingEdge(position) else next(position)

    protected abstract fun isEdge(position: Position): Boolean
    protected abstract fun opposingEdge(position: Position): Position
    protected abstract fun next(position: Position): Position
}

class NorthNeighboringStrategy(mapSize: Int) : NeighboringStrategy(mapSize, North) {
    override fun isEdge(position: Position): Boolean = position.y == edgeIndex
    override fun opposingEdge(position: Position): Position = Position(position.x, 0)
    override fun next(position: Position): Position = Position(position.x, position.y + 1)
}

class EastNeighboringStrategy(mapSize: Int) : NeighboringStrategy(mapSize, East) {
    override fun isEdge(position: Position): Boolean = position.x == edgeIndex
    override fun opposingEdge(position: Position): Position = Position(0, position.y)
    override fun next(position: Position): Position = Position(position.x + 1, position.y)
}

class SouthNeighboringStrategy(mapSize: Int) : NeighboringStrategy(mapSize, South) {
    override fun isEdge(position: Position): Boolean = position.y == 0
    override fun opposingEdge(position: Position): Position = Position(position.x, edgeIndex)
    override fun next(position: Position): Position = Position(position.x, position.y - 1)
}

class WestNeighboringStrategy(mapSize: Int) : NeighboringStrategy(mapSize, West) {
    override fun isEdge(position: Position): Boolean = position.x == 0
    override fun opposingEdge(position: Position): Position = Position(edgeIndex, position.y)
    override fun next(position: Position): Position = Position(position.x - 1, position.y)
}
