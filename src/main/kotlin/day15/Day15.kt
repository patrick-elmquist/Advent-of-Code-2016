package day15

import common.Input
import common.day
import java.lang.Math.floorMod
import kotlin.reflect.KFunction1

// answer #1: 317371
// answer #2: 2080951

fun main() {
    day(n = 15) {
        part1 { input ->
            val conditions = parseDiscConditions(input)

            var t = 0
            while (!conditions.all { it(t) }) t++
            t
        }
        verify {
            expect result 317371
            run test 1 expect 5
        }

        part2 { input ->
            var discConditions = parseDiscConditions(input)

            val positions = 11
            val start = 0
            val expectedResult = floorMod(positions - 1 - discConditions.size, positions)
            fun additionalCondition(t: Int): Boolean = (start + t) % positions == expectedResult
            discConditions = discConditions + ::additionalCondition

            var t = 0
            while (!discConditions.all { disc -> disc(t) }) t++
            t
        }
        verify {
            expect result 2080951
        }
    }
}

private fun parseDiscConditions(input: Input): List<KFunction1<Int, Boolean>> =
    input.lines.mapIndexed { i, line ->
        val split = line.dropLast(1).split(" ")
        val positions = split[3].toInt()
        val start = split.last().toInt()
        val expectedResult = floorMod(positions - 1 - i, positions)
        fun condition(t: Int): Boolean = (start + t) % positions == expectedResult
        ::condition
    }
