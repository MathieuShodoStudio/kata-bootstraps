package com.kata

class FooBarQix {
    private val arithmeticStrategies: List<DivisibleByNumberStrategy>
    private val digitStrategies: List<ContainsCharStrategy>

    init {
        arithmeticStrategies = listOf(
            DivisibleByNumberStrategy(3, "Foo"),
            DivisibleByNumberStrategy(5, "Bar"),
            DivisibleByNumberStrategy(7, "Qix")
        )

        digitStrategies = listOf(
            ContainsCharStrategy('3', "Foo"),
            ContainsCharStrategy('5', "Bar"),
            ContainsCharStrategy('7', "Qix")

        )
    }

    fun compute(number: String): String {
        var result = computeArithmeticsRules(number.toInt())

        result += computeDigitsRules(number)
        
        if (result == "") {
            result = number
        }

        return result
    }

    fun computeArithmeticsRules(number: Int): String {
        var result = ""
        for (strategy in arithmeticStrategies) {
            if (strategy.canHandle(number)) {
                result += strategy.compute()
            }
        }

        return result
    }

    fun computeDigitsRules(number: String): String {
        var result = ""

        for (digit in number) {
            for (strategy in digitStrategies) {
                if (strategy.canHandle(digit)) {
                    result += strategy.compute()
                }
            }
        }

        return result;
    }
}

class DivisibleByNumberStrategy(val number: Int, val output: String) {
    fun canHandle(input: Int): Boolean = input % number == 0
    fun compute(): String = output
}

class ContainsCharStrategy(val char: Char, val output: String) {
    fun canHandle(input: Char): Boolean = input == char
    fun compute(): String = output
}
