package com.kata

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BowlingTest {
    private lateinit var bowlingGame : Game

    @BeforeEach
    fun setUp() {
        bowlingGame = BowlingGame()
    }

    @Test
    fun score() {
        assertThrows<NotImplementedError> { bowlingGame.roll(0) }
        assertThrows<NotImplementedError> { bowlingGame.score() }
    }
}