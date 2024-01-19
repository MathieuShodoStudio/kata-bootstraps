package com.kata

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/* https://codingdojo.org/kata/FooBarQix/ */

private val foobarqix = FooBarQix()

internal class ArithmeticsRulesTest {

    @Test
    fun `return empty by default`() {
        compute(1).returns("")
    }

    @Test
    fun `return Foo if the number is divisible by 3`() {
        compute(6).returns("Foo")
    }

    @Test
    fun `return Bar if the number is divisible by 5`() {
        compute(10).returns("Bar")
    }

    @Test
    fun `return FooBar if the number is divisible by both 3 and 5`() {
        compute(60).returns("FooBar")
    }

    @Test
    fun `return Qix if the number is divisible by 7`() {
        compute(14).returns("Qix")
    }

    @Test
    fun `return FooQix if the number is divisible by both 3 and 7`() {
        compute(21).returns("FooQix")
    }

    @Test
    fun `return BarQix if the number is divisible by both 5 and 7`() {
        compute(140).returns("BarQix")
    }

    @Test
    fun `return FooBarQix if the number is divisible by all 3 and 5 and 7`() {
        compute(210).returns("FooBarQix")
    }

    private class Matcher(val number: Int) {
        fun returns(expected: String) {
            val actual = foobarqix.computeArithmeticsRules(number)
            assertEquals(expected, actual)
        }
    }

    private fun compute(number: Int): Matcher = Matcher(number)
}

internal class DigitsRulesTest {

    @Test
    fun `return empty by default`() {
        compute("1").returns("")
    }

    @Test
    fun `return Foo for each digit 3 the number contains`() {
        compute("333").returns("FooFooFoo")
    }

    @Test
    fun `return Bar for each digit 5 the number contains`() {
        compute("55555").returns("BarBarBarBarBar")
    }

    @Test
    fun `return Qix for each digit 7 the number contains`() {
        compute("77").returns("QixQix")
    }

    @Test
    fun `return values in the digits order`() {
        compute("735").returns("QixFooBar")
    }

    private class Matcher(val number: String) {
        fun returns(expected: String) {
            val actual = foobarqix.computeDigitsRules(number)
            assertEquals(expected, actual)
        }
    }

    private fun compute(number: String): Matcher = Matcher(number)
}

internal class RequirementsExamplesTest {

    @Test
    fun `checking the examples in the requirements compute correctly`() {
        compute("1").returns("1")
        compute("2").returns("2")
        compute("3").returns("FooFoo")
        compute("4").returns("4")
        compute("5").returns("BarBar")
        compute("6").returns("Foo")
        compute("7").returns("QixQix")
        compute("8").returns("8")
        compute("9").returns("Foo")
        compute("10").returns("Bar")
        compute("13").returns("Foo")
        compute("15").returns("FooBarBar")
        compute("21").returns("FooQix")
        compute("33").returns("FooFooFoo")
        compute("51").returns("FooBar")
        compute("53").returns("BarFoo")
        compute("703").returns("QixFoo")
    }

    private class Matcher(val number: String) {
        fun returns(expected: String) {
            val actual = foobarqix.compute(number)
            assertEquals(expected, actual)
        }
    }

    private fun compute(number: String): Matcher = Matcher(number)
}
