package net.bindernews.grackle.interfaces

/**
 * Interface with default implementations allowing functional interfaces to easily add a "priority" feature.
 */
interface IPriority : Comparable<IPriority> {
    val priority: Int get() = 0

    override fun compareTo(other: IPriority): Int {
        val d = priority - other.priority
        return if (d != 0) { d } else { hashCode() - other.hashCode() };
    }
}