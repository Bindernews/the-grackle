package net.bindernews.accesscheck

import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.internal.artifacts.verification.verifier.VerificationFailure
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationException
import org.gradle.internal.classloader.VisitableURLClassLoader
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import com.google.common.reflect.ClassPath as GClassPath

abstract class AccessCheckTask : DefaultTask() {
    /**
     * Set of classpaths to search for applicable classes
     */
    @get:SkipWhenEmpty
    @get:Classpath
    abstract val classPath: ConfigurableFileCollection

    @get:Input
    abstract val checks: SetProperty<AccessCheckSpec>

    @get:Input
    abstract var includeJreClasspath: Boolean

    private val ruleTestCache = hashMapOf<String, RuleTest>()

    private fun getJREPaths(): List<URL> {
        val loader = Thread.currentThread().contextClassLoader
        return if (loader is VisitableURLClassLoader) {
            val prefix = "file:/" + System.getProperty("java.home")
            loader.getURLs().filter { it.file.startsWith(prefix) }
        } else {
            logger.error("unable to determine JRE classpath")
            listOf()
        }
    }

    fun check(a: Action<AccessCheckSpec>): AccessCheckSpec {
        val c = AccessCheckSpec.Impl()
        a.execute(c)
        checks.add(c)
        return c
    }

    @TaskAction
    fun checkRules() {
        // Build the class pool and add paths
        val loaderUrls = classPath.map { it.toURI().toURL() }.toMutableList()
        val parentLoader = if (includeJreClasspath) {
            ClassLoader.getSystemClassLoader()
        } else {
            null
        }
        val loader = URLClassLoader(loaderUrls.toTypedArray(), parentLoader)

        val pool = ClassPool(true)
        classPath.forEach { pool.appendClassPath(it.absolutePath) }
        val scanner = GClassPath.from(loader)
//        ruleTestCache.clear()
        // For each rule: gather classes to check against, generate test list, and run tests.
        for (rule in checks.get()) {
            // Gather classes to check against
            val ruleClasses = gatherRuleClasses(scanner, rule)
            logger.info("rule classes: $ruleClasses")
            // Cache the rules so that we don't have to re-generate them
            for (s in rule.rules) {
                ruleTestCache.computeIfAbsent(s) { RuleTest.fromString(s) }
            }
            // Run tests
            try {
                runTests(ruleClasses.map { pool.get(it.name) }, rule)
            } catch (e: RuleResultException) {
                throw GradleException("access check failed:\n${e.message!!}")
            }
        }
    }

    private fun runTests(classList: List<CtClass>, rule: AccessCheckSpec) {
        val testList = rule.rules.map { ruleTestCache[it]!! }
        for (cls in classList) {
            for (i in testList.indices) {
                val result = testList[i].test(cls)
                if (result != RuleResult.IGNORE) {
                    throw RuleResultException(cls.name, testList[i].description, result)
                }
            }
        }
    }

    private fun gatherRuleClasses(scanner: GClassPath, rule: AccessCheckSpec): Set<GClassPath.ClassInfo> {
        return scanner
            .allClasses
            .filter { it.isTopLevel && rule.filter.isIncluded(it.packageName, it.simpleName) }
            .toHashSet()
    }
}
