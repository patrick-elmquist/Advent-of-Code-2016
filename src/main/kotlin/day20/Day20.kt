package day20

import common.Input
import common.day

// answer #1: 17348574
// answer #2: 104

fun main() {
    day(n = 20) {
        part1 { input ->
            parseRanges(input)
                .sortedBy { it.first }
                .fold(0L) { min, range -> if (min in range) range.last + 1 else min }
        }
        verify {
            expect result 17348574L
            run test 1 expect 3L
        }

        part2 { input ->
            val queue = parseRanges(input).toMutableList()
            val mergedRanges = mutableSetOf<LongRange>()
            while (queue.isNotEmpty()) {
                val range = queue.removeFirst()
                val otherIndex = queue
                    .indexOfFirst { other -> other != range && canMerge(other, range) }
                if (otherIndex != -1) {
                    queue += merge(range, queue.removeAt(otherIndex))
                } else {
                    mergedRanges += range
                }
            }

            4294967295L + 1L - mergedRanges.sumOf { it.last - it.first + 1 }
        }
        verify {
            expect result 104L
        }
    }
}

private fun parseRanges(input: Input): List<LongRange> =
    input.lines
        .map { it.split("-").map(String::toLong) }
        .map { (a, b) -> a..b }

// a   |--------|             |--------|   |--------|     |----|
// b        |--------|   |--------|          |----|     |--------|
private fun canMerge(a: LongRange, b: LongRange): Boolean =
    when {
        a.first < b.first && a.last in b -> true
        b.first < a.first && b.last in a -> true
        b.first in a && b.last in a -> true
        a.first in b && a.last in b -> true
        else -> false
    }

private fun merge(a: LongRange, b: LongRange): LongRange =
    minOf(a.first, b.first)..maxOf(a.last, b.last)
