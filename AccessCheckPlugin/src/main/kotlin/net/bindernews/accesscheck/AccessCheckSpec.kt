package net.bindernews.accesscheck

import java.io.Serializable

interface AccessCheckSpec : Serializable {

    /**
     * Class filter specification.
     */
    val filter: ClassFilterSpec

    /**
     * Set of rules which each class will be checked against, in order.
     *
     * Rules start with a `+` to allow access, `-` to error on access, or `!` to warn on access.
     * The remainder of the rule is a class name or package ending in a `.*`. The first matching
     * rule will trigger a result, so different checks should be in different [AccessCheckSpec]s.
     *
     * This example allow access to anything in `com.example.util`, explicitly allows access
     * to `com.example.test.TestUtil`, disallows access to the package `com.example.test`, and
     * provides a warning when accessing the `com.example.inner` package.
     * ```
     * +com.example.util.*
     * +com.example.test.TestUtil
     * -com.example.test.*
     * !com.example.inner.*
     * ```
     */
    var rules: ArrayList<String>

    /**
     * Builder function to make it clear you're adding rules.
     */
    fun addRule(rule: String): AccessCheckSpec {
        rules.add(rule)
        return this
    }

    fun allow(rule: String): AccessCheckSpec {
        return addRule("+ $rule")
    }

    fun deny(rule: String): AccessCheckSpec {
        return addRule("- $rule")
    }

    fun warn(rule: String): AccessCheckSpec {
        return addRule("! $rule")
    }

    class Impl() : AccessCheckSpec {
        override var filter: ClassFilterSpec = ClassFilterSpec.Impl()
        override var rules: ArrayList<String> = arrayListOf()
    }
}