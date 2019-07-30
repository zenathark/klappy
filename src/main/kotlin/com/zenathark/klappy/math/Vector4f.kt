package com.zenathark.klappy.math

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector4f(val el: FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector4f

        if (sqrt((el zip other.el).fold(0.0) { acc, (j, k) ->
            acc + (j - k).pow(2)
        }) > 0.0001) return false

        return true
    }

    override fun hashCode(): Int {
        return el.contentHashCode()
    }

    override fun toString(): String {
        return "Vector4f(${el[0]}, ${el[1]}, ${el[2]}, ${el[3]})"
    }

    val x = el[0]
    val y = el[1]
    val z = el[2]
    val w = el[3]

    operator fun get(i: Int) = el[i]

    companion object {
        operator fun invoke(el: FloatArray) =
                when (el.count()) {
                    2 -> {
                        val (x, y) = el
                        this(x, y)
                    }
                    3 -> {
                        val (x, y, z) = el
                        this(x, y, z)
                    }
                    4 -> {
                        val (x, y, z, w) = el
                        this(x, y, z, w)
                    }
                    else ->
                        throw IllegalArgumentException("Illegal array length," +
                                " must be 2, 3 or 4 length")
                }

        operator fun invoke(x: Float, y: Float, z: Float, w: Float) =
                Vector4f(floatArrayOf(x, y, z, w))

        operator fun invoke(x: Float, y: Float, z: Float) =
                Vector4f(floatArrayOf(x, y, z, 1f))

        operator fun invoke(x: Float, y: Float) =
                Vector4f(floatArrayOf(x, y, 0f, 1f))
    }
}