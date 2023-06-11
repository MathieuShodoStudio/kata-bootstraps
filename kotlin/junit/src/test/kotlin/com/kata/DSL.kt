package com.kata

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import kotlin.test.assertEquals

private const val STATEMENT_HEADER = "Date;Amount;Balance"
private const val STATEMENT_SEPARATOR = "\n"

private val format = SimpleDateFormat("yyyy-MM-dd")

class FixedDateSupplier(private val format: DateFormat) : DateSupplier {
    private var currentDate: Date = Date.from(Instant.now())

    override fun today(): Date = currentDate

    fun fixed(date: String) {
        currentDate = format.parse(date)
    }
}

val dateSupplier = FixedDateSupplier(format)

fun jumpToDate(date: String) = dateSupplier.fixed(date)

fun statement(vararg expectedOperations: String) = StatementMatcher(expectedOperations)

class StatementMatcher(expectedOperations: Array<out String>) {
    private var expected: String

    init {
        expected = STATEMENT_HEADER + STATEMENT_SEPARATOR + expectedOperations.joinToString(STATEMENT_SEPARATOR)
    }

    fun equalTo(actual: String) = assertEquals(expected.trim(), actual.trim())
}

fun deposit(expected: String) = DepositMatcher(expected, format)

fun withdrawal(expected: String) = WithdrawalMatcher(expected, format)

abstract class OperationMatcher(expected: String, private val format: DateFormat) {
    private val expectedDate: String
    private val expectedAmount: String
    private val expectedBalance: String

    init {
        val expectedValues = expected.split(";")
        expectedDate = expectedValues[0]
        expectedAmount = expectedValues[1]
        expectedBalance = expectedValues[2]
    }

    fun equalTo(actual: Operation) {
        assertEquals(expectedDate, format.format(actual.date))
        assertEquals(expectedAmount, amountSymbol() + actual.amount)
        assertEquals(expectedBalance, actual.balance.toString())
    }

    abstract fun amountSymbol(): String
}

class DepositMatcher(expected: String, format: DateFormat) : OperationMatcher(expected, format) {
    override fun amountSymbol() = "+"
}

class WithdrawalMatcher(expected: String, format: DateFormat) : OperationMatcher(expected, format) {
    override fun amountSymbol() = "-"
}
