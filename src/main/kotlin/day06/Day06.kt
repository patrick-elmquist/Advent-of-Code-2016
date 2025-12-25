package day06

import common.Input
import common.day
import kotlin.collections.mutableListOf

// answer #1: qqqluigu
// answer #2: lsoypmia

fun main() {
    day(n = 6) {
        part1 { input ->
            createColumnsWithCount(input)
                .map { map -> map.maxBy { it.value }.key }
                .joinToString("")
        }
        verify {
            expect result "qqqluigu"
            run test 1 expect "easter"
        }

        part2 { input ->
            createColumnsWithCount(input)
                .map { map -> map.minBy { it.value }.key }
                .joinToString("")
        }
        verify {
            expect result "lsoypmia"
            run test 1 expect "advent"
        }
    }
}

private fun createColumnsWithCount(input: Input): List<Map<Char, Int>> {
    val lines = input.lines
    val array = Array(lines.first().length) { mutableListOf<Char>() }
    lines.forEach { line ->
        line.forEachIndexed { index, c -> array[index] += c }
    }
    return array.map { column -> column.groupingBy { char -> char }.eachCount() }
}
