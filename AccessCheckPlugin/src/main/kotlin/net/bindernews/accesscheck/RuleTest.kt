package net.bindernews.accesscheck

import javassist.CtClass

/**
 * Tests if a rule matches the given class.
 */
interface RuleTest {
    fun test(cls: CtClass): RuleResult

    val description: String

    class RegexRuleTest(
        override val description: String, private val regex: Regex, private val result: RuleResult
    ) : RuleTest {
        override fun test(cls: CtClass): RuleResult {
            val classNames = cls.classFile2.constPool.classNames
            return if (classNames.any { s: Any? -> regex.matches(s as String) })
                result
            else
                RuleResult.IGNORE
        }
    }

    companion object {
        @JvmStatic
        fun fromString(rule: String): RuleTest {
            val s = rule.trim()
            val result: RuleResult = when (s[0]) {
                '+' -> RuleResult.ALLOW
                '-' -> RuleResult.DENY
                '!' -> RuleResult.WARN
                else -> throw Exception("Invalid rule prefix string")
            }
            val trimmedRule = s.substring(1).trimStart()
            var s2 = trimmedRule.replace('.', '/')
            s2 = if (s2.endsWith("*")) {
                Regex.escape(s2.substring(0, s2.length - 1)) + ".+"
            } else {
                Regex.escape(s2)
            }
            return RegexRuleTest(trimmedRule, Regex(s2), result)
        }
    }
}