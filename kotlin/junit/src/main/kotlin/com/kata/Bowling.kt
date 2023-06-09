package com.kata

interface Game {
    fun roll(int: Int)

    fun score(): Int
}

class BowlingGame: Game {
    override fun roll(int: Int) {
        TODO("Not yet implemented")
    }

    override fun score(): Int {
        TODO("Not yet implemented")
    }
}