package day03

import common.day

// answer #1: 993
// answer #2: 1849

fun main() {
    day(n = 3) {
        part1 { input ->
            val regex = """\s+""".toRegex()
            input.lines.count { line ->
                val (a, b, c) = line.trim().split(regex).map(String::toInt)
                a + b > c && a + c > b && c + b > a
            }
        }
        verify {
            expect result 993
        }

        part2 { input ->
            val regex = """\s+""".toRegex()
            val columns = Array<MutableList<Int>>(3) { mutableListOf() }
            input.lines.forEach { line ->
                line.trim().split(regex).mapIndexed { i, s ->
                    columns[i].add(s.toInt())
                }
            }
            columns
                .flatMap { it }
                .chunked(3)
                .count { (a, b, c) ->
                    a + b > c && a + c > b && c + b > a
                }
        }
        verify {
            expect result 1849
        }
    }
}
