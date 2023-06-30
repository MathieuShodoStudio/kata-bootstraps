package com.kata

import com.kata.Direction.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class MarsPrinterTest {
    @ParameterizedTest(name = "print {0}x{0} map with rover at {1},{2} facing {3} and obstacle at {4},{5}")
    @MethodSource("marsConfiguration")
    fun print(size: Int, roverX: Int, roverY: Int, roverOrientation: Direction, obstacleX: Int, obstacleY: Int, expectedPrint: String) {
        mars()
                .withSize(size)
                .withRover(Position(roverX, roverY), roverOrientation)
                .withObstacles(Position(obstacleX, obstacleY))
                .print()
                .isEqualTo(expectedPrint)
    }

    companion object {
        @JvmStatic
        fun marsConfiguration() = listOf(
                Arguments.of(2, 0, 0, North, 1, 1,
                        """
                            -X
                            ^-
                        """.trimIndent()
                ),
                Arguments.of(4, 1, 0, East, 2, 3,
                        """
                            --X-
                            ----
                            ----
                            ->--
                        """.trimIndent()
                ),
                Arguments.of(4, 3, 3, South, 0, 0,
                        """
                            ---v
                            ----
                            ----
                            X---
                        """.trimIndent()
                ),
                Arguments.of(6, 4, 5, West, 2, 1,
                        """
                            ----<-
                            ------
                            ------
                            ------
                            --X---
                            ------
                        """.trimIndent()
                ),
        )
    }
}
