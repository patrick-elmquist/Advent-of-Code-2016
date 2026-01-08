package day14

import common.day
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.IntStream

// answer #1: 15168
// answer #2: 20864

private val md5 = ThreadLocal<MessageDigest>.withInitial {
    MessageDigest.getInstance("MD5")
}

fun main() {
    day(n = 14) {
        part1 { input ->
            val salt = input.lines.first()

            var index = -1
            val cache = mutableMapOf<Int, String>()
            val keys = mutableSetOf<String>()

            fun hashWithCache(index: Int): String =
                cache.getOrPut(index) { md5.get().hash("$salt$index") }

            while (keys.size < 64) {
                index++

                val hash = hashWithCache(index)

                val repeatingChar = hash.findRepeatingChar(3)
                if (repeatingChar != null) {
                    for (i in 1..1000) {
                        val other = hashWithCache(index + i)

                        if (containsFiveRepeatingChars(other, repeatingChar)) {
                            keys.add(hash)
                            break
                        }
                    }
                }
            }

            index
        }
        verify {
            expect result 15168
            run test 1 expect 22728
        }

        part2 { input ->
            val salt = input.lines.first()

            var index = -1
            val cache = ConcurrentHashMap<Int, String>()
            val keys = mutableSetOf<String>()

            fun hashWithCache(index: Int): String =
                cache.getOrPut(index) {
                    val md5 = md5.get()
                    var hash = "$salt$index"
                    repeat(2017) {
                        hash = md5.hash(hash)
                    }
                    hash
                }

            while (keys.size < 64) {
                index++

                val hash = hashWithCache(index)

                val repeatingChar = hash.findRepeatingChar(3)
                if (repeatingChar != null) {
                    IntStream.rangeClosed(1, 1000)
                        .parallel()
                        .anyMatch { i ->
                            val containsRepeat =
                                containsFiveRepeatingChars(hashWithCache(index + i), repeatingChar)
                            if (containsRepeat) keys.add(hash)
                            containsRepeat
                        }
                }
            }

            index
        }
        verify {
            expect result 20864
            run test 1 expect 22551
        }
    }
}

private fun String.findRepeatingChar(n: Int): Char? =
    windowed(n)
        .firstOrNull { it.toSet().size == 1 }
        ?.firstOrNull()

private fun containsFiveRepeatingChars(hash: String, char: Char): Boolean =
    hash.windowed(5).any { it.all { c -> c == char } }

private fun MessageDigest.hash(string: String) =
    digest(string.toByteArray()).toHexString()
