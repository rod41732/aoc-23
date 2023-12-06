/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    fun part1(input: List<String>): Int {
        return input
            .map {
                val (winnings, owned) = it.substringAfter(":").split("|").map {
                    it.trim().split(" ").filter { !it.isBlank() }.map { it.toInt() }
                }
                val cnt = owned.count { winnings.contains(it) }

                if (cnt == 0) {
                    0
                } else {
                    1 shl (cnt - 1)
                }
            }.sum()
    }

    fun part2(input: List<String>): Int {
        val matches = input
            .map {
                val (winnings, owned) = it.substringAfter(":").split("|").map {
                    it.trim().split(" ").filter { !it.isBlank() }.map { it.toInt() }
                }
                val cnt = owned.count { winnings.contains(it) }
                cnt
            }.toList()
        val counts = MutableList(matches.size) { 1 }
        matches.forEachIndexed { idx, match ->
            (1..match).forEach {
                counts[idx + it] += counts[idx]
            }
        }
        return counts.sum()
    }

    assertEqual(part1(testInput), 13)
    assertEqual(part2(testInput), 30)

    println("Part1: ${part1((input))}")
    println("Part2: ${part2((input))}")
}



