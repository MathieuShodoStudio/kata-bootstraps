package com.kata

interface Account {
    fun deposit(amount: Int): Deposit

    fun withdraw(amount: Int): Withdrawal

    fun balance(): Int

    fun printStatement(): String
}

private const val INITIAL_BALANCE = 0

class BankingAccount(private val operationRepo: OperationRepository, private val statementPrinter: StatementPrinter) : Account {
    private var balance: Int = INITIAL_BALANCE

    override fun deposit(amount: Int): Deposit {
        val balanceAfterDeposit = balance + amount
        val deposit = operationRepo.deposit(amount, balanceAfterDeposit)
        balance = balanceAfterDeposit

        return deposit
    }

    override fun withdraw(amount: Int): Withdrawal {
        val balanceAfterWithdrawal = balance - amount
        val withdrawal = operationRepo.withdraw(amount, balanceAfterWithdrawal)
        balance = balanceAfterWithdrawal

        return withdrawal
    }

    override fun balance(): Int = balance

    override fun printStatement(): String = statementPrinter.print(operationRepo.findAll())
}