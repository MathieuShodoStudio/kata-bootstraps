package com.kata

interface Game {
    fun roll(numberOfPins: Int)

    fun score(): Int
}

class BowlingGame : Game {
    private var score: Int = 0
    private var bonusesAvailable: Int = 0

    override fun roll(numberOfPins: Int) {
        score += numberOfPins

        if (bonusesAvailable > 0) {
            score += numberOfPins
            bonusesAvailable--
        }

        if (numberOfPins == 10) {
            bonusesAvailable += 2
        }
        else if (score == 10) {
            bonusesAvailable++
        }
    }

    override fun score(): Int {
        return score
    }
}