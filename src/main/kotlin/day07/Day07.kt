package day07

import common.day

// answer #1: 110
// answer #2: 242

fun main() {
    day(n = 7) {
        part1 { input ->
            input.lines.count(::hasTlsSupport)
        }
        verify {
            expect result 110
        }

        part2 { input ->
            input.lines.count { hasSslSupport(it) }
        }
        verify {
            expect result 242
        }
    }
}

private fun hasTlsSupport(line: String): Boolean {
    var string = line
    var isValid = false
    while (true) {
        val outside = string.substringBefore('[')
        if (hasAbba(outside)) isValid = true

        if (outside == string) return isValid

        val inside = string.drop(outside.length + 1).substringBefore(']')
        if (hasAbba(inside)) return false

        string = string.drop(outside.length + 1).drop(inside.length + 1)
    }
}

private fun hasSslSupport(line: String): Boolean {
    var string = line
    val outside = mutableSetOf<String>()
    val inside = mutableSetOf<String>()
    while (true) {
        val s1 = string.substringBefore('[')
        outside.addAll(findAllAba(s1))

        if (s1 == string) return outside.any { it.drop(1) + it[1] in inside }

        val s2 = string.drop(s1.length + 1).substringBefore(']')
        inside.addAll(findAllAba(s2))

        string = string.drop(s1.length + 1).drop(s2.length + 1)
    }
}

private fun hasAbba(string: String): Boolean =
    string.windowed(4).any {
        val a = it[0]
        val b = it[1]
        val c = it[2]
        val d = it[3]
        a == d && b == c && a != b
    }

private fun findAllAba(string: String): Set<String> =
    string.windowed(3)
        .filter {
            val a = it[0]
            val b = it[1]
            val c = it[2]
            a == c && a != b
        }
        .toSet()
