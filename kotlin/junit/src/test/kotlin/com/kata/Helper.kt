package com.kata

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

const val EDGE_INDEX = 9

val marsMap = MarsMap()

fun assert(rover: Rover): RoverMatcher = RoverMatcher(rover)

open class RoverMatcher(protected val rover: Rover) {
    fun atPosition(x: Int, y: Int): RoverMatcher {
        assertThat(rover.position()).isEqualTo(Position(x, y))
        return this
    }

    fun isOriented(direction: Direction): RoverMatcher {
        assertThat(rover.direction()).isEqualTo(direction)
        return this
    }
}

fun rover(): RoverBuilder = RoverBuilderImpl()

interface RoverBuilder {
    fun facing(direction: Direction): RoverBuilder
    fun on(map: MarsMap): RoverBuilder
    fun at(x: Int, y: Int): RoverBuilder
    fun moves(movingMode: MovingMode): RoverMovementMatcher
    fun turns(rotation: Rotation): RoverTurnMatcher
}

class RoverBuilderImpl : RoverBuilder {
    var startingY = 0
    var startingX = 0
    lateinit var startingDirection: Direction
    lateinit var map: MarsMap

    override fun at(x: Int, y: Int): RoverBuilder {
        startingX = x
        startingY = y
        return this
    }

    override fun facing(direction: Direction): RoverBuilder {
        startingDirection = direction
        return this
    }

    override fun on(map: MarsMap): RoverBuilder {
        this.map = map
        return this
    }

    override fun moves(movingMode: MovingMode): RoverMovementMatcher {
        return RoverMovementMatcher(this, movingMode)
    }

    override fun turns(rotation: Rotation): RoverTurnMatcher {
        return RoverTurnMatcher(this, rotation)
    }

    fun build(): Rover = Rover(Position(startingX, startingY), startingDirection)
}

class RoverMovementMatcher(private val builder: RoverBuilderImpl, private val movingMode: MovingMode) {
    private val rover: Rover = builder.build()

    fun arrivedAt(expectedX: Int, expectedY: Int) {
        rover.move(builder.map, movingMode)
        assert(rover).atPosition(expectedX, expectedY).isOriented(builder.startingDirection)
    }

    fun detectedObstacleAt(obstacleX: Int, obstacleY: Int) {
        assertThatThrownBy { rover.move(builder.map, movingMode) }
                .isInstanceOf(ObstacleDetectedException::class.java)
                .hasFieldOrPropertyWithValue(ObstacleDetectedException::position.name, Position(obstacleX, obstacleY))

        assert(rover).atPosition(builder.startingX, builder.startingY).isOriented(builder.startingDirection)
    }
}

class RoverTurnMatcher(private val builder: RoverBuilderImpl, private val rotation: Rotation) {
    private val rover: Rover = builder.build()

    fun facedTowards(expectedDirection: Direction) {
        rover.turn(rotation)
        assert(rover).atPosition(builder.startingX, builder.startingY).isOriented(expectedDirection)
    }
}

fun terminal(): TerminalBuilder = TerminalBuilderImpl()

interface TerminalBuilder {
    fun startingPoint(landingX: Int, landingY: Int): TerminalBuilder
    fun direction(landingDirection: Direction): TerminalBuilder
    fun on(map: MarsMap): TerminalBuilder
    fun lands(): TerminalLandingMatcher
}

class TerminalBuilderImpl : TerminalBuilder {
    var landingY = 0
    var landingX = 0
    lateinit var landingDirection: Direction
    lateinit var map: MarsMap
    override fun startingPoint(landingX: Int, landingY: Int): TerminalBuilder {
        this.landingX = landingX
        this.landingY = landingY
        return this
    }

    override fun direction(landingDirection: Direction): TerminalBuilder {
        this.landingDirection = landingDirection
        return this
    }

    override fun on(map: MarsMap): TerminalBuilder {
        this.map = map
        return this
    }

    override fun lands(): TerminalLandingMatcher = TerminalLandingMatcher(this)

    fun build(): CommandTerminal = CommandTerminal()
}

class TerminalLandingMatcher(private val builder: TerminalBuilderImpl) {
    private val terminal: CommandTerminal = builder.build()

    fun landed(): CommandsMatcher {
        val rover = land()
        assert(rover).atPosition(builder.landingX, builder.landingY).isOriented(builder.landingDirection)
        return CommandsMatcher(terminal, rover, builder.map)
    }

    fun abortedLanding() {
        assertThatThrownBy { land() }.isInstanceOf(ObstacleDetectedException::class.java)
    }

    private fun land(): Rover =
            terminal.land(builder.map, Position(builder.landingX, builder.landingY), builder.landingDirection)
}

class CommandsMatcher(private val terminal: CommandTerminal, rover: Rover, private val map: MarsMap) : RoverMatcher(rover) {
    private var report: ObstacleReport? = null

    fun commands(vararg commands: Command): CommandsMatcher {
        report = terminal.input(rover, map, *commands)
        return this
    }

    fun reportedSuccess(): RoverMatcher {
        assertThat(report).isNull()
        return this
    }

    fun reportedObstacleAt(obstacleX: Int, obstacleY: Int): RoverMatcher {
        assertThat(report)
                .isNotNull()
                .hasFieldOrPropertyWithValue(ObstacleReport::obstacle.name, Position(obstacleX, obstacleY))
        return this
    }
}

fun mars(): MarsBuilder = MarsBuilderImpl()

interface MarsBuilder {
    fun withSize(size: Int): MarsBuilder
    fun withObstacles(vararg obstacles: Position): MarsBuilder
    fun withRover(position: Position, direction: Direction): MarsBuilder
    fun print(): MarsPrintMatcher
}

class MarsBuilderImpl : MarsBuilder {
    private var size = 0
    private lateinit var map: MarsMap
    private lateinit var rover: Rover

    override fun withSize(size: Int): MarsBuilder {
        this.size = size
        return this
    }

    override fun withObstacles(vararg obstacles: Position): MarsBuilder {
        this.map = MarsMap(size, *obstacles)
        return this
    }

    override fun withRover(position: Position, direction: Direction): MarsBuilder {
        this.rover = Rover(position, direction)
        return this
    }

    override fun print(): MarsPrintMatcher {
        return MarsPrintMatcher(map, rover)
    }
}

class MarsPrintMatcher(private val map: MarsMap, private val rover: Rover) {
    private val printer = MarsPrinter()

    fun isEqualTo(expectedPrint: String) {
        val print = printer.print(map, rover)
        assertThat(print).isEqualTo(expectedPrint)
    }
}
