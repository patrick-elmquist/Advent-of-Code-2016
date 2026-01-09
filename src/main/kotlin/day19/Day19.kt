package day19

import common.day

// answer #1: 1841611
// answer #2: 1423634

fun main() {
    day(n = 19) {
        part1 { input ->
            val n = input.lines.first().toInt()
            var current = createLinkedList(n)

            while (current.nextNode != current) {
                val toBeRemoved = current.next
                current.nextNode = toBeRemoved.nextNode
                current = current.next
            }

            current.value
        }
        verify {
            expect result 1841611
            run test 1 expect 3
        }

        part2 { input ->
            var n = input.lines.first().toInt()
            var current = createLinkedList(n)
            var half = n / 2
            var middle = current
            repeat(half) { middle = middle.next }

            while (current.nextNode != current) {
                val currentMiddle = middle

                currentMiddle.prev.nextNode = middle.next
                currentMiddle.next.prevNode = currentMiddle.prev

                n--

                middle = if (n / 2 == half) {
                    middle.next.next
                } else {
                    middle.next
                }
                half = n / 2

                current = current.next
            }

            current.value
        }
        verify {
            expect result 1423634
            run test 1 expect 2
        }
    }
}

private fun createLinkedList(n: Int): Node {
    val nodes = (0..<n).map { Node(it + 1) }
    nodes.zipWithNext { a, b ->
        a.nextNode = b
        b.prevNode = a
    }
    nodes.first().prevNode = nodes.last()
    nodes.last().nextNode = nodes.first()
    return nodes.first()
}

private class Node(
    val value: Int,
    var nextNode: Node? = null,
    var prevNode: Node? = null,
) {
    val next: Node get() = nextNode!!
    val prev: Node get() = prevNode!!
}
