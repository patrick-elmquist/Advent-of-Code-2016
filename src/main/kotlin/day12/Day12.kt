package day12

import common.day

// answer #1: 318003
// answer #2: 9227657

fun main() {
    day(n = 12) {
        part1 { input ->
            val register = LongArray(4)
            val instructions = input.lines
            var pointer = 0
            while (pointer in instructions.indices) {
                pointer += executeInstruction(instructions[pointer], register)
            }
            register[0]
        }
        verify {
            expect result 318003L
            run test 1 expect 42L
        }

        part2 { input ->
            val register = LongArray(4)
            register[2] = 1L
            val instructions = input.lines
            var pointer = 0
            while (pointer in instructions.indices) {
                pointer += executeInstruction(instructions[pointer], register)
            }
            register[0]
        }
        verify {
            expect result 9227657L
        }
    }
}

private fun executeInstruction(instruction: String, register: LongArray): Int {
    val split = instruction.split(" ")
    return when (split.first()) {
        "cpy" -> {
            val x = split[1]
            val y = split[2].toRegIndex()
            register[y] = x.toLongOrNull() ?: register[x.toRegIndex()]
            1
        }
        "inc" -> {
            register[split[1].toRegIndex()]++
            1
        }
        "dec" -> {
            register[split[1].toRegIndex()]--
            1
        }
        "jnz" -> {
            val x = split[1]
            val xValue = x.toLongOrNull() ?: register[x.toRegIndex()]
            if (xValue != 0L) {
                split[2].toInt()
            } else {
                1
            }
        }
        else -> error("invalid ${split.first()}")
    }
}

private fun String.toRegIndex(): Int = single().code - 'a'.code
