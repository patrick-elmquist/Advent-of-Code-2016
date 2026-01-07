package day11

import common.day
import java.util.PriorityQueue
import kotlin.math.abs

// answer #1: 47
// answer #2: 71

fun main() {
    day(n = 11) {
        part1 { input ->
            val floors = parseFloors(input.lines)
            solve(floors)
        }
        verify {
            expect result 47
            run test 1 expect 11
        }

        part2 { input ->
            val floors = parseFloors(input.lines)
                .mapIndexed { index, components ->
                    if (index == 0) {
                        val additionalComponents = listOf("el", "di")
                            .flatMap { listOf(Generator(it), Microchip(it)) }
                        components + additionalComponents
                    } else {
                        components
                    }
                }

            solve(floors)
        }
        verify {
            expect result 71
        }
    }
}

private fun solve(floors: List<List<Component>>): Int {
    val seenStates = mutableMapOf<State, Int>()
    val count = floors.sumOf { it.size }

    val queue = PriorityQueue(compareBy<State> { it.distance() }.thenBy { it.moves })
    queue += State(floors = floors, elevator = 0, moves = 0)
    while (queue.isNotEmpty()) {
        val state = queue.poll()

        val cache = seenStates[state]
        if (cache != null && state.moves >= cache) continue
        seenStates[state] = state.moves

        if (state.floors[3].size == count) {
            return state.moves
        }

        val doubleMoves = state.currentFloor
            .flatMap { createDoubleMoveStates(state, it) }

        val singleMoves = state.currentFloor
            .flatMap { createSingleMoveStates(state, it) }

        queue += (doubleMoves + singleMoves)
            .filter {
                val seen = seenStates[it]
                seen == null || it.moves < seen
            }
    }

    error("could not find a solution")
}

private fun createDoubleMoveStates(state: State, component: Component): List<State> {
    val availableComponents = state.currentFloor
        .filter {
            when (component) {
                it -> false
                is Microchip -> it is Microchip || it == Generator(component.name)
                is Generator -> it is Generator || it == Microchip(component.name)
            }
        }

    val possibilities = mutableListOf<Pair<Component, Int>>()
    for (component in availableComponents) {
        for (floor in state.floors.withIndex().filter { it.index != state.elevator }) {
            possibilities += component to floor.index
        }
    }

    return possibilities
        .filter { (other, index) ->
            canMoveToFloor(state, listOf(component, other), index)
        }
        .map { (other, index) ->
            State(
                elevator = index,
                moves = state.moves + abs(state.elevator - index),
                floors = state.floors.mapIndexed { i, floor ->
                    when {
                        index == i -> (floor + component + other).sortedBy { it.toString() }
                        i == state.elevator -> floor - component - other
                        else -> floor
                    }
                },
            )
        }.toList()
}

private fun createSingleMoveStates(
    state: State,
    component: Component,
): List<State> = state.floors.asSequence()
    .withIndex()
    .filter { it.index != state.elevator }
    .filter { it.value.isNotEmpty() }
    .filter { (index, _) ->
        canMoveToFloor(state, listOf(component), index)
    }
    .map { (index, _) ->
        State(
            elevator = index,
            moves = state.moves + abs(state.elevator - index),
            floors = state.floors.mapIndexed { i, floor ->
                when {
                    index == i -> (floor + component).sortedBy { it.toString() }
                    i == state.elevator -> floor - component
                    else -> floor
                }
            },
        )
    }.toList()

private fun canMoveToFloor(
    state: State,
    components: List<Component>,
    floorIndex: Int,
): Boolean {
    val range = if (floorIndex < state.elevator) {
        floorIndex..<state.elevator
    } else {
        (state.elevator + 1)..floorIndex
    }

    return range.all { index ->
        if (index == 0 && state.floors[0].isEmpty()) {
            false
        } else {
            isFloorValid(state.floors[index] + components)
        }
    }
}

private fun isFloorValid(floor: Collection<Component>): Boolean =
    when {
        floor.size < 2 -> true
        floor.filterIsInstance<Microchip>().all { Generator(it.name) in floor } -> true
        else -> false
    }

private fun parseFloors(lines: List<String>): List<List<Component>> =
    lines.map { line ->
        line.split("a ").drop(1)
            .map {
                val name = it.take(2)
                when {
                    "microchip" in it -> Microchip(name)
                    else -> Generator(name)
                }
            }
            .sortedBy { it.toString() }
    }

private sealed interface Component {
    val name: String
}

@JvmInline
private value class Generator(override val name: String) : Component {
    override fun toString(): String = "${name}G"
}

@JvmInline
private value class Microchip(override val name: String) : Component {
    override fun toString(): String = "${name}M"
}

private class State(
    val floors: List<List<Component>>,
    val elevator: Int,
    val moves: Int = 0,
) {
    val currentFloor get() = floors[elevator]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (elevator != other.elevator) return false
        if (floors != other.floors) return false

        return true
    }

    override fun hashCode(): Int {
        var result = elevator
        result = 31 * result + floors.hashCode()
        return result
    }

    override fun toString(): String = "State(elevator=$elevator, floors=$floors, moves=$moves)"

    fun distance(): Int = floors
        .mapIndexed { index, floor -> (3 - index) * floor.size }
        .sum()
}
