package day05

import common.day
import java.security.MessageDigest

// answer #1: 2414bc77
// answer #2:

fun main() {
    day(n = 5) {
        part1 { input ->
            val doorId = input.lines.first()
            val password = mutableListOf<Char>()
            var index = 0
            while (password.size < 8) {
                val hash = md5Hash(doorId, index)
                if (hash.startsWith("00000")) {
                    password += hash[5]
                }
                index++
            }
            password.joinToString("")
        }
        verify {
            expect result "2414bc77"
            run test 1 expect "18f47a30"
        }

        part2 { input ->
            val doorId = input.lines.first()
            val password = CharArray(8) { ' ' }
            var index = 0
            while (password.contains(' ')) {
                val hash = md5Hash(doorId, index)
                if (hash.startsWith("00000")) {
                    val index = hash[5].digitToIntOrNull()
                        ?.takeIf { it in 0..7 }
                    if (index != null && password[index] == ' ') {
                        password[index] = hash[6]
                    }
                }
                index++
            }
            password.joinToString("")
        }
        verify {
            expect result "437e60fc"
            run test 1 expect "05ace8e3"
        }
    }
}

private val md5 = MessageDigest.getInstance("MD5")
private fun md5Hash(id: String, index: Int): String =
    md5.digest("$id$index".toByteArray()).toHexString()

