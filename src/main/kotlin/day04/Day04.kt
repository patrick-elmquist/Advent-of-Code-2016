package day04

import common.day

// answer #1: 278221
// answer #2: 267

fun main() {
    day(n = 4) {
        part1 { input ->
            input.lines.sumOf { line ->
                val (name, id, checksum) = parseLine(line)
                if (isValid(name, checksum)) id else 0
            }
        }
        verify {
            expect result 278221
            run test 1 expect 1514
        }

        part2 { input ->
            input.lines
                .mapNotNull { line ->
                    val (name, id, checksum) = parseLine(line)
                    if (isValid(name, checksum)) {
                        id to decipher(name, id)
                    } else {
                        null
                    }
                }
                .single { (_, name) -> name.contains("north") }
                .first
        }
        verify {
            expect result 267
        }
    }
}

private fun isValid(name: String, checksum: String): Boolean =
    checksum == name
        .filter { it !in setOf(' ', '-') }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedWith(
            compareByDescending<Map.Entry<Char, Int>> { it.value }
                .thenBy { it.key },
        )
        .take(5)
        .map { it.key }
        .joinToString("")

private fun decipher(name: String, id: Int): String =
    name.map {
        if (it in 'a'..'z') {
            'a' + ((it - 'a' + id) % 26)
        } else {
            ' '
        }
    }.joinToString("")

private fun parseLine(line: String): Triple<String, Int, String> {
    val split = line.split("-")
    val (id, checksum) = split.last().split("[")
        .let { (a, b) -> a.toInt() to b.dropLast(1) }
    val name = split.dropLast(1).joinToString(" ")
    return Triple(name, id, checksum)
}
