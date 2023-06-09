package com.kata

interface Game {
    fun roll(numberOfPins: Int)

    fun score(): Int
}

class BowlingGame : Game {
    private val framesCompleted: MutableList<Frame> = ArrayList()
    private var currentFrame: Frame = BowlingFrame()
    private var previousFrame: Frame? = null

    private var bonusesAvailable: Int = 0

    init {
        framesCompleted.add(currentFrame)
    }

    override fun roll(numberOfPins: Int) {
        val frameStatus = currentFrame.roll(numberOfPins)

        if (bonusesAvailable > 0 && previousFrame != null) {
            previousFrame!!.bonus(numberOfPins)
            bonusesAvailable--
        }

        if (FrameStatus.COMPLETED == frameStatus) {
            initializeNextFrame()
        }
        else if (FrameStatus.COMPLETED_STRIKE == frameStatus) {
            initializeNextFrame()
            bonusesAvailable += 2
        }
        else if (FrameStatus.COMPLETED_SPARE == frameStatus) {
            initializeNextFrame()
            bonusesAvailable++
        }
    }

    private fun initializeNextFrame() {
        previousFrame = currentFrame
        currentFrame = BowlingFrame()
        framesCompleted.add(currentFrame)
    }

    override fun score(): Int {
        return framesCompleted.stream().mapToInt { frame -> frame.score() }.sum()
    }
}

enum class FrameStatus {
    NO_ROLL_MADE, FIRST_ROLL_MADE, COMPLETED, COMPLETED_SPARE, COMPLETED_STRIKE
}

interface Frame {
    fun roll(numberOfPins: Int): FrameStatus

    fun bonus(numberOfPins: Int)

    fun score(): Int
}

class BowlingFrame: Frame {
    private var score: Int = 0
    private var status = FrameStatus.NO_ROLL_MADE

    override fun roll(numberOfPins: Int) : FrameStatus {
        score += numberOfPins

        status = if (FrameStatus.NO_ROLL_MADE == status && numberOfPins == 10) {
            FrameStatus.COMPLETED_STRIKE
        }
        else if (FrameStatus.FIRST_ROLL_MADE == status) {
            if (score == 10) {
                FrameStatus.COMPLETED_SPARE
            } else {
                FrameStatus.COMPLETED
            }
        }
        else {
            FrameStatus.FIRST_ROLL_MADE
        }

        return status
    }

    override fun bonus(numberOfPins: Int) {
        score += numberOfPins
    }

    override fun score(): Int {
        return score
    }
}