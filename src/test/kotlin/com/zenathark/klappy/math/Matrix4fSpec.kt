package com.zenathark.klappy.math

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import kotlin.math.abs

class Matrix4fGenerator : Gen<Matrix4f> {
    override fun constants() = emptyList<Matrix4f>()
    override fun random() = generateSequence {
        Matrix4f(FloatArray(16) { Gen.float().random().first() })
    }
}

class Vector4fGenerator : Gen<Vector4f> {
    override fun constants() = emptyList<Vector4f>()
    override fun random() = generateSequence {
        Vector4f(FloatArray(4) { Gen.float().random().first() })
    }
}

class Matrix4fSpec : FunSpec() {

    init {
        test("Identity times any matrix must return the same matrix") {
            forAll(Matrix4fGenerator()) { other: Matrix4f ->
                (other * Matrix4f.identity) == other
            }
        }

        test("Create a translation matrix") {
            forAll(Vector4fGenerator()) { translationVector: Vector4f ->
                val mtx = Matrix4f.translationMatrix(translationVector)
                mtx[3, 0] == translationVector[0] &&
                mtx[3, 1] == translationVector[1] &&
                mtx[3, 2] == translationVector[2] &&
                mtx[3, 3] == translationVector[3]
            }
        }

        test("Rotation Matrix must rotate a vector") {
            val input = Vector4f(1f, 0f)
            val degrees = floatArrayOf(45f, 90f, 135f, 180f, -45f, -90f, -135f, -180f)
            val rotationMatrices = degrees.map { Matrix4f.rotationMatrix(it) }
            val expected = arrayOf(
                    0.7071f to 0.7071f, 0f to 1f, -0.7071f to 0.7071f, -1f to 0f,
                    0.7071f to -0.7071f, 0f to -1f, -0.7071f to -0.7071f, -1f to 0f)
                    .map { (x, y) ->
                        Vector4f(x, y)
                    }
            val rotated = rotationMatrices.map { it * input }
            assertSoftly {
                (rotated zip expected).forEach { (got, expected) ->
                    got shouldBe expected
                }
            }
        }
    }
}