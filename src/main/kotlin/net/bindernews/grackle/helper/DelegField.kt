package net.bindernews.grackle.helper

import net.bindernews.bnsts.IField
import kotlin.reflect.KProperty

/**
 * Wrapper for [IField] that allows using it as a delegate.
 */
class DelegField<T, F>(private val field: IField<T, F>) : IField<T, F> by field {
    operator fun setValue(t: T, p: KProperty<*>, value: F) { set(t, value) }
    operator fun getValue(t: T, p: KProperty<*>): F = get(t)
}
