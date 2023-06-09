package com.kata

interface Game {
    fun roll(numberOfPins: Int)

    fun score(): Int
}

private const val MAXIMUM_FRAMES = 10

class BowlingGame : Game {
    private val frames: MutableList<Frame> = ArrayList()
    private var currentFrame: Frame = BowlingFrame()
    private var previousFrame: Frame? = null

    private var bonusesAvailable: Int = 0

    init {
        frames.add(currentFrame)
    }

    override fun roll(numberOfPins: Int) {
        assertGameIsOngoing()

        val frame = getCurrentFrame()

        val frameStatus = frame.roll(numberOfPins)

        creditBonusToFrame(frame, numberOfPins)

        updateBonusesAvailable(frameStatus)
    }

    override fun score(): Int {
        return frames.stream().mapToInt { frame -> frame.score() }.sum()
    }

    private fun assertGameIsOngoing() {
        if (currentFrame is BowlingFinalFrame && currentFrame.status().isCompleted) {
            throw IllegalStateException("Game was completed")
        }
    }

    private fun getCurrentFrame(): Frame {
        if (currentFrame.status().isCompleted) {
            previousFrame = currentFrame
            currentFrame = if (frames.size + 1 == MAXIMUM_FRAMES) {
                BowlingFinalFrame()
            } else {
                BowlingFrame()
            }

            frames.add(currentFrame)
        }

        return currentFrame
    }

    private fun creditBonusToFrame(frame: Frame, numberOfPins: Int) {
        if (bonusesAvailable > 0) {
            val frameToCreditBonus = if (frame is BowlingFinalFrame) {
                frame
            } else {
                previousFrame!!
            }
            frameToCreditBonus.bonus(numberOfPins)
            bonusesAvailable--
        }
    }

    private fun updateBonusesAvailable(frameStatus: FrameStatus) {
        bonusesAvailable += frameStatus.bonuses
    }
}

enum class FrameStatus(val isCompleted: Boolean, val bonuses: Int) {
    NO_ROLL_MADE(false, 0),
    ROLL_MADE(false, 0),
    COMPLETED(true, 0),
    COMPLETED_SPARE(true, 1),
    COMPLETED_STRIKE(true, 2)
}

interface Frame {
    fun status(): FrameStatus

    fun roll(numberOfPins: Int): FrameStatus

    fun bonus(numberOfPins: Int)

    fun score(): Int
}

private const val MAXIMUM_PINS = 10

open class BowlingFrame : Frame {
    private var score: Int = 0
    protected var status = FrameStatus.NO_ROLL_MADE

    override fun status(): FrameStatus = status

    override fun roll(numberOfPins: Int): FrameStatus {
        updateScore(numberOfPins)

        status = if (FrameStatus.NO_ROLL_MADE == status && numberOfPins == MAXIMUM_PINS) {
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

    override fun bonus(numberOfPins: Int) {
        updateScore(numberOfPins)
    }

    private fun updateScore(numberOfPins: Int) {
        score += numberOfPins
    }

    override fun score(): Int = score
}

private const val MAXIMUM_ROLLS_FINAL_FRAME = 3

class BowlingFinalFrame : BowlingFrame() {
    private var rolls : Int = 0
    override fun roll(numberOfPins: Int): FrameStatus {
        rolls++

        bonus(numberOfPins)

        if (FrameStatus.NO_ROLL_MADE == status) {
            status = FrameStatus.ROLL_MADE
        }
        else if (FrameStatus.ROLL_MADE == status && score() < MAXIMUM_PINS) {
            status = FrameStatus.COMPLETED
        }
        else if (MAXIMUM_ROLLS_FINAL_FRAME == rolls) {
            status = FrameStatus.COMPLETED
        }

        return status
    }
}
