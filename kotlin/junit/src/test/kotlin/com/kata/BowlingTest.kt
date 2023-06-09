package com.kata

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class BowlingTest {
    private lateinit var bowlingGame: Game

    @BeforeEach
    fun setUp() {
        bowlingGame = BowlingGame()
    }

    @Test
    fun assertNewGameHasScoreZero() {
        assertEquals(0, bowlingGame.score())
    }

    @Test
    fun assertMissedRollHasScoreZero() {
        bowlingGame.roll(0)
        assertEquals(0, bowlingGame.score())
    }

    @Test
    fun assertRollHasCorrectScore() {
        val roll = 3
        bowlingGame.roll(roll)
        assertEquals(roll, bowlingGame.score())
    }

    @Test
    fun assertSecondRollHasCorrectScore() {
        val firstRoll = 3
        bowlingGame.roll(firstRoll)
        val secondRoll = 3
        bowlingGame.roll(secondRoll)
        assertEquals(firstRoll + secondRoll, bowlingGame.score())
    }
}