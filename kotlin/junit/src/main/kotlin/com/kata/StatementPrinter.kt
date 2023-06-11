package com.kata

import java.text.SimpleDateFormat
import java.util.Date

interface StatementPrinter {
    fun print(operations: List<Operation>): String
}

private const val STATEMENT_HEADER = "Date;Amount;Balance"
private const val STATEMENT_SEPARATOR = "\n"

class CsvStatementPrinter : StatementPrinter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun print(operations: List<Operation>): String {
        return STATEMENT_HEADER + STATEMENT_SEPARATOR + operations.joinToString(STATEMENT_SEPARATOR, transform = this::format)
    }

    private fun format(operation: Operation) : String {
        val amountSymbol = when(operation) {
            is Deposit -> "+"
            is Withdrawal -> "-"
        }

        return "${operation.date.format()};$amountSymbol${operation.amount};${operation.balance}"
    }

    private fun Date.format(): String = dateFormat.format(this)
}
