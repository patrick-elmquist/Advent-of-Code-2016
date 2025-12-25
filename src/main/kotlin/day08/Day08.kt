package day08

import common.day
import common.util.Vec2i

// answer #1:
// answer #2:

fun main() {
    day(n = 8) {
        part1 { input ->
            val display = mutableSetOf<Vec2i>()
            runInstructions(input.lines, display)
            display.size
        }
        verify {
            expect result 123
        }

        part2 { input ->

            val display = mutableSetOf<Vec2i>()
            runInstructions(input.lines, display)

            print(display)
            // ##  #### ###  #  # ###  #### ###    ## ###   ###
            // #  # #    #  # #  # #  #    # #  #    # #  # #
            // #  # ###  ###  #  # #  #   #  ###     # #  # #
            // #### #    #  # #  # ###   #   #  #    # ###   ##
            // #  # #    #  # #  # #    #    #  # #  # #       #
            // #  # #    ###   ##  #    #### ###   ##  #    ###

            "AFBUPZBJPS"
        }
        verify {
            expect result Unit
        }
    }
}

private fun runInstructions(
    instructions: List<String>,
    display: MutableSet<Vec2i>,
    width: Int = 50,
    height: Int = 6,
) {
    instructions.forEach { instruction ->
        val split = instruction.split(" ")
        when (split.first()) {
            "rect" -> {
                val (x, y) = split.last().split("x").map(String::toInt)
                (0 until y).forEach { y ->
                    (0 until x).forEach { x ->
                        display.add(Vec2i(x, y))
                    }
                }
            }

            "rotate" if split[1] == "row" -> {
                val y = split[2].split("=").last().toInt()
                val value = split.last().toInt()

                val affected = display.filter { it.y == y }
                display.removeAll(affected.toSet())
                affected.forEach { affected ->
                    val newX = (affected.x + value) % width
                    display += Vec2i(newX, affected.y)
                }
            }

            else -> {
                val x = split[2].split("=").last().toInt()
                val value = split.last().toInt()

                val affected = display.filter { it.x == x }
                display.removeAll(affected.toSet())
                affected.forEach { affected ->
                    val newY = (affected.y + value) % height
                    display += Vec2i(affected.x, newY)
                }
            }
        }
    }
}

private fun print(
    display: MutableSet<Vec2i>,
    width: Int = 50,
    height: Int = 6,
) {
    val minX = 0
    val minY = 0
    println()
    for (y in minY..height) {
        for (x in minX..width) {
            val c = if (Vec2i(x, y) in display) {
                "#"
            } else {
                " "
            }
            print(c)
        }
        println()
    }
    println()
}

