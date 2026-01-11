package day21

import common.day
import java.util.Collections

// answer #1: gcedfahb
// answer #2: hegbdcfa

fun main() {
    day(n = 21) {
        part1 { input ->
            val initial = input.lines.first()

            input.lines.drop(1)
                .fold(initial.toList()) { password, operation ->
                    password.performOperation(operation)
                }
                .joinToString("")
        }
        verify {
            expect result "gcedfahb"
            run test 1 expect "decab"
        }

        part2 { input ->
            val initial = "fbgdceah"

            input.lines.drop(1)
                .reversed()
                .fold(initial.toList()) { password, operation ->
                    password.performOperation(operation, inverse = true)
                }
                .joinToString("")
        }
        verify {
            expect result "hegbdcfa"
        }
    }
}

private fun List<Char>.performOperation(
    operation: String,
    inverse: Boolean = false,
): List<Char> {
    val split = operation.split(" ")
    return when (split.first()) {
        "swap" if split[1] == "position" -> swapPositions(split)
        "swap" if split[1] == "letter" -> swapLetters(split)

        "rotate" if split[1] == "based" -> if (inverse) {
            rotateBasedOnInverse(split[6].first())
        } else {
            rotateBasedOn(split[6].first())
        }

        "rotate" if split[1] == "left" -> if (inverse) {
            rotateRight(split[2].toInt())
        } else {
            rotateLeft(split[2].toInt())
        }

        "rotate" if split[1] == "right" -> if (inverse) {
            rotateLeft(split[2].toInt())
        } else {
            rotateRight(split[2].toInt())
        }

        "move" -> {
            val (x, y) = if (inverse) {
                split[5].toInt() to split[2].toInt()
            } else {
                split[2].toInt() to split[5].toInt()
            }
            val pass = this.toMutableList()
            val c = pass.removeAt(x)
            pass.add(y, c)
            pass.toList()
        }

        "reverse" -> {
            val x = split[2].toInt()
            val y = split[4].toInt()

            val start = take(x)
            val substring = subList(x, y + 1).reversed()
            val list =
                start + substring + takeLast(size - start.size - substring.size)
            list.toList()
        }

        else -> error("")
    }
}

private fun List<Char>.swapLetters(operation: List<String>): List<Char> {
    val a = operation[2].first()
    val b = operation[5].first()
    return map {
        when (it) {
            a -> b
            b -> a
            else -> it
        }
    }
}

private fun List<Char>.swapPositions(operation: List<String>): List<Char> =
    toMutableList().apply {
        val a = operation[2].toInt()
        val b = operation[5].toInt()
        this[a] = this[b].also { this[b] = this[a] }
    }

private fun List<Char>.rotateLeft(n: Int): List<Char> =
    toMutableList().apply { Collections.rotate(this, -n) }

private fun List<Char>.rotateRight(n: Int): List<Char> =
    toMutableList().apply { Collections.rotate(this, n) }

private fun List<Char>.rotateBasedOn(x: Char): List<Char> {
    val index = indexOf(x)
    val n = 1 + index + if (index >= 4) 1 else 0
    return rotateRight(n)
}

private fun List<Char>.rotateBasedOnInverse(x: Char): List<Char> {
    var prev = rotateLeft(1)
    while (prev.rotateBasedOn(x) != this) prev = prev.rotateLeft(1)
    return prev
}
