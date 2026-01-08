package day18

import common.day

// answer #1: 1956
// answer #2: 19995121

fun main() {
    day(n = 18) {
        part1 { input ->
            val firstRow = input.lines.first().map { if (it == '^') 1 else 0 }
            val rowCount = input.lines.last().toInt()
            countSafeTiles(firstRow, rowCount)
        }
        verify {
            expect result 1956
            run test 1 expect 38
        }

        part2 { input ->
            val firstRow = input.lines.first().map { if (it == '^') 1 else 0 }
            val rowCount = 400000
            countSafeTiles(firstRow, rowCount)
        }
        verify {
            expect result 19995121
        }
    }
}

private fun countSafeTiles(
    initialState: List<Int>,
    rowCount: Int,
): Int {
    var last = initialState.toIntArray()
    var temp = IntArray(last.size)
    var totalSafeTiles = last.count { it == 0 }
    repeat(rowCount - 1) {
        var rowSafeTiles = 0
        for (i in last.indices) {
            val left = last.getOrElse(i - 1) { 0 }
            val right = last.getOrElse(i + 1) { 0 }
            temp[i] = left xor right
            rowSafeTiles += left xor right xor 1
        }
        totalSafeTiles += rowSafeTiles
        last = temp.also { temp = last }
    }
    return totalSafeTiles
}
