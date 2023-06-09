package com.kata

interface Game {
    fun roll(int: Int)

    fun score(): Int
}

class BowlingGame : Game {
    private var score: Int = 0

    override fun roll(int: Int) {
        score += int
    }

    override fun score(): Int {
        return score
    }
}