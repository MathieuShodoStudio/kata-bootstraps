package com.kata

import java.util.Date

sealed interface Operation {
    val date: Date
    val amount: Int
    val balance: Int
}

data class Deposit(override val date: Date, override val amount: Int, override val balance: Int) : Operation

data class Withdrawal(override val date: Date, override val amount: Int, override val balance: Int) : Operation

interface DateSupplier {
    fun today(): Date
}

interface OperationRepository {
    fun deposit(amount: Int, balance: Int): Deposit
    fun withdraw(amount: Int, balance: Int): Withdrawal
    fun findAll(): List<Operation>
}

private const val SUFFICIENT_FUNDS_THRESHOLD = 0

class InMemoryOperationRepository(private val dateSupplier: DateSupplier) : OperationRepository {
    private var operations: MutableList<Operation> = ArrayList()

    override fun deposit(amount: Int, balance: Int): Deposit {
        val deposit = Deposit(dateSupplier.today(), amount, balance)
        operations += deposit

        return deposit
    }

    override fun withdraw(amount: Int, balance: Int): Withdrawal {
        if (balance < SUFFICIENT_FUNDS_THRESHOLD) throw IllegalArgumentException("Cannot withdraw $amount given the current balance $balance")
        val withdrawal = Withdrawal(dateSupplier.today(), amount, balance)
        operations += withdrawal

        return withdrawal
    }

    override fun findAll(): List<Operation> = operations
}
