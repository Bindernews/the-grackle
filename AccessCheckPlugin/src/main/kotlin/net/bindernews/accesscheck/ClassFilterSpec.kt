package net.bindernews.accesscheck

import org.gradle.internal.impldep.com.google.common.reflect.ClassPath.ClassInfo
import java.io.Serializable

/**
 * Specifies a set of packages and classes to be included and excluded,
 * to be used to filter for a specific set of classes in a classpath.
 */
interface ClassFilterSpec : Serializable {
    /**
     * Set of packages this rule applies to.
     */
    var packages: MutableSet<String>

    /**
     * Packages that are specifically excluded, overrides [packages].
     */
    var excludedPackages: MutableSet<String>

    /**
     * Set of classes to include, in addition to any classes found in the listed packages.
     * Overrides [excludedPackages].
     */
    var classes: MutableSet<String>

    /**
     * Classes that are specifically excluded, overrides [classes] and [packages].
     */
    var excludedClasses: MutableSet<String>

    /**
     * Returns `true` if the given class would be included by the filter.
     */
    fun isIncluded(packageName: String, className: String): Boolean {
        val fullName = "${packageName}.${className}"
        // Order is important for consistency with documentation.
        // Process the conditions in reverse order, to make the logic easier.
        if (excludedClasses.contains(fullName)) {
            return false
        }
        if (classes.contains(fullName)) {
            return true
        }
        if (excludedPackages.any { packageName.startsWith(it) }) {
            return false
        }
        if (packages.any { packageName.startsWith(it) }) {
            return true
        }
        return false;
    }

    class Impl() : ClassFilterSpec {
        override var packages: MutableSet<String> = hashSetOf()
        override var excludedPackages: MutableSet<String> = hashSetOf()
        override var classes: MutableSet<String> = hashSetOf()
        override var excludedClasses: MutableSet<String> = hashSetOf()
    }
}