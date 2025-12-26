package day10

import common.day

// answer #1: 27
// answer #2: 13727

fun main() {
    day(n = 10) {
        part1 { input ->
            val instructions = input.lines.map { it.split(" ") }
            val (name, _) = string(instructions, breakOnBot = true)
            name
        }
        verify {
            expect result "27"
        }

        part2 { input ->
            val instructions = input.lines.map { it.split(" ") }

            val (_, outputs) = string(instructions)
            outputs
                .entries
                .sortedBy { it.key.toInt() }
                .take(3)
                .map { it.value.single() }
                .reduce(Int::times)
        }
        verify {
            expect result 13727
        }
    }
}

private fun string(
    instructions: List<List<String>>,
    breakOnBot: Boolean = false,
): Pair<String?, Map<String, List<Int>>> {
    val outputs = mutableMapOf<String, MutableList<Int>>()
    val bots = mutableMapOf<String, MutableList<Int>>()
    val queue = instructions.toMutableList()
    while (queue.isNotEmpty()) {
        val instruction = queue.removeFirst()

        when (instruction.first()) {
            "value" -> {
                val value = instruction[1].toInt()
                val bot = instruction.last()
                bots.getOrPut(bot) { mutableListOf() }.add(value)
            }

            "bot" -> {
                val bot = instruction[1]
                val values = bots.getOrPut(bot) { mutableListOf() }
                if (values.size < 2) {
                    queue += instruction
                } else {
                    val (low, high) = values.sorted()

                    if (breakOnBot && low == 17 && high == 61) {
                        return bot to emptyMap()
                    }
                    val lowReceiver = instruction[6]
                    if (instruction[5] == "output") {
                        outputs.getOrPut(lowReceiver) { mutableListOf() }.add(low)
                    } else {
                        bots.getOrPut(lowReceiver) { mutableListOf() }.add(low)
                    }

                    val highReceiver = instruction[11]
                    if (instruction[10] == "output") {
                        outputs.getOrPut(highReceiver) { mutableListOf() }.add(high)
                    } else {
                        bots.getOrPut(highReceiver) { mutableListOf() }.add(high)
                    }
                }
            }
        }
    }
    return null to outputs
}
