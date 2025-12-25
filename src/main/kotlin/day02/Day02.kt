package day02

import common.day
import common.util.Vec2i

// answer #1: 82958
// answer #2: B3DB8

fun main() {
    day(n = 2) {
        part1 { input ->
            findBathroomCode(
                instructions = input.lines,
                numpad = numpad,
            )
        }
        verify {
            expect result "82958"
            run test 1 expect "1985"
        }

        part2 { input ->
            findBathroomCode(
                instructions = input.lines,
                numpad = advancedNumpad,
            )
        }
        verify {
            expect result "B3DB8"
            run test 1 expect "5DB3"
        }
    }
}

private fun findBathroomCode(
    instructions: List<String>,
    numpad: Map<Vec2i, Char>,
): String {
    var position = Vec2i(0, 0)
    val digits = mutableListOf<Vec2i>()
    instructions.forEach { movements ->
        movements.forEach { movement ->
            val nextPos = position + when (movement) {
                'L' -> Vec2i(-1, 0)
                'R' -> Vec2i(1, 0)
                'U' -> Vec2i(0, -1)
                else -> Vec2i(0, 1)
            }
            if (nextPos in numpad) position = nextPos
        }
        digits.add(position)
    }
    return digits.map { numpad.getValue(it) }.joinToString("")
}

private val numpad = mapOf(
    Vec2i(-1, -1) to '1',
    Vec2i(0, -1) to '2',
    Vec2i(1, -1) to '3',

    Vec2i(-1, 0) to '4',
    Vec2i(0, 0) to '5',
    Vec2i(1, 0) to '6',

    Vec2i(-1, 1) to '7',
    Vec2i(0, 1) to '8',
    Vec2i(1, 1) to '9',
)

private val advancedNumpad = mapOf(
    Vec2i(2, -2) to '1',

    Vec2i(1, -1) to '2',
    Vec2i(2, -1) to '3',
    Vec2i(3, -1) to '4',

    Vec2i(0, 0) to '5',
    Vec2i(1, 0) to '6',
    Vec2i(2, 0) to '7',
    Vec2i(3, 0) to '8',
    Vec2i(4, 0) to '9',

    Vec2i(1, 1) to 'A',
    Vec2i(2, 1) to 'B',
    Vec2i(3, 1) to 'C',

    Vec2i(2, 2) to 'D',
)
