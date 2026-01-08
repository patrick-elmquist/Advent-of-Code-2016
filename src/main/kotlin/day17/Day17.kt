package day17

import common.day
import common.util.Vec2i
import common.util.aboveNeighbour
import common.util.belowNeighbour
import common.util.leftNeighbour
import common.util.rightNeighbour
import java.security.MessageDigest
import java.util.PriorityQueue

// answer #1: RLDUDRDDRR
// answer #2: 590

fun main() {
    day(n = 17) {
        part1 { input ->
            val passcode = input.lines.first()
            solve(passcode).first
        }
        verify {
            expect result "RLDUDRDDRR"
            run test 1 expect "DDRRRD"
        }

        part2 { input ->
            val passcode = input.lines.first()
            solve(passcode).second
        }
        verify {
            expect result 590
            run test 1 expect 370
        }
    }
}

private fun solve(passcode: String): Pair<String, Int> {
    val md5 = MessageDigest.getInstance("MD5")
    val start = Vec2i(0, 0)
    val end = Vec2i(3, 3)

    val open = "bcdef".toSet()

    val queue = PriorityQueue(
        compareBy<Pair<Vec2i, String>> { it.second.length }.thenBy { it.first.distanceTo(end) },
    )
    queue += start to passcode

    var min = Int.MAX_VALUE to ""
    var max = Int.MIN_VALUE

    while (queue.isNotEmpty()) {
        val (pos, path) = queue.poll()

        if (pos == end) {
            val length = path.removePrefix(passcode).length
            if (length < min.first) {
                min = length to path.removePrefix(passcode)
            }
            max = maxOf(max, length)
            continue
        }

        val hash = md5.digest(path.toByteArray()).toHexString().take(4).toList()
        val neighbours = listOf(
            pos.aboveNeighbour to 'U',
            pos.belowNeighbour to 'D',
            pos.leftNeighbour to 'L',
            pos.rightNeighbour to 'R',
        )

        val doors = hash.zip(neighbours)
            .filter { (char, _) -> char in open }
            .filter { (_, position) -> position.first.x in 0..3 && position.first.y in 0..3 }
            .map { (_, neighbour) -> neighbour.first to path + neighbour.second }

        queue += doors
    }
    return min.second to max
}
