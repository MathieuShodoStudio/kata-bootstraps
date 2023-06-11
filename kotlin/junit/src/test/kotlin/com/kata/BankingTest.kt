package com.kata

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class BankingTest {
    private lateinit var account: Account

    @BeforeEach
    fun setUp() {
        jumpToDate("2022-06-20")
        account = BankingAccount(InMemoryOperationRepository(dateSupplier), CsvStatementPrinter())
    }

    @Test
    fun `new account should have a blank statement and balance 0`() {
        statement().equalTo(account.printStatement())
        assertEquals(0, account.balance())
    }

    @Test
    fun `making a deposit should create a deposit, have the amount as balance, and appear on statement`() {
        val createdDeposit = account.deposit(333)
        deposit("2022-06-20;+333;333").equalTo(createdDeposit)

        statement("2022-06-20;+333;333").equalTo(account.printStatement())
    }

    @Test
    fun `making two deposits should create the deposits, have the sum of amounts as balance, and both appear on statement`() {
        val firstDeposit = account.deposit(333)
        deposit("2022-06-20;+333;333").equalTo(firstDeposit)

        jumpToDate("2023-12-01")
        val secondDeposit = account.deposit(666)
        deposit("2023-12-01;+666;999").equalTo(secondDeposit)

        statement("2022-06-20;+333;333", "2023-12-01;+666;999").equalTo(account.printStatement())
    }

    @Test
    fun `fail to withdraw a bigger amount than the balance`() {
        assertThrows<IllegalArgumentException> { account.withdraw(account.balance() + 1) }
    }

    @Test
    fun `making a deposit than a withdrawal should create the operations, have the subtraction of amounts as balance, and both appear on statement`() {
        val deposit = account.deposit(333)
        deposit("2022-06-20;+333;333").equalTo(deposit)

        jumpToDate("2023-12-01")
        val withdrawal = account.withdraw(111)
        withdrawal("2023-12-01;-111;222").equalTo(withdrawal)

        statement("2022-06-20;+333;333", "2023-12-01;-111;222").equalTo(account.printStatement())
    }
}
