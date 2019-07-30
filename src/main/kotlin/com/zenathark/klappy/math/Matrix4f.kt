package com.zenathark.klappy.math

import kotlin.math.cos
import kotlin.math.sin


data class Matrix4f(val el: FloatArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix4f

        if (!el.contentEquals(other.el)) return false

        return true
    }

    override fun hashCode(): Int {
        return el.contentHashCode()
    }

    operator fun get(x: Int, y: Int) = el[4 * x + y]

    private fun pf(v: Float): String =
            String.format("%06.2f", v)

    private val tab = "-".repeat(6 * 3 + 2) + " + " + "-".repeat(6)

    override fun toString(): String =
            """ 
            Matrix4f ${hashCode()}
            ${pf(el[0])} ${pf(el[1])} ${pf(el[2])} | ${pf(el[3])}
            ${pf(el[4])} ${pf(el[5])} ${pf(el[6])} | ${pf(el[7])}
            ${pf(el[8])} ${pf(el[9])} ${pf(el[10])} | ${pf(el[11])}
            $tab
            ${pf(el[12])} ${pf(el[13])} ${pf(el[14])} | ${pf(el[15])}
        """.trimIndent()

    operator fun times(other: Matrix4f): Matrix4f {
        val ans = FloatArray(4 * 4)
        for (y in 0..3)
            for (x in 0..3) {
                var sum = 0f
                for (e in 0..3) sum += this[e, y] * other[x, e]
                ans[y + x * 4] = sum
            }
        return Matrix4f(ans)
    }

    operator fun times(other: Vector4f): Vector4f {
        val ans = FloatArray(4)
        for (y in 0..3) {
            var sum = 0f
            for (x in 0..3)
                sum += this[x, y] * other[x]
            ans[y] = sum
        }
        return Vector4f(ans)
    }
    companion object {
        private const val SIZE = 4 * 4

        val zero = FloatArray(SIZE)

        val identity = Matrix4f(floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        ))

        fun rotationMatrix(angle: Float): Matrix4f {
            val ans = FloatArray(SIZE)
            val r = Math.toRadians(angle.toDouble())
            val cos = cos(r).toFloat()
            val sin = sin(r).toFloat()
            ans[4 * 0 + 0] = cos
            ans[4 * 0 + 1] = sin
            ans[4 * 1 + 0] = -sin
            ans[4 * 1 + 1] = cos
            ans[4 * 2 + 2] = 1f
            ans[4 * 3 + 3] = 1f
            return Matrix4f(ans)
        }

        fun translationMatrix(v: Vector4f): Matrix4f {
            val ans = FloatArray(SIZE)
            for (y in 0..3) ans[4 * 3 + y] = v[y]
            return Matrix4f(ans)
        }

        fun orthographicMatrix(left: Float, right: Float, bottom: Float,
                              top: Float, near: Float, far: Float): Matrix4f {
            val ans = FloatArray(SIZE)
            ans[0 + 0 * 4] = 2.0f / (right - left)
            ans[1 + 1 * 4] = 2.0f / (top - bottom)
            ans[2 + 2 * 4] = 2.0f / (near - far)
            ans[0 + 3 * 4] = (left + right) / (left - right)
            ans[1 + 3 * 4] = (bottom + top) / (bottom - top)
            ans[2 + 3 * 4] = (far + near) / (far - near)
            return Matrix4f(ans)
        }

    }
}






