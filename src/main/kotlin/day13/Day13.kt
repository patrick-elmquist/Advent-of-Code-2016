package day13

import common.day
import common.util.Vec2i
import common.util.neighbors
import java.util.PriorityQueue

// answer #1: 90
// answer #2: 135

fun main() {
    day(n = 13) {
        part1 { input ->
            val (luckyNumber, end) = input.lines.first().let {
                val (n, pos) = it.split(" ")
                n.toInt() to pos.split(",").let { (x, y) -> Vec2i(x.toInt(), y.toInt()) }
            }

            val stepsTaken = mutableMapOf<Vec2i, Int>()
            walk(stepsTaken, luckyNumber, endCondition = { pos, _ -> pos == end })
            stepsTaken[end]
        }

        verify {
            expect result 90
            run test 1 expect 11
        }

        part2 { input ->
            val luckyNumber = input.lines.first().split(" ").first().toInt()
            val stepsTaken = mutableMapOf<Vec2i, Int>()
            walk(stepsTaken, luckyNumber, skipCondition = { _, steps -> steps > 50 })
            stepsTaken.count { it.value <= 50 }
        }
        verify {
            expect result 135
        }
    }
}

private fun walk(
    stepsTaken: MutableMap<Vec2i, Int>,
    luckyNumber: Int,
    skipCondition: (Vec2i, Int) -> Boolean = { _, _ -> false },
    endCondition: (Vec2i, Int) -> Boolean = { _, _ -> false },
) {
    val start = Vec2i(1, 1)
    val map = mutableMapOf<Vec2i, Char>()
    val queue = PriorityQueue<Vec2i>(compareBy { stepsTaken.getValue(it) })
    stepsTaken[start] = 0
    queue += start

    while (queue.isNotEmpty()) {
        val position = queue.poll()
        val steps = stepsTaken.getValue(position)

        if (endCondition(position, steps)) {
            return
        }

        if (skipCondition(position, steps)) {
            continue
        }

        position.neighbors()
            .filter { it.x >= 0 && it.y >= 0 }
            .onEach { map.computeIfAbsent(it) { key -> if (isWall(key, luckyNumber)) '#' else '.' } }
            .filter { map[it] == '.' }
            .forEach {
                val prevSteps = stepsTaken[it]
                val newSteps = steps + 1
                when {
                    prevSteps == null -> {
                        stepsTaken[it] = newSteps
                        queue += it
                    }

                    newSteps < prevSteps -> {
                        stepsTaken[it] = newSteps
                        queue += it
                    }
                }
            }
    }
}

private fun isWall(position: Vec2i, luckyNumber: Int): Boolean {
    val (x, y) = position
    var number = x * x + 3 * x + 2 * x * y + y + y * y
    number += luckyNumber
    return number.countOneBits() % 2 == 1
}
