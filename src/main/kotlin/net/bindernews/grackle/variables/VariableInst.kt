package net.bindernews.grackle.variables

/**
 * The backing field type that stores field data.
 */
data class VariableInst(var value: Int, var baseValue: Int, var upgraded: Boolean) {
    constructor() : this(-1)
    constructor(value: Int) : this(value, value, false)

    fun upgrade(delta: Int) {
        value += delta
        baseValue += delta
        upgraded = true
    }
}