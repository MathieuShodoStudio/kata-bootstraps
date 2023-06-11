package com.kata

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

private const val MAXIMUM_SCORE = 300

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
        val thirdRoll = 1
        bowlingGame.roll(thirdRoll)
        assertEquals(firstRoll + secondRoll + secondRoll + thirdRoll + thirdRoll, bowlingGame.score())
    }

    @Test
    fun assertThreeStrikesTriggeredBonuses() {
        playStrikeFrames(3)
        assertEquals(60, bowlingGame.score())
    }

    @Test
    fun failWhenRollingTooManyNormalBalls() {
        playNormalFrames(10, 1)

        assertThrows<IllegalStateException> { bowlingGame.roll(0) }
    }

    @Test
    fun assertNormalTenthFrameHasCorrectScore() {
        val scoreAfterNineFrames = playNormalFrames(9, 3)

        val firstRoll = 3
        bowlingGame.roll(firstRoll)
        val secondRoll = 3
        bowlingGame.roll(secondRoll)

        assertEquals(scoreAfterNineFrames + firstRoll + secondRoll, bowlingGame.score())
    }

    @Test
    fun failWhenRollingTooManySpareBalls() {
        playNormalFrames(10, 5)
        val bonusRoll = 3
        bowlingGame.roll(bonusRoll)

        assertThrows<IllegalStateException> { bowlingGame.roll(0) }
    }

    @Test
    fun assertSpareTenthFrameHasCorrectScore() {
        val scoreAfterNineFrames = playNormalFrames(9, 3)

        val firstRoll = 3
        bowlingGame.roll(firstRoll)
        val secondRoll = 7
        bowlingGame.roll(secondRoll)

        val finalRoll = 3
        bowlingGame.roll(finalRoll)

        assertEquals(scoreAfterNineFrames + firstRoll + secondRoll + finalRoll, bowlingGame.score())
    }

    @Test
    fun failWhenRollingTooManyStrikeBalls() {
        playStrikeFrames(10)

        val bonusFirstRoll = 3
        bowlingGame.roll(bonusFirstRoll)

        val bonusSecondRoll = 3
        bowlingGame.roll(bonusSecondRoll)

        assertThrows<IllegalStateException> { bowlingGame.roll(0) }
    }

    @Test
    fun assertStrikeTenthFrameHasCorrectScore() {
        val scoreAfterNineFrames = playNormalFrames(9, 3)

        val strike = 10
        bowlingGame.roll(strike)

        val firstRoll = 3
        bowlingGame.roll(firstRoll)

        val finalRoll = 3
        bowlingGame.roll(finalRoll)

        assertEquals(scoreAfterNineFrames + strike + firstRoll + finalRoll, bowlingGame.score())
    }

    @Test
    fun assertPerfectGameHasMaximumScore() {
        playStrikeFrames(10)
        assertEquals(270, bowlingGame.score())

        val bonusFirstRoll = 10
        bowlingGame.roll(bonusFirstRoll)
        assertEquals(290, bowlingGame.score())

        val bonusSecondRoll = 10
        bowlingGame.roll(bonusSecondRoll)

        assertEquals(MAXIMUM_SCORE, bowlingGame.score())
    }

    private fun playNormalFrames(numberOfFrames: Int, numberOfPins: Int): Int {
        for (frameNumber in 1..numberOfFrames) {
            bowlingGame.roll(numberOfPins)
            bowlingGame.roll(numberOfPins)
        }

        return numberOfPins * 2 * numberOfFrames
    }


    private fun playStrikeFrames(numberOfFrames: Int): Int {
        for (frameNumber in 1..numberOfFrames) {
            bowlingGame.roll(10)
        }

        return 10 * numberOfFrames
    }
}