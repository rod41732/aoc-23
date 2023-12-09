/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    fun List<Int>.diff() = this.zipWithNext { a, b -> b - a }
    fun List<Int>.extrapolate(): List<Int> = this + when {
        this.all { it == 0 } -> 0
        else -> this.last() + this.diff().extrapolate().last()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.toInts().extrapolate().last()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            it.toInts().reversed().extrapolate().last()
        }
    }

    val testInput = readInput("Day09_test")
    assertEqual(part1(testInput), 114)
    assertEqual(part2(testInput), 2)

    val input = readInput("Day09")
    println("Part1: ${part1(input)}") // 1939607039
    println("Part2: ${part2(input)}") // 1041
}

