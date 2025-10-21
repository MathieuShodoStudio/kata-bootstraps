package com.kata

import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat

class HelloWorldSteps: En {
    lateinit var currentWorld: HelloWorld
    lateinit var currentGreeting: String

    init {
        Given("the World was created") {
            currentWorld = HelloWorld()
        }

        When("I ask for a greeting") {
            currentGreeting = currentWorld.greeting()
        }

        Then("the greeting should be {string}") { expectedGreeting: String ->
            assertThat(currentGreeting).isEqualTo(expectedGreeting)
        }
    }

}