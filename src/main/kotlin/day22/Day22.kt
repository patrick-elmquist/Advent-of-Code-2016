package day22

import common.day
import common.util.Vec2i

// answer #1: 967
// answer #2:

fun main() {
    day(n = 22) {
        part1 { input ->
            val nodes = input.lines.drop(2).map {
                val split = it.split("\\s+".toRegex())
                val name = split[0].removePrefix("/dev/grid/")
                Node(
                    name = name,
                    size = split[1].dropLast(1).toInt(),
                    used = split[2].dropLast(1).toInt(),
                    avail = split[3].dropLast(1).toInt(),
                    usePer = split[4].dropLast(1).toInt(),
                )
            }

            nodes
                .filter { node -> node.used > 0 }
                .flatMap { a ->
                    nodes.filter { it != a }
                        .filter { it.avail >= a.used }
                        .map { listOf(it, a).sortedBy { it.name } }
                }
                .distinct()
                .size
        }
        verify {
            expect result 967
        }

        part2 { input ->
            val nodes = input.lines.drop(2).associate {
                val split = it.split("\\s+".toRegex())
                val name = split[0].removePrefix("/dev/grid/")
                val loc = name.let {
                    val nameSplit = name.split("-")
                    Vec2i(nameSplit[1].drop(1).toInt(), nameSplit[2].drop(1).toInt())
                }
                loc to Node(
                    name = name,
                    size = split[1].dropLast(1).toInt(),
                    used = split[2].dropLast(1).toInt(),
                    avail = split[3].dropLast(1).toInt(),
                    usePer = split[4].dropLast(1).toInt(),
                )
            }

            "Part 2 not implemented yet"
        }
        verify {
            expect result -1
        }
    }
}

private data class Node(
    val name: String,
    val size: Int,
    val used: Int,
    val avail: Int,
    val usePer: Int,
)
