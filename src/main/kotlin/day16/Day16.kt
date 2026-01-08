package day16

import common.day

// answer #1: 00000100100001100
// answer #2: 00011010100010010

fun main() {
    day(n = 16) {
        part1 { input ->
            solve(initial = input.lines[0], length = input.lines[1].toInt())
        }
        verify {
            expect result "00000100100001100"
            run test 1 expect "01100"
        }

        part2 { input ->
            solve(initial = input.lines[0], length = 35651584)
        }
        verify {
            expect result "00011010100010010"
        }
    }
}

private fun solve(initial: String, length: Int): String {
    var data = initial
    do data = dragonCurve(data) while (data.length < length)
    
    var sum = data.take(length)
    do sum = checksum(sum) while (sum.length % 2 == 0)

    return sum
}

private fun dragonCurve(a: String): String {
    val b = a.reversed().map { if (it == '1') '0' else '1' }.joinToString("")
    return a + '0' + b
}

private fun checksum(data: String): String =
    data.chunked(2).joinToString("") { if (it in setOf("00", "11")) "1" else "0" }
