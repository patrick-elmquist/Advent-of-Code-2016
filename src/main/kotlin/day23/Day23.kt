package day23

import common.day

// answer #1: 10365
// answer #2: 479006925

fun main() {
    day(n = 23) {
        part1 { input ->
            val register = LongArray(4)
            if (input.lines.size > 7) {
                register[0] = 7
            }
            val instructions = input.lines.toMutableList()
            var pointer = 0
            while (pointer in instructions.indices) {
                pointer += executeInstruction(instructions, pointer, register)
            }
            register[0]
        }
        verify {
            expect result 10365L
            run test 1 expect 3L
        }

        part2 { input ->
            val register = LongArray(4)
            register[0] = 12

            val instructions = input.lines.mapIndexed { index, instruction ->
                when (index) {
                    4 -> "mul b d a"
                    in 5..9 -> "jnz 0 0"
                    else -> instruction
                }
            }.toMutableList()

            var pointer = 0
            while (pointer in instructions.indices) {
                pointer += executeInstruction(instructions, pointer, register)
            }
            register[0]
        }
        verify {
            expect result 479006925L
        }
    }
}

private fun executeInstruction(
    instructions: MutableList<String>,
    pointer: Int,
    register: LongArray,
): Int {
    val split = instructions[pointer].split(" ")
    return when (split.first()) {
        "cpy" -> {
            val x = split[1]
            if (split[2].first().isLetter()) {
                val y = split[2].toRegIndex()
                register[y] = x.toLongOrNull() ?: register[x.toRegIndex()]
            }
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
                val y = split[2]
                val yValue = (y.toLongOrNull() ?: register[y.toRegIndex()]).toInt()
                yValue
            } else {
                1
            }
        }

        "mul" -> {
            val x = split[1]
            val xValue = x.toLongOrNull() ?: register[x.toRegIndex()]

            val y = split[2]
            val yValue = y.toLongOrNull() ?: register[y.toRegIndex()]

            register[split[3].toRegIndex()] = xValue * yValue
            1
        }

        "tgl" -> {
            val x = split[1]
            val xValue = x.toLongOrNull() ?: register[x.toRegIndex()]

            val index = (pointer + xValue).toInt()
            if (index in instructions.indices) {
                val other = instructions[index]
                val updatedInstruction = when(other.split(" ").first()) {
                    "cpy" -> other.replace("cpy", "jnz")
                    "inc" -> other.replace("inc", "dec")
                    "dec" -> other.replace("dec", "inc")
                    "jnz" -> other.replace("jnz", "cpy")
                    "tgl" -> other.replace("tgl", "inc")
                    else -> error("")
                }
                instructions[index] = updatedInstruction
            }
            1
        }

        else -> error("invalid ${split.first()}")
    }
}

private fun String.toRegIndex(): Int = single().code - 'a'.code
