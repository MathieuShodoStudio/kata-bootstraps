package com.kata

interface Game {
    fun roll(pins: Int)

    fun score(): Int
}

private const val MAXIMUM_FRAMES = 10

class BowlingGame : Game {
    private val frames: MutableList<Frame> = ArrayList()
    private var currentFrameIndex: Int = 0
    private var currentRollIndex: Int = 1

    private val framesEligibleForBonus: MutableMap<Int, MutableList<Frame>> = HashMap()

    init {
        for (frameIndex in 1 until MAXIMUM_FRAMES) {
            frames.add(BowlingFrame(frameIndex))
        }
        frames.add(BowlingFinalFrame(MAXIMUM_FRAMES))
    }

    override fun roll(pins: Int) {
        val frame = getCurrentFrame()

        val frameStatus = frame.roll(currentRollIndex, pins)

        creditBonuses(pins)

        updateBonusesAvailable(frame, frameStatus)

        System.err.println("Total score of ${score()} after roll $currentRollIndex during frame ${currentFrameIndex+1}")
        System.err.println("===========================================")

        currentRollIndex++
    }

    override fun score(): Int {
        return frames.stream().mapToInt { frame -> frame.score() }.sum()
    }

    private fun getCurrentFrame(): Frame {
        var currentFrame = frames[currentFrameIndex]
        if (currentFrame.isCompleted()) {
            if (currentFrame is BowlingFinalFrame) throw IllegalStateException("Game is over")

            currentFrameIndex++
            currentFrame = frames[currentFrameIndex]
        }

        return currentFrame
    }

    private fun creditBonuses(pins: Int) {
        framesEligibleForBonus[currentRollIndex]?.forEach { it.bonus(currentRollIndex, pins) }
    }

    private fun updateBonusesAvailable(frame: Frame, frameStatus: FrameStatus) {
        if (FrameStatus.COMPLETED_SPARE == frameStatus) {
            addBonus(frame, currentRollIndex + 1)
        }
        else if (FrameStatus.COMPLETED_STRIKE == frameStatus) {
            addBonus(frame, currentRollIndex + 1)
            addBonus(frame, currentRollIndex + 2)
        }
        else if (FrameStatus.BONUS_ROLL == frameStatus) {
            addBonus(frame, currentRollIndex + 1)
        }
    }

    private fun addBonus(frame: Frame, frameIndex: Int) {
        if (!framesEligibleForBonus.containsKey(frameIndex)) {
            framesEligibleForBonus[frameIndex] = ArrayList()
        }
        framesEligibleForBonus[frameIndex]!!.add(frame)
    }
}

enum class FrameStatus(val isCompleted: Boolean) {
    NO_ROLL_MADE(false),
    ROLL_MADE(false),
    BONUS_ROLL(false),
    COMPLETED(true),
    COMPLETED_SPARE(true),
    COMPLETED_STRIKE(true)
}

interface Frame {
    fun isCompleted(): Boolean

    fun roll(rollIndex: Int, pins: Int): FrameStatus

    fun bonus(rollIndex: Int, pins: Int)

    fun score(): Int
}

private const val MAXIMUM_PINS = 10

open class BowlingFrame(private val index: Int) : Frame {
    private var score: Int = 0
    private var bonus: Int = 0
    protected var status = FrameStatus.NO_ROLL_MADE

    override fun isCompleted(): Boolean  = status.isCompleted

    override fun roll(rollIndex: Int, pins: Int): FrameStatus {
        updateScore(rollIndex, pins)

        status = if (FrameStatus.NO_ROLL_MADE == status && pins == MAXIMUM_PINS) {
            FrameStatus.COMPLETED_STRIKE
        } else if (FrameStatus.ROLL_MADE == status) {
            if (score == MAXIMUM_PINS) {
                FrameStatus.COMPLETED_SPARE
            } else {
                FrameStatus.COMPLETED
            }
        } else {
            FrameStatus.ROLL_MADE
        }

        return status
    }

    override fun bonus(rollIndex: Int, pins: Int) {
        System.err.println("Bonus of $pins pins on roll $rollIndex for frame $index")
        bonus += pins
    }

    protected fun updateScore(rollIndex: Int, pins: Int) {
        System.err.println("Rolled $pins pins on roll $rollIndex during frame $index")
        score += pins
    }

    override fun score(): Int = score + bonus

    override fun toString(): String = "index: $index ; score: $score ; bonus: $bonus"
}

private const val MAXIMUM_ROLLS_FINAL_FRAME = 3

class BowlingFinalFrame(index: Int) : BowlingFrame(index) {
    private var rolls : Int = 0

    override fun roll(rollIndex: Int, pins: Int): FrameStatus {
        rolls++

        if (FrameStatus.BONUS_ROLL != status) {
            updateScore(rollIndex, pins)
        }

        if (FrameStatus.NO_ROLL_MADE == status) {
            status = if (MAXIMUM_PINS == pins) {
                FrameStatus.BONUS_ROLL
            } else {
                FrameStatus.ROLL_MADE
            }
        } else if (FrameStatus.ROLL_MADE == status && score() < MAXIMUM_PINS) {
            status = FrameStatus.COMPLETED
        }
        else if (MAXIMUM_ROLLS_FINAL_FRAME == rolls) {
            status = FrameStatus.COMPLETED
        }

        return status
    }
}
