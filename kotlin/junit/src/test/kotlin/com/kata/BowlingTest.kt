package com.kata

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @Test
    fun assertNormalFrameDidntTriggerBonus() {
        val firstRoll = 3
        bowlingGame.roll(firstRoll)
        val secondRoll = 3
        bowlingGame.roll(secondRoll)
        val thirdRoll = 1
        bowlingGame.roll(thirdRoll)
        assertEquals(firstRoll + secondRoll + thirdRoll, bowlingGame.score())
    }

    @Test
    fun assertSpareTriggeredBonus() {
        val firstRoll = 3
        bowlingGame.roll(firstRoll)
        val secondRoll = 7
        bowlingGame.roll(secondRoll)
        val thirdRoll = 1
        bowlingGame.roll(thirdRoll)
        assertEquals(firstRoll + secondRoll + thirdRoll + thirdRoll, bowlingGame.score())
    }

    @Test
    fun assertStrikeTriggeredTwoBonuses() {
        val firstRoll = 10
        bowlingGame.roll(firstRoll)
        val secondRoll = 3
        bowlingGame.roll(secondRoll)
        val thirdRoll = 3
        bowlingGame.roll(thirdRoll)
        assertEquals(firstRoll + secondRoll + secondRoll + thirdRoll + thirdRoll, bowlingGame.score())
    }
}