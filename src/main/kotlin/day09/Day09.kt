package day09

import common.day

// answer #1: 102239
// answer #2: 10780403063

fun main() {
    day(n = 9) {
        part1 { input ->
            decompress(input.lines.first()).length
        }
        verify {
            expect result 102239
        }

        part2 { input ->
            decompressRecursive(input.lines.first())
        }
        verify {
            expect result 10780403063L
        }
    }
}

private fun decompress(input: String): String {
    var string = input
    var out = ""

    while (string.isNotEmpty()) {
        val s1 = string.substringBefore('(')
        out += s1
        string = string.drop(s1.length + 1)
        if (s1 == string || string.isEmpty()) break


        val s2 = string.substringBefore(")")
        val (count, repeat) = s2.split("x").map(String::toInt)
        string = string.drop(s2.length + 1)

        val take = string.take(count)
        repeat(repeat) {
            out += take
        }
        string = string.drop(count)
    }

    return out
}

private fun decompressRecursive(input: String): Long {
    var string = input
    var len = 0L

    while (string.isNotEmpty()) {
        val s1 = string.substringBefore('(')
        len += s1.length

        if (s1 == string || string.isEmpty()) break
        string = string.drop(s1.length)

        val s2 = string.drop(1).substringBefore(")")
        val (count, repeat) = s2.split("x").map(String::toInt)
        string = string.drop(s2.length + 2)

        val take = string.take(count)
        len += repeat * decompressRecursive(take)
        string = string.drop(count)
    }

    return len
}
