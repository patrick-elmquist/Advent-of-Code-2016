package day16

import common.day

// answer #1: 00000100100001100
// answer #2: 00011010100010010

fun main() {
    day(n = 16) {
        part1 { input ->
            val length = input.lines[1].toInt()

            var data = input.lines[0]
            while(data.length < length) {
                data = dragonCurve(data)
            }

            checksum(data.take(length))
        }
        verify {
            expect result "00000100100001100"
            run test 1 expect "01100"
        }

        part2 { input ->
            val length = 35651584

            var data = input.lines[0]
            while(data.length < length) {
                data = dragonCurve(data)
            }

            checksum(data.take(length))
        }
        verify {
            expect result "00011010100010010"
        }
    }
}

private fun dragonCurve(a: String): String {
    val b = a.reversed().map { if (it == '1') '0' else '1' }.joinToString("")
    return a + '0' + b
}

private fun checksum(data: String): String {
    var checksum = data
    do {
        checksum = checksum.chunked(2).joinToString("") {
            if (it in setOf("00", "11")) "1" else "0"
        }
    } while (checksum.length % 2 == 0)
    return checksum
}
