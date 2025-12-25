package day01

import common.day
import common.util.Direction
import common.util.Vec2i
import common.util.nextCCW
import common.util.nextCW
import common.util.nextInDirection
import common.util.pointsBetween

// answer #1: 209
// answer #2: 136

fun main() {
    day(n = 1) {
        part1 { input ->
            var pos = Vec2i.Origin
            var dir = Direction.Up

            input.lines.first()
                .split(", ")
                .forEach { instruction ->
                    dir = if (instruction.first() == 'L') dir.nextCCW else dir.nextCW
                    pos = pos.nextInDirection(
                        direction = dir,
                        steps = instruction.drop(1).toInt(),
                    )
                }

            pos.distanceTo(Vec2i.Origin)
        }
        verify {
            expect result 209
        }

        part2 { input ->
            var pos = Vec2i.Origin
            var dir = Direction.Up

            val visited = mutableSetOf<Vec2i>()
            input.lines.first()
                .split(", ")
                .forEach { instruction ->
                    dir = if (instruction.first() == 'L') dir.nextCCW else dir.nextCW
                    val nextPos = pos.nextInDirection(
                        direction = dir,
                        steps = instruction.drop(1).toInt(),
                    )
                    pos.pointsBetween(nextPos).forEach {
                        if (it in visited) {
                            return@part2 it.distanceTo(Vec2i.Origin)
                        } else {
                            visited += it
                        }
                    }
                    visited += pos
                    pos = nextPos
                }
        }
        verify {
            expect result 136
        }
    }
}
